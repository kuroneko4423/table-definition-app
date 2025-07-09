package com.example.tabledef.service;

import com.example.tabledef.dto.TableDefinitionDto;
import com.example.tabledef.dto.ColumnDefinitionDto;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class ExportService {
    
    private final MessageSource messageSource;
    
    public byte[] exportToExcel(List<TableDefinitionDto> tables, Locale locale) throws IOException {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            
            // Create styles
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dataStyle = createDataStyle(workbook);
            
            for (TableDefinitionDto table : tables) {
                Sheet sheet = workbook.createSheet(table.getPhysicalName());
                
                // Table information
                int rowNum = 0;
                Row tableInfoRow = sheet.createRow(rowNum++);
                tableInfoRow.createCell(0).setCellValue(getMessage("table.schemaName", locale));
                tableInfoRow.createCell(1).setCellValue(table.getSchemaName() != null ? table.getSchemaName() : "");
                
                tableInfoRow = sheet.createRow(rowNum++);
                tableInfoRow.createCell(0).setCellValue(getMessage("table.physicalName", locale));
                tableInfoRow.createCell(1).setCellValue(table.getPhysicalName());
                
                tableInfoRow = sheet.createRow(rowNum++);
                tableInfoRow.createCell(0).setCellValue(getMessage("table.logicalName", locale));
                tableInfoRow.createCell(1).setCellValue(table.getLogicalName());
                
                tableInfoRow = sheet.createRow(rowNum++);
                tableInfoRow.createCell(0).setCellValue(getMessage("table.description", locale));
                tableInfoRow.createCell(1).setCellValue(table.getDescription() != null ? table.getDescription() : "");
                
                // Empty row
                rowNum++;
                
                // Column headers
                Row headerRow = sheet.createRow(rowNum++);
                String[] headers = {
                    getMessage("column.physicalName", locale),
                    getMessage("column.logicalName", locale),
                    getMessage("column.dataType", locale),
                    getMessage("column.length", locale),
                    getMessage("column.nullable", locale),
                    getMessage("column.primaryKey", locale),
                    getMessage("column.unique", locale),
                    getMessage("column.defaultValue", locale),
                    getMessage("column.description", locale)
                };
                
                for (int i = 0; i < headers.length; i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(headers[i]);
                    cell.setCellStyle(headerStyle);
                }
                
                // Column data
                if (table.getColumns() != null) {
                    for (ColumnDefinitionDto column : table.getColumns()) {
                        Row dataRow = sheet.createRow(rowNum++);
                        dataRow.createCell(0).setCellValue(column.getPhysicalName());
                        dataRow.createCell(1).setCellValue(column.getLogicalName());
                        dataRow.createCell(2).setCellValue(column.getDataType());
                        dataRow.createCell(3).setCellValue(column.getLength() != null ? column.getLength().toString() : "");
                        dataRow.createCell(4).setCellValue(column.isNullable() ? "可" : "不可");
                        dataRow.createCell(5).setCellValue(column.isPrimaryKey() ? "○" : "");
                        dataRow.createCell(6).setCellValue(column.isUniqueKey() ? "○" : "");
                        dataRow.createCell(7).setCellValue(column.getDefaultValue() != null ? column.getDefaultValue() : "");
                        dataRow.createCell(8).setCellValue(column.getDescription() != null ? column.getDescription() : "");
                        
                        for (int i = 0; i < 9; i++) {
                            dataRow.getCell(i).setCellStyle(dataStyle);
                        }
                    }
                }
                
                // Auto-size columns
                for (int i = 0; i < headers.length; i++) {
                    sheet.autoSizeColumn(i);
                }
            }
            
            workbook.write(bos);
            return bos.toByteArray();
        }
    }
    
    public String exportToMarkdown(List<TableDefinitionDto> tables, Locale locale) {
        StringBuilder markdown = new StringBuilder();
        
        markdown.append("# ").append(getMessage("app.title", locale)).append("\n\n");
        
        for (TableDefinitionDto table : tables) {
            String fullTableName = table.getSchemaName() != null && !table.getSchemaName().isEmpty() 
                ? table.getSchemaName() + "." + table.getPhysicalName()
                : table.getPhysicalName();
            
            markdown.append("## ").append(table.getLogicalName())
                    .append(" (").append(fullTableName).append(")\n\n");
            
            if (table.getDescription() != null && !table.getDescription().isEmpty()) {
                markdown.append(table.getDescription()).append("\n\n");
            }
            
            // Column table
            markdown.append("| ")
                    .append(getMessage("column.physicalName", locale)).append(" | ")
                    .append(getMessage("column.logicalName", locale)).append(" | ")
                    .append(getMessage("column.dataType", locale)).append(" | ")
                    .append(getMessage("column.length", locale)).append(" | ")
                    .append(getMessage("column.nullable", locale)).append(" | ")
                    .append(getMessage("column.primaryKey", locale)).append(" | ")
                    .append(getMessage("column.unique", locale)).append(" | ")
                    .append(getMessage("column.defaultValue", locale)).append(" | ")
                    .append(getMessage("column.description", locale)).append(" |\n");
            
            markdown.append("|---|---|---|---|---|---|---|---|---|\n");
            
            if (table.getColumns() != null) {
                for (ColumnDefinitionDto column : table.getColumns()) {
                    markdown.append("| ")
                            .append(column.getPhysicalName()).append(" | ")
                            .append(column.getLogicalName()).append(" | ")
                            .append(column.getDataType()).append(" | ")
                            .append(column.getLength() != null ? column.getLength() : "").append(" | ")
                            .append(column.isNullable() ? "可" : "不可").append(" | ")
                            .append(column.isPrimaryKey() ? "○" : "").append(" | ")
                            .append(column.isUniqueKey() ? "○" : "").append(" | ")
                            .append(column.getDefaultValue() != null ? column.getDefaultValue() : "").append(" | ")
                            .append(column.getDescription() != null ? column.getDescription() : "").append(" |\n");
                }
            }
            
            markdown.append("\n");
        }
        
        return markdown.toString();
    }
    
    public String exportToDDL(List<TableDefinitionDto> tables, Locale locale) {
        StringBuilder ddl = new StringBuilder();
        
        ddl.append("-- ").append(getMessage("app.title", locale)).append("\n");
        ddl.append("-- Generated at: ").append(java.time.LocalDateTime.now()).append("\n\n");
        
        for (TableDefinitionDto table : tables) {
            // Table name with schema
            String fullTableName = table.getSchemaName() != null && !table.getSchemaName().isEmpty() 
                ? table.getSchemaName() + "." + table.getPhysicalName()
                : table.getPhysicalName();
            
            // Drop table if exists
            ddl.append("-- ").append(table.getLogicalName()).append("\n");
            ddl.append("DROP TABLE IF EXISTS ").append(fullTableName).append(";\n\n");
            
            // Create table
            ddl.append("CREATE TABLE ").append(fullTableName).append(" (\n");
            
            if (table.getColumns() != null && !table.getColumns().isEmpty()) {
                for (int i = 0; i < table.getColumns().size(); i++) {
                    ColumnDefinitionDto column = table.getColumns().get(i);
                    ddl.append("    ").append(column.getPhysicalName()).append(" ");
                    
                    // Data type with length
                    String dataType = column.getDataType();
                    if (column.getLength() != null && needsLength(dataType)) {
                        if (column.getPrecision() != null && column.getScale() != null) {
                            ddl.append(dataType).append("(").append(column.getPrecision()).append(",").append(column.getScale()).append(")");
                        } else {
                            ddl.append(dataType).append("(").append(column.getLength()).append(")");
                        }
                    } else {
                        ddl.append(dataType);
                    }
                    
                    // NOT NULL constraint
                    if (!column.isNullable()) {
                        ddl.append(" NOT NULL");
                    }
                    
                    // Default value
                    if (column.getDefaultValue() != null && !column.getDefaultValue().isEmpty()) {
                        ddl.append(" DEFAULT ");
                        if (isStringType(dataType)) {
                            ddl.append("'").append(column.getDefaultValue()).append("'");
                        } else {
                            ddl.append(column.getDefaultValue());
                        }
                    }
                    
                    // Comment
                    if (column.getDescription() != null && !column.getDescription().isEmpty()) {
                        ddl.append(" COMMENT '").append(column.getDescription()).append("'");
                    }
                    
                    if (i < table.getColumns().size() - 1) {
                        ddl.append(",");
                    }
                    ddl.append("\n");
                }
                
                // Primary key constraint
                List<String> primaryKeys = table.getColumns().stream()
                    .filter(ColumnDefinitionDto::isPrimaryKey)
                    .map(ColumnDefinitionDto::getPhysicalName)
                    .collect(java.util.stream.Collectors.toList());
                
                if (!primaryKeys.isEmpty()) {
                    ddl.append("    , PRIMARY KEY (");
                    ddl.append(String.join(", ", primaryKeys));
                    ddl.append(")\n");
                }
                
                // Unique constraints
                List<String> uniqueKeys = table.getColumns().stream()
                    .filter(ColumnDefinitionDto::isUniqueKey)
                    .map(ColumnDefinitionDto::getPhysicalName)
                    .collect(java.util.stream.Collectors.toList());
                
                for (String uniqueKey : uniqueKeys) {
                    ddl.append("    , UNIQUE (").append(uniqueKey).append(")\n");
                }
            }
            
            ddl.append(")");
            
            // Table comment
            if (table.getDescription() != null && !table.getDescription().isEmpty()) {
                ddl.append(" COMMENT = '").append(table.getDescription()).append("'");
            }
            
            ddl.append(";\n\n");
        }
        
        return ddl.toString();
    }
    
    private boolean needsLength(String dataType) {
        return dataType.equals("VARCHAR") || dataType.equals("CHAR") || dataType.equals("DECIMAL");
    }
    
    private boolean isStringType(String dataType) {
        return dataType.equals("VARCHAR") || dataType.equals("CHAR") || dataType.equals("TEXT");
    }
    
    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        
        return style;
    }
    
    private CellStyle createDataStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        return style;
    }
    
    private String getMessage(String code, Locale locale) {
        return messageSource.getMessage(code, null, locale);
    }
}
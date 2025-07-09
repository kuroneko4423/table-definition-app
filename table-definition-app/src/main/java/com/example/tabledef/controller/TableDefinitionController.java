package com.example.tabledef.controller;

import com.example.tabledef.dto.TableDefinitionDto;
import com.example.tabledef.service.TableDefinitionService;
import com.example.tabledef.service.ExportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/projects/{projectId}")
@RequiredArgsConstructor
public class TableDefinitionController {
    
    private final TableDefinitionService tableService;
    private final ExportService exportService;
    
    @GetMapping("/tables")
    public String listTables(@PathVariable Long projectId, Model model) {
        model.addAttribute("project", tableService.findProjectById(projectId));
        model.addAttribute("tables", tableService.findByProjectId(projectId));
        return "index";
    }
    
    @GetMapping("/tables/new")
    public String newTable(@PathVariable Long projectId, Model model) {
        TableDefinitionDto tableDto = new TableDefinitionDto();
        tableDto.setProjectId(projectId);
        model.addAttribute("table", tableDto);
        model.addAttribute("project", tableService.findProjectById(projectId));
        return "table-form";
    }
    
    @GetMapping("/tables/{id}/edit")
    public String editTable(@PathVariable Long projectId, @PathVariable Long id, Model model) {
        model.addAttribute("table", tableService.findById(id));
        model.addAttribute("project", tableService.findProjectById(projectId));
        return "table-form";
    }
    
    @PostMapping("/tables")
    public String saveTable(@PathVariable Long projectId,
                          @Valid @ModelAttribute("table") TableDefinitionDto tableDto,
                          BindingResult result,
                          Model model,
                          RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("project", tableService.findProjectById(projectId));
            return "table-form";
        }
        
        tableDto.setProjectId(projectId);
        if (tableDto.getId() == null) {
            tableService.create(tableDto);
        } else {
            tableService.update(tableDto.getId(), tableDto);
        }
        
        redirectAttributes.addFlashAttribute("successMessage", "message.success.save");
        return "redirect:/projects/" + projectId + "/tables";
    }
    
    @PostMapping("/tables/{id}/delete")
    public String deleteTable(@PathVariable Long projectId, @PathVariable Long id, RedirectAttributes redirectAttributes) {
        tableService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "message.success.delete");
        return "redirect:/projects/" + projectId + "/tables";
    }
    
    @GetMapping("/export/excel")
    public ResponseEntity<byte[]> exportExcel(@PathVariable Long projectId,
                                            @RequestParam(required = false) Long tableId, 
                                            Locale locale) throws IOException {
        List<TableDefinitionDto> tables = getTablesForExport(tableId);
        byte[] excelContent = exportService.exportToExcel(tables, locale);
        
        String filename = "table_definitions_" + 
                         LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + 
                         ".xlsx";
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(excelContent);
    }
    
    @GetMapping("/export/markdown")
    public ResponseEntity<String> exportMarkdown(@PathVariable Long projectId,
                                                @RequestParam(required = false) Long tableId, 
                                                Locale locale) {
        List<TableDefinitionDto> tables = getTablesForExport(tableId);
        String markdownContent = exportService.exportToMarkdown(tables, locale);
        
        String filename = "table_definitions_" + 
                         LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + 
                         ".md";
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType("text/markdown; charset=UTF-8"))
                .body(markdownContent);
    }
    
    @GetMapping("/export/ddl")
    public ResponseEntity<String> exportDDL(@PathVariable Long projectId,
                                          @RequestParam(required = false) Long tableId, 
                                          Locale locale) {
        List<TableDefinitionDto> tables = getTablesForExport(tableId);
        String ddlContent = exportService.exportToDDL(tables, locale);
        
        String filename = "table_definitions_" + 
                         LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + 
                         ".sql";
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType("text/plain; charset=UTF-8"))
                .body(ddlContent);
    }
    
    private List<TableDefinitionDto> getTablesForExport(Long tableId) {
        if (tableId != null) {
            // 指定されたテーブルのみを取得
            TableDefinitionDto table = tableService.findById(tableId);
            return List.of(table);
        } else {
            // エクスポートにはテーブル選択が必須
            throw new RuntimeException("Table selection is required for export");
        }
    }
}
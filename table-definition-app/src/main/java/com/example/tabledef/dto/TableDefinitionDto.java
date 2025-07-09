package com.example.tabledef.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TableDefinitionDto {
    
    private Long id;
    
    private Long projectId;
    
    private String schemaName;
    
    @NotBlank(message = "Physical name is required")
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9_]*$", message = "Physical name must start with a letter and contain only letters, numbers, and underscores")
    private String physicalName;
    
    @NotBlank(message = "Logical name is required")
    private String logicalName;
    
    private String description;
    
    private List<ColumnDefinitionDto> columns;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
}
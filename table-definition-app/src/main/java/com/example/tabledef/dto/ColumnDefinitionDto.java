package com.example.tabledef.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Min;

@Data
public class ColumnDefinitionDto {
    
    private Long id;
    
    @NotBlank(message = "Physical name is required")
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9_]*$", message = "Physical name must start with a letter and contain only letters, numbers, and underscores")
    private String physicalName;
    
    @NotBlank(message = "Logical name is required")
    private String logicalName;
    
    @NotBlank(message = "Data type is required")
    private String dataType;
    
    @Min(value = 0, message = "Length must be positive")
    private Integer length;
    
    @Min(value = 0, message = "Precision must be positive")
    private Integer precision;
    
    @Min(value = 0, message = "Scale must be positive")
    private Integer scale;
    
    private boolean nullable = true;
    
    private boolean primaryKey = false;
    
    private boolean uniqueKey = false;
    
    private String defaultValue;
    
    private String description;
    
    @Min(value = 1, message = "Column order must be positive")
    private Integer columnOrder;
}
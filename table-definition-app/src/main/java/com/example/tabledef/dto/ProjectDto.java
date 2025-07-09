package com.example.tabledef.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProjectDto {
    
    private Long id;
    
    @NotBlank(message = "{project.code.required}")
    @Pattern(regexp = "^[A-Za-z0-9_-]+$", message = "{project.code.pattern}")
    @Size(max = 50, message = "{project.code.size}")
    private String code;
    
    @NotBlank(message = "{project.name.required}")
    @Size(max = 100, message = "{project.name.size}")
    private String name;
    
    @Size(max = 500, message = "{project.description.size}")
    private String description;
    
    private Integer tableCount;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
}
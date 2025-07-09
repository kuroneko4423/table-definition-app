package com.example.tabledef.service;

import com.example.tabledef.dto.TableDefinitionDto;
import com.example.tabledef.dto.ColumnDefinitionDto;
import com.example.tabledef.model.TableDefinition;
import com.example.tabledef.model.ColumnDefinition;
import com.example.tabledef.repository.TableDefinitionRepository;
import com.example.tabledef.repository.ProjectRepository;
import com.example.tabledef.model.Project;
import com.example.tabledef.dto.ProjectDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TableDefinitionService {
    
    private final TableDefinitionRepository repository;
    private final ProjectRepository projectRepository;
    
    public List<TableDefinitionDto> findByProjectId(Long projectId) {
        return repository.findByProjectIdWithColumns(projectId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    
    public ProjectDto findProjectById(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        ProjectDto dto = new ProjectDto();
        dto.setId(project.getId());
        dto.setCode(project.getCode());
        dto.setName(project.getName());
        dto.setDescription(project.getDescription());
        return dto;
    }
    
    public TableDefinitionDto findById(Long id) {
        return repository.findByIdWithColumns(id)
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("Table not found"));
    }
    
    public TableDefinitionDto create(TableDefinitionDto dto) {
        if (repository.existsByProjectIdAndPhysicalName(dto.getProjectId(), dto.getPhysicalName())) {
            throw new RuntimeException("Table with same physical name already exists in this project");
        }
        TableDefinition entity = toEntity(dto);
        Project project = projectRepository.findById(dto.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not found"));
        entity.setProject(project);
        return toDto(repository.save(entity));
    }
    
    public TableDefinitionDto update(Long id, TableDefinitionDto dto) {
        TableDefinition entity = repository.findByIdWithColumns(id)
                .orElseThrow(() -> new RuntimeException("Table not found"));
        
        if (!entity.getPhysicalName().equals(dto.getPhysicalName()) && 
            repository.existsByProjectIdAndPhysicalName(dto.getProjectId(), dto.getPhysicalName())) {
            throw new RuntimeException("Table with same physical name already exists in this project");
        }
        
        entity.setSchemaName(dto.getSchemaName());
        entity.setPhysicalName(dto.getPhysicalName());
        entity.setLogicalName(dto.getLogicalName());
        entity.setDescription(dto.getDescription());
        
        // Update columns
        entity.getColumns().clear();
        if (dto.getColumns() != null) {
            for (ColumnDefinitionDto columnDto : dto.getColumns()) {
                ColumnDefinition column = toColumnEntity(columnDto);
                entity.addColumn(column);
            }
        }
        
        return toDto(repository.save(entity));
    }
    
    public void delete(Long id) {
        repository.deleteById(id);
    }
    
    private TableDefinitionDto toDto(TableDefinition entity) {
        TableDefinitionDto dto = new TableDefinitionDto();
        dto.setId(entity.getId());
        dto.setProjectId(entity.getProject() != null ? entity.getProject().getId() : null);
        dto.setSchemaName(entity.getSchemaName());
        dto.setPhysicalName(entity.getPhysicalName());
        dto.setLogicalName(entity.getLogicalName());
        dto.setDescription(entity.getDescription());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        
        if (entity.getColumns() != null) {
            dto.setColumns(entity.getColumns().stream()
                    .map(this::toColumnDto)
                    .collect(Collectors.toList()));
        }
        
        return dto;
    }
    
    private TableDefinition toEntity(TableDefinitionDto dto) {
        TableDefinition entity = new TableDefinition();
        entity.setSchemaName(dto.getSchemaName());
        entity.setPhysicalName(dto.getPhysicalName());
        entity.setLogicalName(dto.getLogicalName());
        entity.setDescription(dto.getDescription());
        
        if (dto.getColumns() != null) {
            for (ColumnDefinitionDto columnDto : dto.getColumns()) {
                ColumnDefinition column = toColumnEntity(columnDto);
                entity.addColumn(column);
            }
        }
        
        return entity;
    }
    
    private ColumnDefinitionDto toColumnDto(ColumnDefinition entity) {
        ColumnDefinitionDto dto = new ColumnDefinitionDto();
        dto.setId(entity.getId());
        dto.setPhysicalName(entity.getPhysicalName());
        dto.setLogicalName(entity.getLogicalName());
        dto.setDataType(entity.getDataType());
        dto.setLength(entity.getLength());
        dto.setPrecision(entity.getPrecision());
        dto.setScale(entity.getScale());
        dto.setNullable(entity.isNullable());
        dto.setPrimaryKey(entity.isPrimaryKey());
        dto.setUniqueKey(entity.isUniqueKey());
        dto.setDefaultValue(entity.getDefaultValue());
        dto.setDescription(entity.getDescription());
        dto.setColumnOrder(entity.getColumnOrder());
        return dto;
    }
    
    private ColumnDefinition toColumnEntity(ColumnDefinitionDto dto) {
        ColumnDefinition entity = new ColumnDefinition();
        entity.setPhysicalName(dto.getPhysicalName());
        entity.setLogicalName(dto.getLogicalName());
        entity.setDataType(dto.getDataType());
        entity.setLength(dto.getLength());
        entity.setPrecision(dto.getPrecision());
        entity.setScale(dto.getScale());
        entity.setNullable(dto.isNullable());
        entity.setPrimaryKey(dto.isPrimaryKey());
        entity.setUniqueKey(dto.isUniqueKey());
        entity.setDefaultValue(dto.getDefaultValue());
        entity.setDescription(dto.getDescription());
        entity.setColumnOrder(dto.getColumnOrder());
        return entity;
    }
}
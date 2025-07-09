package com.example.tabledef.service;

import com.example.tabledef.dto.ProjectDto;
import com.example.tabledef.model.Project;
import com.example.tabledef.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectService {
    
    private final ProjectRepository projectRepository;
    
    public List<ProjectDto> findAll() {
        return projectRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public ProjectDto findById(Long id) {
        return projectRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new RuntimeException("Project not found"));
    }
    
    public ProjectDto create(ProjectDto dto) {
        if (projectRepository.existsByCode(dto.getCode())) {
            throw new RuntimeException("Project code already exists");
        }
        
        Project project = new Project();
        project.setCode(dto.getCode());
        project.setName(dto.getName());
        project.setDescription(dto.getDescription());
        
        Project saved = projectRepository.save(project);
        return convertToDto(saved);
    }
    
    public ProjectDto update(Long id, ProjectDto dto) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        
        if (!project.getCode().equals(dto.getCode()) && projectRepository.existsByCode(dto.getCode())) {
            throw new RuntimeException("Project code already exists");
        }
        
        project.setCode(dto.getCode());
        project.setName(dto.getName());
        project.setDescription(dto.getDescription());
        
        Project updated = projectRepository.save(project);
        return convertToDto(updated);
    }
    
    public void delete(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new RuntimeException("Project not found");
        }
        projectRepository.deleteById(id);
    }
    
    private ProjectDto convertToDto(Project project) {
        ProjectDto dto = new ProjectDto();
        dto.setId(project.getId());
        dto.setCode(project.getCode());
        dto.setName(project.getName());
        dto.setDescription(project.getDescription());
        dto.setTableCount(project.getTables() != null ? project.getTables().size() : 0);
        dto.setCreatedAt(project.getCreatedAt());
        dto.setUpdatedAt(project.getUpdatedAt());
        return dto;
    }
}
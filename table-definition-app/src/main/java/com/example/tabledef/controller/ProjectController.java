package com.example.tabledef.controller;

import com.example.tabledef.dto.ProjectDto;
import com.example.tabledef.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class ProjectController {
    
    private final ProjectService projectService;
    
    @GetMapping
    public String index() {
        return "redirect:/projects";
    }
    
    @GetMapping("/projects")
    public String listProjects(Model model) {
        model.addAttribute("projects", projectService.findAll());
        return "project-list";
    }
    
    @GetMapping("/projects/new")
    public String newProject(Model model) {
        model.addAttribute("project", new ProjectDto());
        return "project-form";
    }
    
    @PostMapping("/projects")
    public String createProject(@Valid @ModelAttribute("project") ProjectDto project, 
                               BindingResult result, 
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "project-form";
        }
        
        try {
            projectService.create(project);
            redirectAttributes.addFlashAttribute("successMessage", "project.created");
            return "redirect:/projects";
        } catch (Exception e) {
            result.rejectValue("code", "error.project", e.getMessage());
            return "project-form";
        }
    }
    
    @GetMapping("/projects/{id}/edit")
    public String editProject(@PathVariable Long id, Model model) {
        model.addAttribute("project", projectService.findById(id));
        return "project-form";
    }
    
    @PostMapping("/projects/{id}")
    public String updateProject(@PathVariable Long id, 
                               @Valid @ModelAttribute("project") ProjectDto project, 
                               BindingResult result,
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "project-form";
        }
        
        try {
            projectService.update(id, project);
            redirectAttributes.addFlashAttribute("successMessage", "project.updated");
            return "redirect:/projects";
        } catch (Exception e) {
            result.rejectValue("code", "error.project", e.getMessage());
            return "project-form";
        }
    }
    
    @PostMapping("/projects/{id}/delete")
    public String deleteProject(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            projectService.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", "project.deleted");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "project.delete.error");
        }
        return "redirect:/projects";
    }
}
package com.pfa.controller;

import com.pfa.models.Project;
import com.pfa.models.Status;
import com.pfa.models.Client;
import com.pfa.models.Service;
import com.pfa.services.ClientService;
import com.pfa.services.ServiceService;
import com.pfa.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ClientService clientService;

    @Autowired
    private ServiceService serviceService;

    @GetMapping
    public String listProjects(Model model) {
        List<Project> projects = projectRepository.findAll();
        model.addAttribute("projects", projects);
        return "projects/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("project", new Project());
        model.addAttribute("clients", clientService.getAllClients());
        model.addAttribute("services", serviceService.getAllServices());
        model.addAttribute("statuses", Status.values());
        return "projects/form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Project> projectOpt = projectRepository.findById(id);
        if (projectOpt.isPresent()) {
            Project project = projectOpt.get();
            model.addAttribute("project", project);
            model.addAttribute("clients", clientService.getAllClients());
            model.addAttribute("services", serviceService.getAllServices());
            model.addAttribute("statuses", Status.values());
            return "projects/form";
        } else {
            return "redirect:/projects";
        }
    }

    @PostMapping("/save")
    public String saveProject(@ModelAttribute Project project) {
        // Set default status only if it's a new project and status is not provided
        if (project.getId() == null) {
            if (project.getStatus() == null) {
                project.setStatus(Status.PENDING);
            }
        }
        projectRepository.save(project);
        return "redirect:/projects";
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        if (projectRepository.existsById(id)) {
            projectRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

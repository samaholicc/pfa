package com.pfa.controller;

import com.pfa.models.Project;
import com.pfa.models.ProjectTechnology;
import com.pfa.models.Technology;
import com.pfa.repository.ProjectRepository;
import com.pfa.repository.ProjectTechnologyRepository;
import com.pfa.repository.TechnologyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/project-technologies")
public class ProjectTechnologyController {

    @Autowired
    private ProjectTechnologyRepository projectTechnologyRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TechnologyRepository technologyRepository;

    @GetMapping
    public String listProjectTechnologies(Model model) {
        List<ProjectTechnology> projectTechnologies = projectTechnologyRepository.findAll();
        model.addAttribute("projectTechnologies", projectTechnologies);
        return "project-technologies/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("projectTechnology", new ProjectTechnology());
        model.addAttribute("projects", projectRepository.findAll());
        model.addAttribute("technologies", technologyRepository.findAll());
        return "project-technologies/form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<ProjectTechnology> projectTechnologyOpt = projectTechnologyRepository.findById(id);
        if (projectTechnologyOpt.isPresent()) {
            model.addAttribute("projectTechnology", projectTechnologyOpt.get());
            model.addAttribute("projects", projectRepository.findAll());
            model.addAttribute("technologies", technologyRepository.findAll());
            return "project-technologies/form";
        } else {
            return "redirect:/project-technologies";
        }
    }

    @PostMapping("/save")
    public String saveProjectTechnology(@ModelAttribute ProjectTechnology projectTechnology,
                                        @RequestParam("projectId") Long projectId,
                                        @RequestParam("technologyId") Long technologyId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid project ID"));
        Technology technology = technologyRepository.findById(technologyId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid technology ID"));

        // Check if it's an existing entity
        if (projectTechnology.getId() != null) {
            Optional<ProjectTechnology> existingProjectTechnology = projectTechnologyRepository.findById(projectTechnology.getId());
            if (existingProjectTechnology.isPresent()) {
                ProjectTechnology pt = existingProjectTechnology.get();
                pt.setProject(project);
                pt.setTechnology(technology);
                projectTechnologyRepository.save(pt);
                return "redirect:/project-technologies"; // Redirect after successful update
            }
        }

        // If it's a new entity or the ID doesn't exist in the database
        projectTechnology.setProject(project);
        projectTechnology.setTechnology(technology);
        projectTechnologyRepository.save(projectTechnology);

        return "redirect:/project-technologies"; // Redirect after successful save
    }



    @GetMapping("/delete/{id}")
    public String deleteProjectTechnology(@PathVariable Long id) {
        if (projectTechnologyRepository.existsById(id)) {
            projectTechnologyRepository.deleteById(id);
        }
        return "redirect:/project-technologies";
    }
}

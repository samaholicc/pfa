package com.pfa.controller;

import com.pfa.models.Technology;
import com.pfa.repository.TechnologyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/technologies")
public class TechnologyController {

    @Autowired
    private TechnologyRepository technologyRepository;

    @GetMapping
    public String listTechnologies(Model model) {
        List<Technology> technologies = technologyRepository.findAll();
        model.addAttribute("technologies", technologies);
        return "technologies/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("technology", new Technology());
        return "technologies/form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Technology> technologyOpt = technologyRepository.findById(id);
        if (technologyOpt.isPresent()) {
            model.addAttribute("technology", technologyOpt.get());
            return "technologies/form";
        } else {
            return "redirect:/technologies";
        }
    }

    @PostMapping("/save")
    public String saveTechnology(@ModelAttribute Technology technology) {
        if (technology.getId() != null) {
            Optional<Technology> existingTechnologyOpt = technologyRepository.findById(technology.getId());
            if (existingTechnologyOpt.isPresent()) {
                Technology existingTechnology = existingTechnologyOpt.get();
                existingTechnology.setName(technology.getName());
                existingTechnology.setDescription(technology.getDescription());
                technologyRepository.save(existingTechnology);
            } else {
                return "redirect:/technologies";
            }
        } else {
            technologyRepository.save(technology);
        }
        return "redirect:/technologies";
    }

    @GetMapping("/delete/{id}")
    public String deleteTechnology(@PathVariable Long id) {
        if (technologyRepository.existsById(id)) {
            technologyRepository.deleteById(id);
        }
        return "redirect:/technologies";
    }
}

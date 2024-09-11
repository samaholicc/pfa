package com.pfa.controller;

import com.pfa.models.Demand;
import com.pfa.models.Service;
import com.pfa.models.Status;
import com.pfa.services.ServiceService;
import com.pfa.repository.DemandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/demands")
public class DemandController {

    @Autowired
    private DemandRepository demandRepository;

    @Autowired
    private ServiceService serviceService;

    @GetMapping
    public String listDemands(Model model) {
        List<Demand> demands = demandRepository.findAll();
        model.addAttribute("demands", demands);
        return "demands/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("demand", new Demand());
        model.addAttribute("services", serviceService.getAllServices());
        model.addAttribute("statuses", Status.values());
        return "demands/form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Demand> demandOpt = demandRepository.findById(id);
        if (demandOpt.isPresent()) {
            Demand demand = demandOpt.get();
            model.addAttribute("demand", demand);
            model.addAttribute("services", serviceService.getAllServices());
            model.addAttribute("statuses", Status.values());
            return "demands/form";
        } else {
            return "redirect:/demands";
        }
    }

    @PostMapping("/save")
    public String saveDemand(@ModelAttribute Demand demand) {
        // Set default status only if it's a new demand and status is not provided
        if (demand.getId() == null) {
            if (demand.getStatus() == null) {
                demand.setStatus(Status.PENDING);
            }
        }
        demandRepository.save(demand);
        return "redirect:/demands";
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDemand(@PathVariable Long id) {
        if (demandRepository.existsById(id)) {
            demandRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

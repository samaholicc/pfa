package com.pfa.controller;

import com.pfa.models.Service;
import com.pfa.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/services")
public class ServiceController {

    @Autowired
    private ServiceRepository serviceRepository;

    @GetMapping
    public String listServices(Model model) {
        List<Service> services = serviceRepository.findAll();
        model.addAttribute("services", services);
        return "services/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("service", new Service());
        return "services/form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Service> serviceOpt = serviceRepository.findById(id);
        if (serviceOpt.isPresent()) {
            model.addAttribute("service", serviceOpt.get());
            return "services/form";
        } else {
            return "redirect:/services";
        }
    }

    @PostMapping("/save")
    public String saveService(@ModelAttribute Service service) {
        serviceRepository.save(service);
        return "redirect:/services";
    }

    @GetMapping("/delete/{id}")
    public String deleteService(@PathVariable Long id) {
        if (serviceRepository.existsById(id)) {
            serviceRepository.deleteById(id);
        }
        return "redirect:/services";
    }

    // New endpoint for AJAX
    @GetMapping("/api/{id}")
    @ResponseBody
    public Service getServiceById(@PathVariable Long id) {
        return serviceRepository.findById(id).orElse(null);
    }
}

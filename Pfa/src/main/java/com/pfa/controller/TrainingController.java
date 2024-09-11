package com.pfa.controller;

import com.pfa.models.Training;
import com.pfa.repository.TrainingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/trainings")
public class TrainingController {

    @Autowired
    private TrainingRepository trainingRepository;

    @GetMapping
    public String listTrainings(Model model) {
        List<Training> trainings = trainingRepository.findAll();
        model.addAttribute("trainings", trainings);
        return "trainings/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("training", new Training());
        return "trainings/form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Training> trainingOpt = trainingRepository.findById(id);
        if (trainingOpt.isPresent()) {
            model.addAttribute("training", trainingOpt.get());
            return "trainings/form";
        } else {
            return "redirect:/trainings";
        }
    }

    @PostMapping("/save")
    public String saveTraining(@ModelAttribute Training training) {
        if (training.getId() != null) {
            Optional<Training> existingTrainingOpt = trainingRepository.findById(training.getId());
            if (existingTrainingOpt.isPresent()) {
                Training existingTraining = existingTrainingOpt.get();
                // Update the existing training
                existingTraining.setName(training.getName());
                existingTraining.setDescription(training.getDescription());
                existingTraining.setStartDate(training.getStartDate());
                existingTraining.setEndDate(training.getEndDate());
                existingTraining.setPrice(training.getPrice());
                trainingRepository.save(existingTraining);
            } else {
                // Handle the case where the training ID doesn't exist in the database
                return "redirect:/trainings"; // Or show an error message
            }
        } else {
            // Create a new training
            trainingRepository.save(training);
        }
        return "redirect:/trainings";
    }


    @GetMapping("/delete/{id}")
    public String deleteTraining(@PathVariable Long id) {
        if (trainingRepository.existsById(id)) {
            trainingRepository.deleteById(id);
        }
        return "redirect:/trainings";
    }
}

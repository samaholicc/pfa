package com.pfa.controller;

import com.pfa.models.Notification;
import com.pfa.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private NotificationRepository notificationRepository;

    @GetMapping
    public String listNotifications(Model model) {
        List<Notification> notifications = notificationRepository.findAll();
        model.addAttribute("notifications", notifications);
        return "notifications/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("notification", new Notification());
        return "notifications/form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Notification> notificationOpt = notificationRepository.findById(id);
        if (notificationOpt.isPresent()) {
            Notification notification = notificationOpt.get();
            model.addAttribute("notification", notification);
            return "notifications/form";
        } else {
            return "redirect:/notifications";
        }
    }

    @PostMapping("/save")
    public String saveNotification(@ModelAttribute Notification notification) {

        notificationRepository.save(notification);
        return "redirect:/notifications";
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        if (notificationRepository.existsById(id)) {
            notificationRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/view/{id}")
    public String viewNotification(@PathVariable Long id, Model model) {
        Optional<Notification> notificationOpt = notificationRepository.findById(id);
        if (notificationOpt.isPresent()) {
            Notification notification = notificationOpt.get();
            model.addAttribute("notification", notification);
            return "notifications/view";
        } else {
            return "redirect:/notifications";
        }
    }
}

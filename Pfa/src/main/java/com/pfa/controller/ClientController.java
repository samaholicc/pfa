package com.pfa.controller;

import com.pfa.models.Client;
import com.pfa.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    @GetMapping
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable Long id) {
        Optional<Client> client = clientRepository.findById(id);
        return client.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Client createClient(@RequestBody Client client) {
        return clientRepository.save(client);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Client> updateClient(@PathVariable Long id, @RequestBody Client updatedClient) {
        return clientRepository.findById(id)
                .map(client -> {
                    client.setName(updatedClient.getName());
                    client.setContactPerson(updatedClient.getContactPerson());
                    client.setEmail(updatedClient.getEmail());
                    client.setPhone(updatedClient.getPhone());
                    client.setAddress(updatedClient.getAddress());
                    Client savedClient = clientRepository.save(client);
                    return ResponseEntity.ok(savedClient);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        if (clientRepository.existsById(id)) {
            clientRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Additional methods for Thymeleaf views
    @Controller
    @RequestMapping("/clients")
    public static class ClientViewController {

        @Autowired
        private ClientRepository clientRepository;

        @GetMapping("/view")
        public String listClients(Model model) {
            List<Client> clients = clientRepository.findAll();
            model.addAttribute("clients", clients);
            return "list";
        }

        @GetMapping("/new")
        public String showCreateForm(Model model) {
            model.addAttribute("client", new Client());
            return "form";
        }

        @PostMapping("/save")
        public String saveClient(@ModelAttribute Client client) {
            clientRepository.save(client);
            return "redirect:/clients/view";
        }

        @GetMapping("/edit/{id}")
        public String showEditForm(@PathVariable Long id, Model model) {
            Optional<Client> client = clientRepository.findById(id);
            if (client.isPresent()) {
                model.addAttribute("client", client.get());
                return "form";
            }
            return "redirect:/clients/view";
        }

        @PostMapping("/update/{id}")
        public String updateClient(@PathVariable Long id, @ModelAttribute Client updatedClient) {
            if (clientRepository.existsById(id)) {
                updatedClient.setId(id);
                clientRepository.save(updatedClient);
            }
            return "redirect:/clients/view";
        }

        @GetMapping("/delete/{id}")
        public String deleteClient(@PathVariable Long id) {
            if (clientRepository.existsById(id)) {
                clientRepository.deleteById(id);
            }
            return "redirect:/clients/view";
        }
    }
}

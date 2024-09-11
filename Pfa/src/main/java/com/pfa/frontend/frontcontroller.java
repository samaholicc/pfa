package com.pfa.frontend;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/geomadev")
public class frontcontroller {

    @GetMapping("/home")
    public String showHomePage(Model model) {
        return "index";
    }
    @GetMapping("/about")
    public String showAboutPage(Model model) {
        return "about";
    }
    @GetMapping("/contact")
    public String showContactPage(Model model) {
        return "contact";
    }
    @GetMapping("/services")
    public String showServicesPage(Model model) {
        return "services";
    }
}

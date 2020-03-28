package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.enums.Role;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
public class SecurityController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String login(
            @RequestParam(required = false, defaultValue = "") String error,
            Principal principal,
            Model model
    ) {
        if (principal != null) {
            return "redirect:/";
        }

        model.addAttribute("error", error.equals("1"));

        return "security/login";
    }

    @GetMapping("/registration")
    public String registration(Principal principal, Model model) {
        if (principal != null) {
            return "redirect:/";
        }

        model.addAttribute("user", new User());

        return "security/registration";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user, Principal principal) {
        if (principal != null) {
            return "redirect:/";
        }

        User same = userRepository.findByUsername(user.getUsername());
        if (same != null) {
            return "redirect:/registration";
        }

        user.getRoles().add(Role.ROLE_USER);
        user.setPassword(
                passwordEncoder.encode(user.getPassword())
        );

        userRepository.save(user);
        userRepository.flush();

        return "redirect:/login";
    }
}

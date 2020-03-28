package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.enums.Role;
import com.example.demo.exception.HttpAccessDeniedException;
import com.example.demo.exception.HttpNotFoundException;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.stream.IntStream;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String index() {
        return "admin/index";
    }

    @GetMapping("/user/list")
    public String userList(
            @PageableDefault(value = 100, sort = "username") Pageable pageable,
            Model model
    ) {
        Page<User> users = userRepository.findAll(pageable);

        model.addAttribute("users", users.getContent());
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("pages", users.getTotalPages());
        model.addAttribute("pagesRange", IntStream.range(0, users.getTotalPages()).toArray());

        return "admin/user/list";
    }

    @GetMapping("/user/edit/{id}")
    public String userEdit(
            @PathVariable(name = "id") User user,
            Model model
    ) {
        if (user.getUsername().equals("admin")) {
            throw new HttpAccessDeniedException();
        }

        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());

        return "admin/user/edit";
    }

    @PostMapping("/user/save")
    public String userSave(@ModelAttribute User user) {
        User original = userRepository.findByUsername(user.getUsername());
        if (original == null) {
            throw new HttpNotFoundException();
        }

        if (user.getUsername().equals("admin")) {
            throw new HttpAccessDeniedException();
        }

        original.setName(user.getName());
        original.setEmail(user.getEmail());
        original.setActive(user.getActive());
        original.getRoles().clear();
        for (Role role : user.getRoles()) {
            original.getRoles().add(role);
        }

        userRepository.save(original);
        userRepository.flush();

        return "redirect:/admin/user/edit/" + original.getId();
    }
}

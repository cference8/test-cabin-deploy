package com.cf.cabinescaperoom.controllers;

import com.cf.cabinescaperoom.models.Role;
import com.cf.cabinescaperoom.models.User;
import com.cf.cabinescaperoom.service.RoleService;
import com.cf.cabinescaperoom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import java.util.HashSet;
import java.util.Set;

@Controller
public class LoginController {

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @Autowired
    BCryptPasswordEncoder encoder;

    Set<ConstraintViolation<User>> violations = new HashSet<>();

    @GetMapping("/login")
    public String showLoginForm() {
        if (isAuthenticated()) {
            return "redirect:home";
        }
        return "login";
    }

    private boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || AnonymousAuthenticationToken.class.
                isAssignableFrom(authentication.getClass())) {
            return false;
        }
        return authentication.isAuthenticated();
    }

    @GetMapping("/account")
    public String displayContentPage(HttpServletRequest request, Model model) {
        // need to grab the user id from the parameter
        int userId = Integer.parseInt(request.getParameter("id"));
        User currentUser = userService.getUserById(userId);

        model.addAttribute("user", currentUser);

        return "account";
    }

    @GetMapping("/createAccount")
    public String createAccount(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("errors", violations);
        return "createAccount";
    }

    @PostMapping("/createAccount")
    public String createAccount(User user, BindingResult result, Model model) {
        if (result.hasErrors()) {

            model.addAttribute("user", user);
            model.addAttribute("errors", result.getAllErrors());
            return "createAccount";
        } else {
            try {
                Set<Role> userRoles = new HashSet<>();
                userRoles.add(roleService.getRoleByRole("ROLE_USER"));
                user.setRoles(userRoles);

                if (!userService.existsByUsername(user.getUsername())) {
                    if(user.getPassword() != null && !user.getPassword().equals("") && user.getPassword().length() >= 6){
                        user.setPassword(encoder.encode(user.getPassword()));
                        user.setEnabled(true);
                        userService.createUser(user);
                    } else {
                        FieldError error = new FieldError("user", "password", "Password must contain at least 6 characters");
                        result.addError(error);
                        model.addAttribute("errors", result.getAllErrors());
                        return "createAccount";
                    }
                } else {
                    FieldError error = new FieldError("user", "username", "Username already exists. Please choose a unique username.");
                    result.addError(error);
                    model.addAttribute("errors", result.getAllErrors());
                    return "createAccount";
                }
                return "redirect:/home";
            } catch (Exception ex) {
                FieldError error = new FieldError("user", "username", "Username already exists. Please choose a unique username.");
                result.addError(error);
                model.addAttribute("errors", result.getAllErrors());
                return "createAccount";
            }
        }
    }
}

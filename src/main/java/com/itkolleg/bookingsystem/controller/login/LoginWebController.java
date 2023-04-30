package com.itkolleg.bookingsystem.controller.login;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
public class LoginWebController {


    private final AuthenticationManager authenticationManager;

    public LoginWebController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }


    @GetMapping("/web/login")
    public String showLoginForm() {
        return "login/login";
    }


    @GetMapping("/web/hello")
    public String hello(Model model, Principal principal) {
        String username = principal.getName();
        model.addAttribute("username", username);
        return "/login/hello";
    }

    @PostMapping("/web/login")
    public String processLoginForm(HttpServletRequest request, HttpServletResponse response) {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);

        try {
            Authentication authentication = authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return "redirect:/web/hello";
        } catch (AuthenticationException e) {
            return "redirect:/login-error";
        }
    }

    @GetMapping("/login-error")
    public String loginError() {
        return "login/login-error";
    }
}

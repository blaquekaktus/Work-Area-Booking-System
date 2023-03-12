package com.itkolleg.bookingsystem.controller.login;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginWebController {

    /**
     * Zeigt die Login-Seite an.
     *
     * @return Name der Login-Seitenvorlage
     */
    @GetMapping("/web/login")
    public String showLoginForm() {
        return "login/login";
    }

    /**
     * Zeigt die Login-Seite mit einer Fehlermeldung an, wenn die Authentifizierung fehlschlägt.
     *
     * @param model Model zur Verwendung in der Vorlage
     * @return Name der Login-Seitenvorlage
     */
    @GetMapping("/login-error")
    public String showLoginError(Model model) {
        model.addAttribute("loginError", true);
        return "login/login";
    }
}

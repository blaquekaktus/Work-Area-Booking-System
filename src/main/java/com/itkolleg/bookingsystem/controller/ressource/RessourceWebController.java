package com.itkolleg.bookingsystem.controller.ressource;


import com.itkolleg.bookingsystem.service.Ressource.RessourceService;
import com.itkolleg.bookingsystem.domains.Ressource;
import com.itkolleg.bookingsystem.exceptions.EmployeeExceptions.EmployeeAlreadyExistsException;
import com.itkolleg.bookingsystem.exceptions.RessourceExceptions.RessourceDeletionNotPossibleException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Controller
public class RessourceWebController {

    RessourceService ressourceService;

    public RessourceWebController(RessourceService ressourceService) {
        this.ressourceService = ressourceService;
    }

    @GetMapping("/web/allressources")
    public ModelAndView allressources() throws ExecutionException, InterruptedException {
        List<Ressource> allRessources = ressourceService.getAllRessource();
        return new ModelAndView("ressource/allressources", "ressources", allRessources);
    }

    @GetMapping("/web/insertressourceform")
    public ModelAndView insertressourceform() {
        return new ModelAndView("ressource/insertressourceform", "myressource", new Ressource());
    }

    @GetMapping("/web/deleteressource/{id}")
    public String deleteRessourceWithId(@PathVariable Long id, Model model) {
        try {
            this.ressourceService.deleteRessourceById(id);
            return "redirect:/web/allressources";
        } catch (RessourceDeletionNotPossibleException e) {
            model.addAttribute("errortitle", "Ressource-Löschen schlägt fehl!");
            model.addAttribute("errormessage", e.getMessage());
            return "errorPage";
        }
    }

    @PostMapping("/web/insertressource")
    public String insertRoom(@Valid @ModelAttribute("myroom") Ressource ressource, BindingResult bindingResult) throws EmployeeAlreadyExistsException, ExecutionException, InterruptedException {
        if (bindingResult.hasErrors()) {
            return "ressource/insertressourceform";
        } else {

            this.ressourceService.addRessource(ressource);
            return "redirect:/web/allressources";
        }
    }
}

package com.itkolleg.bookingsystem.controller.ressource;

import com.itkolleg.bookingsystem.domains.Ressourcetype;
import com.itkolleg.bookingsystem.exceptions.ResourceDeletionFailureException;
import com.itkolleg.bookingsystem.exceptions.RessourceExceptions.RessourceAlreadyExistsException;
import com.itkolleg.bookingsystem.domains.Ressource;
import com.itkolleg.bookingsystem.exceptions.RessourceExceptions.RessourceDeletionNotPossibleException;
import com.itkolleg.bookingsystem.service.Ressource.RessourceService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Controller
@RequestMapping("/web/ressource")
public class RessourceWebController {

    RessourceService ressourceService;

    public RessourceWebController(RessourceService ressourceService) {
        this.ressourceService = ressourceService;
    }


    /**
     * Dient dazu eine Übersicht aller Ressourcen für den/die Admin zu liefern.
     * @return Model of Ressources
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @GetMapping("/allRessources")
    public ModelAndView allressources() throws ExecutionException, InterruptedException {
        List<Ressource> allRessources = ressourceService.getAllRessource();
        return new ModelAndView("ressource/allressources", "ressources", allRessources);
    }

    /**
     * Dient dazu eine Übersicht aller Ressourcen für den/die Mitarbeiter:inn zu liefern
     * @return Model of Ressources
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @GetMapping("/allRessourcesEmployee")
    public ModelAndView allressourcesEmployee() throws ExecutionException, InterruptedException {
        List<Ressource> allRessourcesEmployee = ressourceService.getAllRessource();
        return new ModelAndView("ressource/allressourcesEmployee", "ressourcesEmployee", allRessourcesEmployee);
    }


    @GetMapping("/addRessource")
    public ModelAndView addRessource( Model model) {

        model.addAttribute("newRessource", new Ressource());
        return new ModelAndView("ressource/addRessource", "newRessource", model);
    }


    @PostMapping("/addRessource")
    public String addRessource(@Valid Ressource ressource, BindingResult bindingResult) throws RessourceAlreadyExistsException, ExecutionException, InterruptedException {
        if (bindingResult.hasErrors()) {
            return "/ressource/addRessource";
        } else {
            this.ressourceService.addRessource(ressource);
            return "redirect:/web/ressource/allRessources";
        }
    }
/*
    @PostMapping("/deleteRessource/{id}")
    public String deleteRessource(@Valid Long id, BindingResult bindingResult) throws RessourceDeletionNotPossibleException {
        this.ressourceService.deleteRessourceById(id);
        return "redirect:/web/ressource/allRessources";
    }
*/
    @GetMapping("/delete/{id}")
    public String deleteRessource(@PathVariable Long id) {
        try {
            this.ressourceService.deleteRessourceById(id);
            return "redirect:/web/ressource/allRessources";
        } catch (RessourceDeletionNotPossibleException e) {
            return "redirect:/web/ressource/allRessources";
        }
    }

}

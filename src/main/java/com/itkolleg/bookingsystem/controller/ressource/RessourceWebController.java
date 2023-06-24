package com.itkolleg.bookingsystem.controller.ressource;

import com.itkolleg.bookingsystem.domains.Ressourcetype;
import com.itkolleg.bookingsystem.exceptions.ResourceDeletionFailureException;
import com.itkolleg.bookingsystem.exceptions.RessourceExceptions.RessourceAlreadyExistsException;
import com.itkolleg.bookingsystem.domains.Ressource;
import com.itkolleg.bookingsystem.exceptions.RessourceExceptions.RessourceDeletionNotPossibleException;
import com.itkolleg.bookingsystem.exceptions.RessourceExceptions.RessourceNotFoundException;
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

    @GetMapping("/allRessourcesEmployee")
    public ModelAndView allRessourcesEmployee() throws ExecutionException, InterruptedException {
        List<Ressource> allRessources = ressourceService.getAllRessource();
        return new ModelAndView("ressource/allressourcesEmployee", "ressourcesEmployee", allRessources);
    }

    @GetMapping("/addRessource")
    public ModelAndView addRessource( Model model) {

        model.addAttribute("newRessource", new Ressource());
        return new ModelAndView("ressource/addRessource", "Ressource", model);
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

    @GetMapping("/updateRessource/{id}")
    public ModelAndView updateRessource(@PathVariable Long id, Model model) throws RessourceNotFoundException, ExecutionException, InterruptedException {

        Ressource ressource = this.ressourceService.getRessourceById(id);
        model.addAttribute("updateRessource", ressource);
        return new ModelAndView("ressource/editRessource", "Ressource", model);
    }

    @PostMapping("/updateRessource")
    public String updateRessource(@Valid Ressource ressource, BindingResult bindingResult) throws RessourceAlreadyExistsException, ExecutionException, InterruptedException, RessourceNotFoundException {
        if (bindingResult.hasErrors()) {
            return "/ressource/editRessource";
        } else {
            this.ressourceService.updateRessource(ressource);
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
    @GetMapping("/deleteRessource/{id}")
    public String deleteRessource(@PathVariable Long id) {
        try {
            this.ressourceService.deleteRessourceById(id);
            return "redirect:/web/ressource/allRessources";
        } catch (RessourceDeletionNotPossibleException e) {
            return "redirect:/web/ressource/allRessources";
        }
    }

}

package com.itkolleg.bookingsystem.controller.ressource;


import com.itkolleg.bookingsystem.domains.Ressourcetype;
import com.itkolleg.bookingsystem.exceptions.RessourceExceptions.RessourceAlreadyExistsException;
import com.itkolleg.bookingsystem.service.Ressource.RessourceService;
import com.itkolleg.bookingsystem.domains.Ressource;
import com.itkolleg.bookingsystem.exceptions.EmployeeExceptions.EmployeeAlreadyExistsException;
import com.itkolleg.bookingsystem.exceptions.RessourceExceptions.RessourceDeletionNotPossibleException;
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

    @GetMapping("/web/allRessources")
    public ModelAndView allressources() throws ExecutionException, InterruptedException {
        List<Ressource> allRessources = ressourceService.getAllRessource();
        return new ModelAndView("ressource/allressources", "ressources", allRessources);
    }



    @GetMapping("/web/addRessource")
    public String addRessource(Model model) {
        model.addAttribute("newRessource", new Ressource());
        List<String> ressourceTypes = new ArrayList<>();
        ressourceTypes.add(String.valueOf(Ressourcetype.BICYCLE));
        ressourceTypes.add(String.valueOf(Ressourcetype.TRIPOD));
        ressourceTypes.add(String.valueOf(Ressourcetype.BEAMER));
        ressourceTypes.add(String.valueOf(Ressourcetype.CAMERA));
        ressourceTypes.add(String.valueOf(Ressourcetype.WHITEBOARD));
        model.addAttribute("ressourcetype", ressourceTypes);
        return "ressource/addRessource";
    }

    @PostMapping("/web/addRessource")
    public String addRessource(@Valid Ressource ressource, BindingResult bindingResult) throws RessourceAlreadyExistsException, ExecutionException, InterruptedException {
        if (bindingResult.hasErrors()) {
            return "ressource/addRessource";
        } else {
            this.ressourceService.addRessource(ressource);
            return "redirect:/web/allRessources";
        }
    }

    @PostMapping("/web/allresources/{id}")
    public String deleteRessourceWithId(@PathVariable Long id, Model model)
    {
      try
      {
          this.ressourceService.deleteRessourceById(id);
          return "redirect:/web/allressources";
      } catch (RessourceDeletionNotPossibleException e)
        {
            model.addAttribute("errortitle", "Ressource-Löschen fehlgeschlagen!");
            model.addAttribute("errormessage", e.getMessage());
            return "errorPage";
        }

    }


    /*@GetMapping("/web/manageRessource/{id}")
    public String deleteRessourceWithId(@PathVariable Long id, Model model) {
        try {
            this.ressourceService.deleteRessourceById(id);
            return "redirect:/web/allressources";
        } catch (RessourceDeletionNotPossibleException e) {
            model.addAttribute("errortitle", "Ressource-Löschen fehlgeschlagen!");
            model.addAttribute("errormessage", e.getMessage());
            return "errorPage";
        }
    }*/

}

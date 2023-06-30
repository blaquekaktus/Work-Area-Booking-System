package com.itkolleg.bookingsystem.controller;

import com.itkolleg.bookingsystem.domains.Desk;
import com.itkolleg.bookingsystem.exceptions.ResourceDeletionFailureException;
import com.itkolleg.bookingsystem.exceptions.ResourceNotFoundException;
import com.itkolleg.bookingsystem.service.Desk.DeskService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/web/desks")
public class DeskWebController {

    private static final Logger logger = LoggerFactory.getLogger(DeskWebController.class);

    private final DeskService deskService;

    /**
     * Constructs a new DeskWebController with the specified DeskService.
     *
     * @param deskService the DeskService to be used
     */
    public DeskWebController(DeskService deskService) {
        this.deskService = deskService;
    }

    /**
     * Retrieves all desks and adds them to the model.
     *
     * @param model the model to be used
     * @return the view name for displaying all desks
     */
    @GetMapping
    public String getAllDesks(Model model) {
        model.addAttribute("viewAllDesks", this.deskService.getAllDesks());
        return "Desks/allDesks";
    }

    /**
     * Displays the form for adding a new desk.
     *
     * @param model the model to be used
     * @return the view name for adding a desk
     */
    @GetMapping("/add")
    public String addDeskForm(Model model) {
        model.addAttribute("desk", new Desk());
        return "Desks/addDesk";
    }

    /**
     * Adds a new desk based on the submitted form data.
     *
     * @param desk                the desk to be added
     * @param bindingResult       the binding result for validation
     * @param redirectAttributes  the redirect attributes for flash messages
     * @return the redirect URL after adding the desk
     */
    @PostMapping("/add")
    public String addDesk(@Valid Desk desk, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "Desks/addDesk";
        } else {
            this.deskService.addDesk(desk);
            redirectAttributes.addFlashAttribute("message", "Desk added successfully!");
            return "redirect:/web/desks";
        }
    }

    /**
     * Displays the details of a specific desk.
     *
     * @param id     the ID of the desk to be viewed
     * @param model  the model to be used
     * @return the view name for viewing a desk
     */
    @GetMapping("/view/{id}")
    public String viewDesk(@PathVariable Long id, Model model) {
        try {
            Desk desk = this.deskService.getDeskById(id);
            model.addAttribute("myDesk", desk);
            return "Desks/viewDesk";
        } catch (ResourceNotFoundException e) {
            return "redirect:/web/desks";
        }
    }

    /**
     * Displays the form for updating a specific desk.
     *
     * @param id     the ID of the desk to be updated
     * @param model  the model to be used
     * @return the view name for updating a desk
     */
    @GetMapping("/update/{id}")
    public String updateDeskForm(@PathVariable Long id, Model model) {
        try {
            Desk desk = this.deskService.getDeskById(id);
            model.addAttribute("updatedDesk", desk);
            return "Desks/updateDesk";
        } catch (ResourceNotFoundException e) {
            return "redirect:/web/desks";
        }
    }

    /**
     * Updates a specific desk based on the submitted form data.
     *
     * @param desk                the updated desk
     * @param bindingResult       the binding result for validation
     * @param redirectAttributes  the redirect attributes for flash messages
     * @return the redirect URL after updating the desk
     */
    @PostMapping("/update")
    public String updateDesk(@Valid Desk desk, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "Desks/updateDesk";
        } else {
            try {
                this.deskService.updateDesk(desk);
                redirectAttributes.addFlashAttribute("message", "Desk updated successfully!");
                return "redirect:/web/desks";
            } catch (ResourceNotFoundException e) {
                return "redirect:/web/desks";
            }
        }
    }

    /**
     * Displays the form for deleting a specific desk.
     *
     * @param id     the ID of the desk to be deleted
     * @param model  the model to be used
     * @return the view name for deleting a desk
     * @throws ResourceNotFoundException if the desk with the given ID is not found
     */
    @GetMapping("/delete/{id}")
    public String deleteDeskForm(@PathVariable Long id, Model model) throws ResourceNotFoundException {
        Desk desk = this.deskService.getDeskById(id);
        model.addAttribute("desk", desk);
        return "Desks/deleteDesk";
    }

    /**
     * Deletes a specific desk.
     *
     * @param id                   the ID of the desk to be deleted
     * @param redirectAttributes  the redirect attributes for flash messages
     * @return the redirect URL after deleting the desk
     */
    @PostMapping("/delete/{id}")
    public String deleteDesk(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            this.deskService.deleteDeskById(id);
        } catch (ResourceDeletionFailureException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete the desk.");
            return "redirect:/web/desks";
        }
        return "redirect:/web/desks";
    }

    /**
     * Displays the error page.
     *
     * @return the view for the error page
     */
    @GetMapping("/error")
    public String getError() {
        return "error";
    }
}

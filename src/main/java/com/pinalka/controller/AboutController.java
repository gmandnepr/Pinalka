package com.pinalka.controller;

import com.pinalka.domain.User;
import com.pinalka.services.LocalizationService;
import com.pinalka.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for about page
 *
 * @author gman
 */
@Controller
public class AboutController {

    @Autowired
    private UserService userService;

    @Autowired
    private LocalizationService localizationService;

    /**
     * Populate model of the about page
     *
     * @param model is the model to populate
     * @return page template
     */
    @RequestMapping(value = "/about")
    public String about(final Model model) {

        final User user = userService.getCurrentUser();
        localizationService.fillCommon(model, user, "page.about");
        model.addAttribute("text", localizationService.getMessage("text.about"));

        return "/about";
    }
}

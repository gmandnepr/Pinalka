package com.pinalka.controller;

import com.pinalka.domain.User;
import com.pinalka.services.LocalizationService;
import com.pinalka.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for register page
 *
 * @author gman
 */
@Controller
public class LoginController {

    private static final String LOGIN = "/login";

    @Autowired
    private UserService userService;

    @Autowired
    private LocalizationService localizationService;

    /**
     * Populate model of the login page
     *
     * @param model is the model to populate
     * @return page template
     */
    @RequestMapping(value = "/login")
    public String login(final Model model) {

        final User user = userService.getCurrentUser();
        localizationService.fillCommon(model, user, "page.login");
        fillCommonInLogin(model, false);

        return LOGIN;
    }

    /**
     * Populate model of the login page after error
     *
     * @param model is the model to populate
     * @return page template
     */
    @RequestMapping(value = "/login/fail")
    public String loginFail(final Model model) {

        final User user = userService.getCurrentUser();
        localizationService.fillCommon(model, user, "page.login");
        fillCommonInLogin(model, true);

        return LOGIN;
    }

    /**
     * Fills common attributes in login page
     *
     * @param model is the model to populate
     * @param isFail indicator of previous logon fail
     */
    private void fillCommonInLogin(final Model model, final boolean isFail) {
        model.addAttribute("isFail", isFail);
        model.addAttribute("profileErrorLogin", localizationService.getMessage("profile.error.login"));
        model.addAttribute("profileName", localizationService.getMessage("profile.name"));
        model.addAttribute("profilePassword", localizationService.getMessage("profile.password"));
        model.addAttribute("profileActionRemember", localizationService.getMessage("profile.action.remember"));
        model.addAttribute("profileActionLogin", localizationService.getMessage("profile.action.login"));
    }
}

package com.pinalka.controller;

import com.pinalka.domain.User;
import com.pinalka.services.LocalizationService;
import com.pinalka.services.UserService;
import com.pinalka.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

/**
 * Controller for register page
 *
 * @author gman
 */
@Controller
public class RegisterController {

    @Autowired
    private UserService userService;

    @Autowired
    private LocalizationService localizationService;

    /**
     * Show registration form
     *
     * @param model model to populate
     * @return page template
     */
    @RequestMapping(value = "/register")
    public String register(final Model model){

        final User user = userService.getCurrentUser();
        localizationService.fillCommon(model, user, "page.register");

        model.addAttribute(Constants.USER_PARAM, new User());

        fillCommonInRegisterForm(model);

        return "/register";
    }

    /**
     * Perform validation and register user with new information if valid
     *
     * @param password1 is the new password
     * @param password2 is the new password confirmation
     * @param user is the user to update information
     * @param bindingResult result of binding and validation
     * @param model is the model to populate with data
     * @return page template in case of error or redirect
     */
    @RequestMapping(value = "/register/action", method = RequestMethod.POST)
    public String registerAction(@RequestParam("password1") final String password1,
                              @RequestParam("password2") final String password2,
                              @Valid final User user,
                              final BindingResult bindingResult,
                              final Model model) {

        if(!password1.equals(password2)){
            bindingResult.addError(new FieldError(Constants.USER_PARAM, "pass", "profile.error.password.notequal"));
        }

        if(password1.length() < User.MIN_PASSWORD_LENGTH){
            bindingResult.addError(new FieldError(Constants.USER_PARAM, "pass", "profile.error.password.length"));
        }

        if(userService.isNameOccupied(user.getName())){
            bindingResult.addError(new FieldError(Constants.USER_PARAM, "name", "profile.error.name.occupied"));
        }

        if(bindingResult.hasErrors()){
            localizationService.fillCommon(model, user, "page.register");
            fillCommonInRegisterForm(model);

            return "/register";
        }

        userService.register(user, password1);

        return "redirect:/login";
    }

    /**
     * Fills common information into the registration form
     *
     * @param model is the model to populate
     */
    private void fillCommonInRegisterForm(final Model model) {

        model.addAttribute("actionUrl", "/register/action");

        model.addAttribute("text", localizationService.getMessage("text.register"));
        model.addAttribute("profileName", localizationService.getMessage("profile.name"));
        model.addAttribute("profileEmail", localizationService.getMessage("profile.email"));
        model.addAttribute("profilePassword", localizationService.getMessage("profile.password"));
        model.addAttribute("profilePasswordRepeat", localizationService.getMessage("profile.password.repeat"));
        model.addAttribute("profileActionRegister", localizationService.getMessage("profile.action.register"));
    }
}

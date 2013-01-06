package com.pinalka.controller;

import com.pinalka.controller.editors.RolePropertyEditor;
import com.pinalka.controller.editors.VisibilityPolicyPropertyEditor;
import com.pinalka.domain.UserRole;
import com.pinalka.domain.VisibilityPolicy;
import com.pinalka.domain.User;
import com.pinalka.services.LocalizationService;
import com.pinalka.services.UserService;
import com.pinalka.util.Constants;
import com.pinalka.util.UserUtils;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Controller for profile pages
 *
 * @author gman
 */
@Controller
@RequestMapping(value = "/profile")
public class ProfileController {

    private static final String REDIRECT = "redirect:";
    private static final String PROFILE = "/profile";
    private static final String PROFILE_OF = "/profile/%s";
    private static final String MODIFY_PROFILE = "/modifyProfile";

    @Autowired
    private UserService userService;
    
    @Autowired
    private LocalizationService localizationService;

    /**
     * Initialize binder with custom property editors
     * @param binder binder to register editors
     */
    @InitBinder
    protected void initBinder(final PropertyEditorRegistry binder) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyMMddHHmmss"), true));
        binder.registerCustomEditor(VisibilityPolicy.class, new VisibilityPolicyPropertyEditor(localizationService));
        binder.registerCustomEditor(UserRole.class,  new RolePropertyEditor(localizationService));
    }

    /**
     * Show profile for the logged user
     *
     * @param model is the model to populate
     * @return page template or redirect in case of error
     */
    @RequestMapping(value = "")
    public String myProfile(final Model model) {

        final User user = userService.getCurrentUser();
        localizationService.fillCommon(model, user, "page.profile");
        fillCommonInProfile(model, user);

        return PROFILE;
    }

    /**
     * Show profile for the user
     *
     * @param model is the model to populate
     * @param name is the name of the user to view profile
     * @return page template or redirect in case of error
     */
    @RequestMapping(value = "/{name}")
    public String publicProfile(final Model model, @PathVariable(Constants.NAME_PARAM) final String name) {

        final User owner = userService.getUserByName(name);
        if(UserUtils.isAnonymousUser(owner)){
            return REDIRECT+PROFILE;
        }
        final User user = userService.getCurrentUser();

        localizationService.fillCommon(model, user, "page.profile");
        fillCommonInProfile(model, owner);

        return PROFILE;
    }

    /**
     * Show profile edit form for the user
     *
     * @param model is the model to populate
     * @param name is the name of the user to edit
     * @return page template or redirect in case of error
     */
    @RequestMapping(value = "/{name}/edit")
    public String editProfile(final Model model, @PathVariable(Constants.NAME_PARAM) final String name) {

        final User user = userService.getCurrentUser();
        final User owner = userService.getUserByName(name);
        if(!UserUtils.isAccessible(owner, user)) {
            return REDIRECT+PROFILE;
        }

        localizationService.fillCommon(model, user, "page.profile.edit");
        model.addAttribute("isAdmin", user.getRole() == UserRole.ROLE_ADMIN);
        fillCommonInProfileEdit(model, owner);

        return MODIFY_PROFILE;
    }
    
    /**
     * Perform validation and update user with new information if valid
     *
     * @param name is the username to edit
     * @param password1 is the new password
     * @param password2 is the new password confirmation
     * @param diff is the user to update information
     * @param bindingResult result of binding and validation
     * @param model is the model to populate
     * @return page template or redirect in case of error
     */
    @RequestMapping(value = "/{name}/action/edit", method = RequestMethod.POST)
    public String editProfileAction(@PathVariable(Constants.NAME_PARAM) final String name,
                              @RequestParam("password1") final String password1,
                              @RequestParam("password2") final String password2,
                              @Valid final User diff,
                              final BindingResult bindingResult,
                              final Model model) {

        final User user = userService.getCurrentUser();
        final User owner = userService.getUserByName(name);
        if(!UserUtils.isAccessible(owner, user)) {
            return REDIRECT+PROFILE;
        }

        if(!password1.isEmpty() && !password1.equals(password2)){
            bindingResult.addError(new FieldError(Constants.USER_PARAM, "pass", "profile.error.password.notequal"));
        }

        if(!password1.isEmpty() && password1.length() < User.MIN_PASSWORD_LENGTH){
            bindingResult.addError(new FieldError(Constants.USER_PARAM, "pass", "profile.error.password.length"));
        }

        if(bindingResult.hasErrors()){
            localizationService.fillCommon(model, user, "page.profile.edit");
            fillCommonInProfileEdit(model, diff);

            return MODIFY_PROFILE;
        }

        userService.updateDetail(owner, diff, password1);

        return REDIRECT+String.format(PROFILE_OF, owner.getName());
    }

    /**
     * Fills common information into the registration form
     *
     * @param model is the model to populate
     * @param user is the user to obtain data
     */
    private void fillCommonInProfile(final Model model, final User user) {
        model.addAttribute(Constants.USER_PARAM, user);
        model.addAttribute("profileName", localizationService.getMessage("profile.name"));
        model.addAttribute("profileRegistered", localizationService.getMessage("profile.registered"));
    }

    /**
     * Fills common information into the registration form
     *
     * @param model is the model to populate
     * @param user is the user to obtain data
     */
    private void fillCommonInProfileEdit(final Model model, final User user) {
        model.addAttribute(Constants.USER_PARAM, user);

        model.addAttribute("actionUrl", String.format("/profile/%s/action/edit", user.getName()));

        model.addAttribute("profileName", localizationService.getMessage("profile.name"));
        model.addAttribute("profilePasswordNew", localizationService.getMessage("profile.password.new"));
        model.addAttribute("profilePasswordRepeat", localizationService.getMessage("profile.password.repeat"));
        model.addAttribute("profileRole", localizationService.getMessage("profile.role"));
        model.addAttribute("profileLocale", localizationService.getMessage("profile.locale"));
        model.addAttribute("profileEmail", localizationService.getMessage("profile.email"));
        model.addAttribute("profileVisibilityPolicy", localizationService.getMessage("profile.visibility.policy"));
        model.addAttribute("profileActionSave", localizationService.getMessage("profile.action.save"));

        model.addAttribute("visibilityPolicies", VisibilityPolicy.values());
        model.addAttribute("roles", UserRole.values());
    }
}

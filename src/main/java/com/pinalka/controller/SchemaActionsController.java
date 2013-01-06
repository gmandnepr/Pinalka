package com.pinalka.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.pinalka.controller.editors.StatePropertyEditor;
import com.pinalka.domain.State;
import com.pinalka.domain.Subject;
import com.pinalka.domain.User;
import com.pinalka.services.LocalizationService;
import com.pinalka.services.SubjectsService;
import com.pinalka.services.UserService;
import com.pinalka.util.Constants;
import com.pinalka.util.UserUtils;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * This controller will be a provider for ajax action services requests
 * Will be rewritten using @ResponseBody when ajax will be used
 *
 * @author gman
 */
@Controller
@RequestMapping(value = "/schema/{name}/action")
public class SchemaActionsController {

    private static final String REDIRECT = "redirect:/schema";
    private static final String REDIRECT_OK = "redirect:/schema/%s";

    @Autowired
    private UserService userService;

    @Autowired
    private SubjectsService subjectsService;

    @Autowired
    private LocalizationService localizationService;

    /**
     * Initialize binder with custom property editors
     *
     * @param binder binder to register editors
     */
    @InitBinder
    protected void initBinder(final PropertyEditorRegistry binder) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("dd/MM/yyyy"), true));
        binder.registerCustomEditor(State.class, new StatePropertyEditor(localizationService));
    }

    /**
     * Adds a public subject to the users schema
     *
     * @param id of the public subject
     * @param name is the name of the subject owner
     * @return redirect
     */
//    @ResponseBody
    @RequestMapping(value = "/add/public/{id}")
    public String addNewPublicSubjectAction(@PathVariable(Constants.NAME_PARAM) final String name,
                                            @PathVariable("id") final Long id) {

        final User user = userService.getCurrentUser();
        final User owner = userService.getUserByName(name);
        if(!UserUtils.isAccessible(owner, user)) {
            return REDIRECT;
        }

        subjectsService.addNewPublicSubject(owner, id);

        return String.format(REDIRECT_OK, owner.getName());
    }

    /**
     * Adds a custom subject to the user schema
     *
     * @param subject is the subject to add
     * @param name is the name of the subject owner
     * @return redirect
     */
//    @ResponseBody
    @RequestMapping(value = "/add/own", method = RequestMethod.POST)
    public String addNewOwnSubjectAction(@PathVariable(Constants.NAME_PARAM) final String name,
                                         @ModelAttribute("subject") final Subject subject) {

        final User user = userService.getCurrentUser();
        final User owner = userService.getUserByName(name);
        if(!UserUtils.isAccessible(owner, user)) {
            return REDIRECT;
        }

        subjectsService.addNewOwnSubject(owner, subject);

        return String.format(REDIRECT_OK, owner.getName());
    }

    /**
     * Edits a subject from the user schema
     *
     * @param subject is the subject to edit
     * @param bindingResult is the result of binding
     * @param name is the name of the subject owner
     * @return redirect
     */
//    @ResponseBody
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String editSubjectAction(@PathVariable(Constants.NAME_PARAM) final String name,
                                    @ModelAttribute("subject") final Subject subject,
                                    final BindingResult bindingResult) {

        final User user = userService.getCurrentUser();
        final User owner = userService.getUserByName(name);
        if(!UserUtils.isAccessible(owner, user)) {
            return REDIRECT;
        }

        if (!bindingResult.hasErrors()) {
            //TODO

            subjectsService.editSubject(owner, subject.getId(), subject);
        }

        return String.format(REDIRECT_OK, owner.getName());
    }

    /**
     * Removes user subject from his/her schema
     *
     * @param id of the users subject
     * @param name is the name of the subject owner
     * @return redirect
     */
//    @ResponseBody
    @RequestMapping(value = "/remove/{id}")
    public String removeSubjectAction(@PathVariable(Constants.NAME_PARAM) final String name,
                                      @PathVariable("id") final Long id) {

        final User user = userService.getCurrentUser();
        final User owner = userService.getUserByName(name);
        if(!UserUtils.isAccessible(owner, user)) {
            return REDIRECT;
        }

        subjectsService.removeSubject(owner, id);

        return String.format(REDIRECT_OK, owner.getName());
    }

    /**
     * Increase priority of the subject
     *
     * @param id of the users subject
     * @param name is the name of the subject owner
     * @return redirect
     */
//    @ResponseBody
    @RequestMapping(value = "/priority/inc/{id}")
    public String increasePriority(@PathVariable(Constants.NAME_PARAM) final String name,
                                   @PathVariable("id") final Long id) {

        final User user = userService.getCurrentUser();
        final User owner = userService.getUserByName(name);
        if(!UserUtils.isAccessible(owner, user)) {
            return REDIRECT;
        }

        subjectsService.increasePriority(owner, id);

        return String.format(REDIRECT_OK, owner.getName());
    }

    /**
     * Decrease priority of the subject
     *
     * @param id of the users subject
     * @param name is the name of the subject owner
     * @return redirect
     */
//    @ResponseBody
    @RequestMapping(value = "/priority/dec/{id}")
    public String decreasePriority(@PathVariable(Constants.NAME_PARAM) final String name,
                                   @PathVariable("id") final Long id) {

        final User user = userService.getCurrentUser();
        final User owner = userService.getUserByName(name);
        if(!UserUtils.isAccessible(owner, user)) {
            return REDIRECT;
        }

        subjectsService.decreasePriority(owner, id);

        return String.format(REDIRECT_OK, owner.getName());
    }

    /**
     * Update state of the subject
     *
     * @param name is the name of the subject owner
     * @param subjectId is the id of the subject of the item
     * @param itemId is the id of the item
     * @param state is the new state
     * @return redirect
     */
//    @ResponseBody
    @RequestMapping(value = "/state/{subjectId}/{itemId}/{state}/")
    public String changeStatus(@PathVariable(Constants.NAME_PARAM) final String name,
                               @PathVariable("subjectId") final Long subjectId,
                               @PathVariable("itemId") final Long itemId,
                               @PathVariable("state") final String state) {

        final User user = userService.getCurrentUser();
        final User owner = userService.getUserByName(name);
        if(!UserUtils.isAccessible(owner, user)) {
            return REDIRECT;
        }

        subjectsService.updateState(owner, subjectId, itemId, State.valueOf(state));

        return String.format(REDIRECT_OK, owner.getName());
    }
}

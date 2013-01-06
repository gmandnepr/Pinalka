package com.pinalka.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.pinalka.controller.editors.StatePropertyEditor;
import com.pinalka.domain.Item;
import com.pinalka.domain.PublicSubject;
import com.pinalka.domain.State;
import com.pinalka.domain.Subject;
import com.pinalka.domain.User;
import com.pinalka.services.LocalizationService;
import com.pinalka.services.PublicSubjectService;
import com.pinalka.services.SubjectsService;
import com.pinalka.services.UserService;
import com.pinalka.util.Constants;
import com.pinalka.util.SubjectsUtil;
import com.pinalka.util.UserUtils;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for the schema pages
 *
 * @author gman
 */
@Controller
@RequestMapping(value = "/schema")
public class SchemaController {

    private static final String REDIRECT = "redirect:";
    private static final String SCHEMA = "/schema";
    private static final String MODIFY_SUBJECT = "/modifySubject";
    private static final String PUBLIC_SUBJECT_LIST = "/subjectList";
    private static final String CHANGE_STATE = "/changeState";

    private static final int ONE_HUNDRED_PERCENT = 100;

    @Autowired
    private UserService userService;

    @Autowired
    private SubjectsService subjectsService;

    @Autowired
    private PublicSubjectService publicSubjectService;

    @Autowired
    private LocalizationService localizationService;

    /**
     * Initialize binder with custom property editors
     * @param binder binder to register editors
     */
    @InitBinder
    protected void initBinder(final PropertyEditorRegistry binder) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("dd/MM/yyyy"), true));
        binder.registerCustomEditor(State.class, new StatePropertyEditor(localizationService));
    }

    /**
     * Show user own schema
     *
     * @param model to populate
     * @return page template name
     */
    @RequestMapping(value = "")
    public String mySchema(final Model model) {

        final User user = userService.getCurrentUser();
        final List<Subject> subjects  = subjectsService.getUserSortedSubjects(user);

        model.addAttribute("ownSchema", true);
        localizationService.fillCommon(model, user, "page.schema");
        fillCommonInSchema(model, user, subjects);

        return SCHEMA;
    }

    /**
     * Show schema of the given user
     *
     * @param name is the name of the schema owner
     * @param model to populate
     * @return page template name
     */
    @RequestMapping(value = "/{name}")
    public String publicSchema(final Model model, @PathVariable(Constants.NAME_PARAM) final String name) {

        final User owner = userService.getUserByName(name);
        if (UserUtils.isAnonymousUser(owner)) {
            return REDIRECT + SCHEMA;
        }
        final User user = userService.getCurrentUser();

        final boolean ownSchema = UserUtils.isAccessible(owner, user);
        final List<Subject> subjects;
        if (ownSchema) {
            subjects = subjectsService.getUserSortedSubjects(owner);
        } else {
            subjects = subjectsService.getUserSortedSubjectsByPolicy(owner);
        }

        model.addAttribute("ownSchema", ownSchema);
        localizationService.fillCommon(model, user, "page.schema");
        fillCommonInSchema(model, owner, subjects);

        return SCHEMA;
    }

    /**
     * Add subject from the full list of public ones
     *
     * @param model to populate
     * @return page template name
     */
    @RequestMapping(value = "/{name}/add/public")
    public String addNewPublicSubject(final Model model, @PathVariable(Constants.NAME_PARAM) final String name) {

        final User user = userService.getCurrentUser();
        final User owner = userService.getUserByName(name);
        if(!UserUtils.isAccessible(owner, user)) {
            return REDIRECT+SCHEMA;
        }
        final List<PublicSubject> publicSubjects = publicSubjectService.getPublicSubjects();

        localizationService.fillCommon(model, user, "page.schema.add.public");
        fillCommonInSchemaPublic(model, owner, publicSubjects);

        return PUBLIC_SUBJECT_LIST;
    }

    /**
     * Add subject from the list of the recommended ones
     *
     * @param model to populate
     * @return page template name
     */
    @RequestMapping(value = "/{name}/add/recommended")
    public String addNewRecommendedSubject(final Model model, @PathVariable(Constants.NAME_PARAM) final String name) {

        final User user = userService.getCurrentUser();
        final User owner = userService.getUserByName(name);
        if(!UserUtils.isAccessible(owner, user)) {
            return REDIRECT+SCHEMA;
        }
        final List<PublicSubject> recommendedSubjects = publicSubjectService.getRecommendedPublicSubjects(user);

        localizationService.fillCommon(model, user, "page.schema.add.recommended");
        fillCommonInSchemaPublic(model, owner, recommendedSubjects);

        return PUBLIC_SUBJECT_LIST;
    }

    /**
     * Create new own subject
     *
     * @param model to populate
     * @return page template name
     */
    @RequestMapping(value = "/{name}/add/own")
    public String addNewOwnSubject(final Model model, @PathVariable(Constants.NAME_PARAM) final String name) {

        final User user = userService.getCurrentUser();
        final User owner = userService.getUserByName(name);
        if(!UserUtils.isAccessible(owner, user)) {
            return REDIRECT+SCHEMA;
        }
        final Subject subject = SubjectsUtil.createSubject();

        localizationService.fillCommon(model, user, "page.schema.add.own");
        model.addAttribute("actionUrl", String.format("/schema/%s/action/add/own", owner.getName()));
        fillCommonInSubjectEdit(model, owner, subject);

        return MODIFY_SUBJECT;
    }

    /**
     * Edit subject by id
     *
     * @param id is the id of the subject to edit
     * @param model to populate
     * @return page template name
     */
    @RequestMapping(value = "/{name}/edit/{id}")
    public String editSubject(final Model model,
                              @PathVariable(Constants.NAME_PARAM) final String name,
                              @PathVariable("id") final Long id) {

        final User user = userService.getCurrentUser();
        final User owner = userService.getUserByName(name);
        if(!UserUtils.isAccessible(owner, user)) {
            return REDIRECT+SCHEMA;
        }
        final Subject subject = subjectsService.getSubjectById(owner, id);

        localizationService.fillCommon(model, user, "page.schema.edit");
        model.addAttribute("actionUrl", String.format("/schema/%s/action/edit", owner.getName()));
        fillCommonInSubjectEdit(model, owner, subject);

        return MODIFY_SUBJECT;
    }

    /**
     * Update state of the item by subject and item id
     *
     * @param model to populate
     * @param subjectId is the id of the subject
     * @param itemId is the id of the item
     * @return page template name
     */
    @RequestMapping(value = "/{name}/state/{subjectId}/{itemId}")
    public String changeState(final Model model,
                              @PathVariable(Constants.NAME_PARAM) final String name,
                              @PathVariable("subjectId") final Long subjectId,
                              @PathVariable("itemId") final Long itemId) {

        final User user = userService.getCurrentUser();
        final User owner = userService.getUserByName(name);
        if(!UserUtils.isAccessible(owner, user)) {
            return REDIRECT+SCHEMA;
        }
        final Subject subject = subjectsService.getSubjectById(owner, subjectId);
        final Item item = subjectsService.getItemById(owner, subjectId, itemId);

        model.addAttribute("states", State.values());
        model.addAttribute(Constants.USER_PARAM, owner);
        model.addAttribute("subject", subject);
        model.addAttribute("item", item);
        model.addAttribute("schemaSubjectDescription", localizationService.getMessage("schema.subject.description"));
        model.addAttribute("schemaSubjectTeacher", localizationService.getMessage("schema.subject.teacher"));
        model.addAttribute("schemaItemDescription", localizationService.getMessage("schema.item.description"));
        model.addAttribute("schemaItemDeadline", localizationService.getMessage("schema.item.deadline"));
        model.addAttribute("schemaDone", localizationService.getMessage("schema.done", item.getState().getDoneRatio()*ONE_HUNDRED_PERCENT));
        localizationService.fillCommon(model, user, "page.schema.state");

        return CHANGE_STATE;
    }

    /**
     * Fills common attributes for the schema
     *
     * @param model to fill attributes
     * @param user owner
     * @param subjects subjects to fill
     */
    private void fillCommonInSchema(final Model model, final User user, final List<Subject> subjects) {
        model.addAttribute(Constants.USER_PARAM, user);
        model.addAttribute("subjects", subjects);

        model.addAttribute("schemaOwner", localizationService.getMessage("schema.owner", user.getName()));
        model.addAttribute("schemaDone", localizationService.getMessage("schema.done", 0));
        model.addAttribute("schemaSubjectActionUp", localizationService.getMessage("schema.subject.action.up"));
        model.addAttribute("schemaSubjectActionDown", localizationService.getMessage("schema.subject.action.down"));
        model.addAttribute("schemaSubjectActionEdit", localizationService.getMessage("schema.subject.action.edit"));
        model.addAttribute("schemaSubjectActionDelete", localizationService.getMessage("schema.subject.action.delete"));
        model.addAttribute("schemaSubjectActionAdd", localizationService.getMessage("schema.subject.action.add"));
        model.addAttribute("schemaSubjectActionAddPublic", localizationService.getMessage("schema.subject.action.add.public"));
        model.addAttribute("schemaSubjectActionAddRecommended", localizationService.getMessage("schema.subject.action.add.recommended"));
        model.addAttribute("schemaSubjectActionAddOwn", localizationService.getMessage("schema.subject.action.add.own"));
    }

    /**
     * Fills common attributes in public schema
     *
     * @param model to fill attributes
     * @param publicSubjects public subjects to fill
     */
    private void fillCommonInSchemaPublic(final Model model, final User user, final List<PublicSubject> publicSubjects){
        model.addAttribute(Constants.USER_PARAM, user);
        model.addAttribute("subjects", publicSubjects);

        model.addAttribute("schemaSubjectActionAdd", localizationService.getMessage("schema.subject.action.add"));
    }

    /**
     * Fills common attributes in subject editor
     *
     * @param model to fill attributes
     * @param subject subject to fill
     */
    private void fillCommonInSubjectEdit(final Model model, final User user, final Subject subject) {
        model.addAttribute(Constants.USER_PARAM, user);
        model.addAttribute("subject", subject);
        model.addAttribute("statesValues", State.values());
        model.addAttribute("itemsSize", subject.getItems().size());

        model.addAttribute("schemaSubjectLabel", localizationService.getMessage("schema.subject.label"));
        model.addAttribute("schemaSubjectDescription", localizationService.getMessage("schema.subject.description"));
        model.addAttribute("schemaSubjectTeacher", localizationService.getMessage("schema.subject.teacher"));
        model.addAttribute("schemaSubjectPublic", localizationService.getMessage("schema.subject.public"));
        model.addAttribute("schemaSubjectPriority", localizationService.getMessage("schema.subject.priority"));

        model.addAttribute("schemaItemActionAdd", localizationService.getMessage("schema.item.action.add"));
        model.addAttribute("schemaItemActionRemove", localizationService.getMessage("schema.item.action.remove"));
        model.addAttribute("profileActionSave", localizationService.getMessage("profile.action.save"));

        model.addAttribute("schemaItemLabel", localizationService.getMessage("schema.item.label"));
        model.addAttribute("schemaItemDescription", localizationService.getMessage("schema.item.description"));
        model.addAttribute("schemaItemDeadline", localizationService.getMessage("schema.item.deadline"));
        model.addAttribute("schemaItemState", localizationService.getMessage("schema.item.state"));
    }
}

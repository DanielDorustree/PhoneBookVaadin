/*
 * Copyright (c) 2023 Dorustree private limited. All rights reserved.
 */

package phonebook.form;

import com.vaadin.data.converter.LocalDateToDateConverter;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.TextField;
import phonebook.model.PersonModel;
import phonebook.repository.PersonRepository;
import org.vaadin.spring.events.EventBus;
import org.vaadin.teemu.switchui.Switch;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import phonebook.handler.ModifiedEvent;
import phonebook.componet.UserGroupField;

/**
 * <h1>Person Form</h1>
 *
 * <p>Form to handle new or update person {@link PersonModel}.</p>
 *
 */
@UIScope
@SpringComponent
public class PersonForm
    extends AbstractForm<PersonModel>
{

    //
    // autowired
    //

    private final EventBus.UIEventBus eventBus;
    private final PersonRepository personRepository;

    //
    // attributes
    //

    private final TextField name = new MTextField("Name");
    private final TextField email = new MTextField("Email");
    private final TextField phoneNumber = new MTextField("Phone");
    private final DateField birthDay = new DateField("Birthday");
    private final Switch colleague = new Switch("Colleague");
    private final UserGroupField category = new UserGroupField("Special Purpose");

    //
    // constructor
    //

    PersonForm (
        final PersonRepository personRepository
        , final EventBus.UIEventBus eventBus
    )
    {
        super( PersonModel.class );
        this.personRepository = personRepository;
        this.eventBus = eventBus;

        // On save & cancel, publish events that other parts of the UI can listen
        setSavedHandler(personModel -> {
            // persist changes
            personRepository.save(personModel);
            // send the event for other parts of the application
            eventBus.publish( this, new ModifiedEvent(personModel) );
        });

        setResetHandler(event ->
            eventBus.publish( this, new ModifiedEvent(event) )
        );

        setSizeUndefined();
    }

    //
    // operations
    //

    /**
     * Bind vaadin local date to java date field
     */
    @Override
    protected void bind()
    {
        // DateField in Vaadin 8 uses LocalDate by default, the backend
        // uses plain old java.util.Date, thus we need a converter, using
        // built in helper here
        getBinder()
                .forMemberField(birthDay)
                .withConverter(new LocalDateToDateConverter());
        super.bind();
    }

    /**
     * Create person form
     *
     * @return
     *      {@link Component}
     */
    @Override
    protected Component createContent()
    {
        return new MVerticalLayout(
                new MFormLayout(
                        name,
                        email,
                        phoneNumber,
                        birthDay,
                        colleague,
                        category
                ).withWidth(""),
                getToolbar()
        ).withWidth("");
    }
}
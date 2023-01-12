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

@UIScope
@SpringComponent
public class PersonForm extends AbstractForm<PersonModel> {

    private static final long serialVersionUID = 1L;

    EventBus.UIEventBus eventBus;
    PersonRepository personRepository;

    TextField name = new MTextField("Name");
    TextField email = new MTextField("Email");
    TextField phoneNumber = new MTextField("Phone");
    DateField birthDay = new DateField("Birthday");
    Switch colleague = new Switch("Colleague");
    @lombok.Getter
    UserGroupField category = new UserGroupField("Special Purpose");

    PersonForm(PersonRepository r, EventBus.UIEventBus b) {
        super(PersonModel.class);
        this.personRepository = r;
        this.eventBus = b;

        // On save & cancel, publish events that other parts of the UI can listen
        setSavedHandler(personModel -> {
//            getBinder().forField(getCategory()).bind(PersonModel::getCategory, PersonModel::setCategory);
            // persist changes
            personRepository.save(personModel);
            // send the event for other parts of the application
            eventBus.publish(this, new ModifiedEvent(personModel));
        });
        setResetHandler(p -> eventBus.publish(this, new ModifiedEvent(p)));

        setSizeUndefined();
    }

    @Override
    protected void bind() {
        // DateField in Vaadin 8 uses LocalDate by default, the backend
        // uses plain old java.util.Date, thus we need a converter, using
        // built in helper here
        getBinder()
                .forMemberField(birthDay)
                .withConverter(new LocalDateToDateConverter());
        super.bind();
    }

    @Override
    protected Component createContent() {
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

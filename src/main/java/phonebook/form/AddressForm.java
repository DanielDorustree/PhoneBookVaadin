package phonebook.form;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Component;
import com.vaadin.ui.TextField;
import org.vaadin.spring.events.EventBus;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import phonebook.model.AddressModel;
import phonebook.repository.AddressRepository;
import phonebook.handler.ModifiedEvent;

@UIScope
@SpringComponent
public class AddressForm extends AbstractForm<AddressModel> {

    private static final long serialVersionUID = 1L;

    EventBus.UIEventBus eventBus;
    AddressRepository addressRepository;

    TextField doorNo = new MTextField("Door Number");
    TextField street = new MTextField("Street");
    TextField area = new MTextField("Area");
    TextField district = new MTextField("District");
    TextField state = new MTextField("State");
    TextField pinCode = new MTextField("Pin code");

    AddressForm(AddressRepository r, EventBus.UIEventBus b) {
        super(AddressModel.class);
        this.addressRepository = r;
        this.eventBus = b;
        // On save & cancel, publish events that other parts of the UI can listen
        setSavedHandler(AddressModel -> {
//            getBinder().forField(getCategory()).bind(AddressModel::getCategory, AddressModel::setCategory);
            // persist changes
            addressRepository.save(AddressModel);
            // send the event for other parts of the application
            eventBus.publish(this, new ModifiedEvent(AddressModel));
        });
        setResetHandler(p -> eventBus.publish(this, new ModifiedEvent(p)));

        setSizeUndefined();
    }

    @Override
    protected void bind() {
        // DateField in Vaadin 8 uses LocalDate by default, the backend
        // uses plain old java.util.Date, thus we need a converter, using
        // built in helper here
        super.bind();
    }

    @Override
    protected Component createContent() {
        return new MVerticalLayout(
                new MFormLayout(
                        doorNo,
                        street,
                        area,
                        district,
                        state,
                        pinCode
                ).withWidth(""),
                getToolbar()
        ).withWidth("");
    }

}

/*
 * Copyright (c) 2023 Dorustree private limited. All rights reserved.
 */

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

/**
 * <h1>Address Form</h1>
 *
 * <p>Form to handle new or update address {@link AddressModel} for person.</p>
 *
 */
@UIScope
@SpringComponent
public class AddressForm
    extends AbstractForm<AddressModel>
{

    //
    // autowired
    //

    private final EventBus.UIEventBus eventBus;
    private final AddressRepository addressRepository;

    //
    // attributes
    //

    private final TextField doorNo = new MTextField("Door Number");
    private final TextField street = new MTextField("Street");
    private final TextField area = new MTextField("Area");
    private final TextField district = new MTextField("District");
    private final TextField state = new MTextField("State");
    private final TextField pinCode = new MTextField("Pin code");

    //
    // constructor
    //

    AddressForm (
        final AddressRepository addressRepository
        , final EventBus.UIEventBus uiEventBus
    )
    {
        super( AddressModel.class );
        this.addressRepository = addressRepository;
        this.eventBus = uiEventBus;

        // On save & cancel, publish events that other parts of the UI can listen
        setSavedHandler(AddressModel -> {
            // persist changes
            addressRepository.save( AddressModel );
            // send the event for other parts of the application
            eventBus.publish( this, new ModifiedEvent(AddressModel) );
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
     * Create address form
     *
     * @return
     *      {@link Component}
     */
    @Override
    protected Component createContent()
    {
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
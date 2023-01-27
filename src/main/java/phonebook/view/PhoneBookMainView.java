/*
 * Copyright (c) 2023 Dorustree private limited. All rights reserved.
 */

package phonebook.view;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.UI;
import org.vaadin.viritin.label.Header;
import phonebook.model.AddressModel;
import phonebook.repository.AddressRepository;
import phonebook.model.PersonModel;
import phonebook.repository.PersonRepository;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventScope;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.viritin.button.ConfirmButton;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.grid.MGrid;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import phonebook.form.AddressForm;
import phonebook.handler.ModifiedEvent;
import phonebook.form.PersonForm;

/**
 * Phonebook crud applications main view
 */
@Title("Phonebook")
@Theme("valo")
@SpringUI
public class PhoneBookMainView extends UI
{

    //
    // attributes
    //

    private final PersonRepository personRepository;
    private final AddressRepository addressRepository;
    private final PersonForm personForm;
    private final AddressForm addressForm;
    private final EventBus.UIEventBus eventBus;
    private final Header header = new Header("Phone Book");
    private final MGrid<PersonModel> list = new MGrid<>(PersonModel.class)
            .withProperties("name", "email","phoneNumber","birthDay")
            .withColumnHeaders("Name", "Email","Phone Number","Birth Day")
            .withFullWidth();
    private final MTextField filterByName = new MTextField()
            .withPlaceholder("Filter by name");
    private final Button addNew = new MButton(VaadinIcons.PLUS, this::add);
    private final Button edit = new MButton(VaadinIcons.PENCIL, this::edit);
    private final Button delete = new ConfirmButton(VaadinIcons.TRASH,
            "Are you sure you want to delete the entry?", this::remove);
    private final Button updateAddress = new MButton(VaadinIcons.LOCATION_ARROW, this::editAddress);

    //
    // constructor
    //

    public PhoneBookMainView(
        final PersonRepository personRepository
        , final PersonForm personForm
        , final AddressForm addressForm
        , final EventBus.UIEventBus eventBus
        , final AddressRepository addressRepository
    )
    {
        this.personRepository = personRepository;
        this.personForm = personForm;
        this.addressForm = addressForm;
        this.eventBus = eventBus;
        this.addressRepository = addressRepository;
    }

    //
    // Spring initialization
    //

    /**
     *
     * @param request
     *            the Vaadin request {@link VaadinRequest} that caused this UI to be created
     */
    @Override
    protected void init(
        VaadinRequest request
    )
    {

        setContent(
                new MVerticalLayout(
                        header,
                        new MHorizontalLayout(filterByName, addNew, edit, delete, updateAddress),
                        list
                ).expand(list)
        );
        listEntities();

        list.asSingleSelect().addValueChangeListener(e -> adjustActionButtonState());
        filterByName.addValueChangeListener(event -> {
            listEntities( event.getValue() );
        });

        // Listen to change events emitted by PersonForm and address form see onPersonModified method
        eventBus.subscribe( this );
    }

    //
    // operations
    //

    /**
     * Adjust action button state with selection handler
     */
    protected void adjustActionButtonState() {
        boolean hasSelection = !list.getSelectedItems().isEmpty();
        edit.setEnabled(hasSelection);
        delete.setEnabled(hasSelection);
        updateAddress.setEnabled(hasSelection);
    }

    /**
     * List records from database
     */
    private void listEntities() {
        listEntities(filterByName.getValue());
    }

    /**
     * List records based on search
     * @param nameFilter
     *      {@link String}
     */
    private void listEntities(String nameFilter) {
        // We want to support filtering, first add the % marks for SQL name query
        String likeFilter = "%" + nameFilter + "%";

        // If there is a moderate amount of rows, one can just fetch everything
        list.setRows(personRepository.findByNameLikeIgnoreCase(likeFilter));

        adjustActionButtonState();
    }

    /**
     * Click listener for add new record
     * @param clickEvent
     *      {@link ClickEvent}
     */
    public void add(ClickEvent clickEvent) {
        edit(new PersonModel());
    }

    /**
     * Click listener for edit a person record
     * @param e
     *      {@link ClickEvent}
     */
    public void edit(ClickEvent e) {
        edit(list.asSingleSelect().getValue());
    }

    /**
     * Click listener for remove a record
     */
    public void remove() {
        personRepository.delete(list.asSingleSelect().getValue());
        list.deselectAll();
        listEntities();
    }

    /**
     * Click listener for edit a person record
     * @param phoneBookEntry
     *      {@link PersonModel}
     */
    protected void edit(final PersonModel phoneBookEntry) {
        personForm.setEntity(phoneBookEntry);
        personForm.openInModalPopup();
    }

    /**
     * Click listener for edit an address record
     * @param e
     *      {@link ClickEvent}
     */
    public void editAddress(ClickEvent e) {
        editAddress(list.asSingleSelect().getValue());
    }

    /**
     * Click listener for edit an address record
     * @param phoneBookEntry
     */
    protected void editAddress(final PersonModel phoneBookEntry) {
        AddressModel addressModel = addressRepository
            .findAll()
            .stream()
            .filter( address-> address.getPerson().getId() == phoneBookEntry.getId() )
            .findFirst()
            .orElse(new AddressModel());
        addressForm.setEntity(addressModel);
        addressForm.getEntity().setPerson(phoneBookEntry);
        addressForm.openInModalPopup();
    }

    /**
     * Record modify event handler
     * @param event
     *      {@link ModifiedEvent}
     */
    @EventBusListenerMethod(scope = EventScope.UI)
    public void onPersonModified(ModifiedEvent event) {
        listEntities();
        personForm.closePopup();
        addressForm.closePopup();
    }
}
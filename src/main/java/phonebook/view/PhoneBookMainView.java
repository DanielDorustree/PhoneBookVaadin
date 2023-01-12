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

@Title("Phone Book")
@Theme("valo")
@SpringUI
public class PhoneBookMainView extends UI {

    private static final long serialVersionUID = 1L;

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

    public PhoneBookMainView(PersonRepository pr, PersonForm pf, AddressForm af, EventBus.UIEventBus b,
                             AddressRepository ar) {
        this.personRepository = pr;
        this.personForm = pf;
        this.addressForm = af;
        this.eventBus = b;
        this.addressRepository = ar;
    }

    @Override
    protected void init(VaadinRequest request) {

        setContent(
                new MVerticalLayout(
                        header,
                        new MHorizontalLayout(filterByName, addNew, edit, delete, updateAddress),
                        list
                ).expand(list)
        );
        listEntities();

        list.asSingleSelect().addValueChangeListener(e -> adjustActionButtonState());
        filterByName.addValueChangeListener(e -> {
            listEntities(e.getValue());
        });

        // Listen to change events emitted by PersonForm see onEvent method
        eventBus.subscribe(this);
    }

    protected void adjustActionButtonState() {
        boolean hasSelection = !list.getSelectedItems().isEmpty();
        edit.setEnabled(hasSelection);
        delete.setEnabled(hasSelection);
        updateAddress.setEnabled(hasSelection);
    }

    private void listEntities() {
        listEntities(filterByName.getValue());
    }

    final int PAGESIZE = 45;

    private void listEntities(String nameFilter) {
        // We want to support filtering, first add the % marks for SQL name query
        String likeFilter = "%" + nameFilter + "%";

        // If there is a moderate amount of rows, one can just fetch everything
        list.setRows(personRepository.findByNameLikeIgnoreCase(likeFilter));

        adjustActionButtonState();
    }

    public void add(ClickEvent clickEvent) {
        edit(new PersonModel());
    }

    public void edit(ClickEvent e) {
        edit(list.asSingleSelect().getValue());
    }

    public void remove() {
        personRepository.delete(list.asSingleSelect().getValue());
        list.deselectAll();
        listEntities();
    }

    protected void edit(final PersonModel phoneBookEntry) {
        personForm.setEntity(phoneBookEntry);
        personForm.openInModalPopup();
    }

    public void editAddress(ClickEvent e) {
        editAddress(list.asSingleSelect().getValue());
    }

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
    @EventBusListenerMethod(scope = EventScope.UI)
    public void onPersonModified(ModifiedEvent event) {
        listEntities();
        personForm.closePopup();
        addressForm.closePopup();
    }

}

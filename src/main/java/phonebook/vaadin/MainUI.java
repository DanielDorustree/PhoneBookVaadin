package phonebook.vaadin;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import org.vaadin.viritin.label.Header;
import phonebook.spring.PersonModel;
import phonebook.spring.PersonRepository;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventScope;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.viritin.button.ConfirmButton;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.grid.MGrid;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

@Title("Phone Book")
@Theme("valo")
@SpringUI
public class MainUI extends UI {

    private static final long serialVersionUID = 1L;

    PersonRepository personRepository;
    PersonForm personForm;
    EventBus.UIEventBus eventBus;

    private final Header header = new Header("Phone Book");
    private final MGrid<PersonModel> list = new MGrid<>(PersonModel.class)
            .withProperties("id", "name", "email")
            .withColumnHeaders("id", "Name", "Email")
            .withFullWidth();

    private final MTextField filterByName = new MTextField()
            .withPlaceholder("Filter by name");
    private final Button addNew = new MButton(VaadinIcons.PLUS, this::add);
    private final Button edit = new MButton(VaadinIcons.PENCIL, this::edit);
    private final Button delete = new ConfirmButton(VaadinIcons.TRASH,
            "Are you sure you want to delete the entry?", this::remove);

    public MainUI(PersonRepository r, PersonForm f, EventBus.UIEventBus b) {
        this.personRepository = r;
        this.personForm = f;
        this.eventBus = b;
    }

    @Override
    protected void init(VaadinRequest request) {

        setContent(
                new MVerticalLayout(
                        header,
                        new MHorizontalLayout(filterByName, addNew, edit, delete),
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

    @EventBusListenerMethod(scope = EventScope.UI)
    public void onPersonModified(PersonModifiedEvent event) {
        listEntities();
        personForm.closePopup();
    }

}

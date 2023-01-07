package phonebook.vaadin;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.UI;
import phonebook.spring.PersonModel;
import phonebook.spring.PersonRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    private MGrid<PersonModel> list = new MGrid<>(PersonModel.class)
            .withProperties("id", "name", "email")
            .withColumnHeaders("id", "Name", "Email")
            // not yet supported by V8
            //.setSortableProperties("name", "email")
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
        // A dead simple in memory listing would be:
        // list.setRows(personRepository.findAll());

        // But we want to support filtering, first add the % marks for SQL name query
        String likeFilter = "%" + nameFilter + "%";
        
        // If there is a moderate amount of rows, one can just fetch everything
        //list.setRows(personRepository.findByNameLikeIgnoreCase(likeFilter));

        // Lazy binding for better optimized connection from the Vaadin Grid to
        // Spring Repository. This approach uses less memory and database
        // resources. Use this approach if you expect you'll have lots of data 
        // in your table. There are simpler APIs if you don't need sorting.
        list.setDataProvider(
                // lazy entity fetching strategy, due to a design flaw in DataProvider API,
                // this is bit tricky with Spring Data's Pageable abstraction as requests
                // by Grid may be on two pages, see https://github.com/vaadin/framework/issues/8982
                // TODO see if this could be simplified in Viritin MGrid
                (sortOrder, offset, limit) -> {
                    final int startPage = (int) Math.floor((double) offset / limit);
                    final int endPage = (int) Math.floor((double) (offset + limit - 1) / limit);
                    final Sort.Direction sortDirection = sortOrder.isEmpty() || sortOrder.get(0).getDirection() == SortDirection.ASCENDING ? Sort.Direction.ASC : Sort.Direction.DESC;
                    // fall back to id as "natural order"
                    final String sortProperty = sortOrder.isEmpty() ? "id" : sortOrder.get(0).getSorted();
                    if (startPage != endPage) {
                        List<PersonModel> page0 = personRepository.findByNameLikeIgnoreCase(likeFilter, PageRequest.of(startPage, limit, sortDirection, sortProperty));
                        page0 = page0.subList(offset % limit, page0.size());
                        List<PersonModel> page1 = personRepository.findByNameLikeIgnoreCase(likeFilter, PageRequest.of(endPage, limit, sortDirection, sortProperty));
                        page1 = page1.subList(0, limit - page0.size());
                        List<PersonModel> result = new ArrayList<>(page0);
                        result.addAll(page1);
                        return result.stream();
                    } else {
                        return personRepository.findByNameLikeIgnoreCase(likeFilter, PageRequest.of(startPage, limit, sortDirection, sortProperty)).stream();
                    }
                },
                // count fetching strategy
                () -> (int) personRepository.countByNameLikeIgnoreCase(likeFilter)
        );
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

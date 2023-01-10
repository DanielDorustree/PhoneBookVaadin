package phonebook.vaadin;

import phonebook.spring.PersonModel;
import java.io.Serializable;

public class PersonModifiedEvent implements Serializable {

    private final PersonModel personModel;

    public PersonModifiedEvent(PersonModel p) {
        this.personModel = p;
    }

}

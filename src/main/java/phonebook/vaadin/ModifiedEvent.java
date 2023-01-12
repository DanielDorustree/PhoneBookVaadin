package phonebook.vaadin;

import phonebook.spring.AddressModel;
import phonebook.spring.PersonModel;
import java.io.Serializable;

public class ModifiedEvent implements Serializable {

    private PersonModel personModel;

    private AddressModel addressModel;

    public ModifiedEvent(PersonModel p) {
        this.personModel = p;
    }

    public ModifiedEvent(AddressModel p) {
        this.addressModel = p;
    }

}

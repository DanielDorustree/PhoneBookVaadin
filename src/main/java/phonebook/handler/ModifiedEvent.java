package phonebook.handler;

import phonebook.model.AddressModel;
import phonebook.model.PersonModel;
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

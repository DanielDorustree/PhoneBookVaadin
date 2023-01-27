/*
 * Copyright (c) 2023 Dorustree private limited. All rights reserved.
 */

package phonebook.handler;

import phonebook.model.AddressModel;
import phonebook.model.PersonModel;

/**
 * <h1>Event Handler</h1>
 */
public class ModifiedEvent
{

    //
    //autowired
    //

    private PersonModel personModel;
    private AddressModel addressModel;

    //
    // operations
    //

    /**
     * Event listener for {@link PersonModel}
     * @param personModel
     */
    public ModifiedEvent(PersonModel personModel) {
        this.personModel = personModel;
    }

    /**
     * Event listener for {@link AddressModel}
     * @param addressModel
     */
    public ModifiedEvent(AddressModel addressModel) {
        this.addressModel = addressModel;
    }
}
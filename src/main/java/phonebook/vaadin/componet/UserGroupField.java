/*
 * Copyright (c) 2019 MedWorxs Inc. All rights reserved.
 */

package phonebook.vaadin.componet;

import com.vaadin.ui.ComboBox;

/**
 * <h1>User Group Field</h1>
 *
 * <p>Combo box for selecting a {@link GroupType}.</p>
 *
 */
public class UserGroupField
    extends ComboBox<GroupType>
{
    //
    // static
    //

    //
    // constructors
    //

    /**
     * Label constructor.
     *
     * @param _label    {@link String}
     */
    public UserGroupField(
        final String _label
    )
    {
        super( _label );
        setItems( GroupType.values() );
    }

    //
    // operations
    //

    //
    // attributes
    //

}
/*
 * Copyright (c) 2023 Dorustree private limited. All rights reserved.
 */

package phonebook.componet;

import com.vaadin.ui.ComboBox;

/**
 * <h1>User Group Field</h1>
 *
 * <p>Combo box for selecting a {@link UserGroupType}.</p>
 *
 */
public class UserGroupField
    extends ComboBox<UserGroupType>
{
    //
    // constructors
    //

    /**
     * Set up the label and items {@link UserGroupType} in combo box
     *
     * @param _label
     *      {@link String}
     */
    public UserGroupField(
        final String _label
    )
    {
        super( _label );
        setItems( UserGroupType.values() );
    }
}
/*
 * Copyright (c) 2023 Dorustree private limited. All rights reserved.
 */

package phonebook.componet;

/**
 * <h1>User Group Type</h1>
 *
 * <p>Enumeration of UserGroupField.</p>
 *
 */
public enum UserGroupType
{
	//
	// enum
	//
	NONE(
		"None"
	)
	, FRIEND(
		"Friend"
	)
	, FAMILY(
		"Family"
	)
	,OTHER(
		"Other"
	)
	;

	//
	// constructors
	//

	/**
	 * Default constructor for {@link UserGroupType}.
	 *
	 * @param _text
	 *    {@link String} descriptive text
	 */
	UserGroupType(
		final String _text
	)
	{
		text = _text;
	}

    //
    // operations
    //

	/**
	 * Setup toString() to return text
	 * @return  text
     *      {@link String}
	 */
	@Override
	public String toString()
	{
		return text;
	}

	//
	// attributes
	//
	@lombok.Getter
	private final String text;
}
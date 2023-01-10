/*
 * Copyright (c) 2019 MedWorxs Inc. All rights reserved.
 */

package phonebook.vaadin.componet;


import org.hibernate.ObjectNotFoundException;

/**
 * <h1>Special Purpose Type</h1>
 *
 * <p>Enumeration of transaction types.</p>
 *
 */
public enum GroupType
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
	// static
	//

	//
	// constructors
	//

	//
	// operations
	//
	/**
	 * Default constructor for {@link GroupType}.
	 *
	 * @param _text
	 *    {@link String} descriptive text
	 */
	GroupType(
		final String _text
	)
	{
		text = _text;
	}

	/**
	 * {@inheritDoc }
	 *
	 * @see Enum#toString()
	 */
	@Override
	public String toString()
	{
		return text;
	}

	/**
	 * Gets a version of the enum identifier in a human friendly format.
	 *
	 * @return {@code String} - a human friendly version of the identifier
	 */
//    public String getDisplayName()
//    {
//        return StringUtility.staticFinalToHumanFriendly( name() );
//    }

	public static GroupType valueByDisplay(
		final String _target
	)
		throws ObjectNotFoundException
	{
		for (GroupType groupType : GroupType.values())
		{
			if (groupType.getText().equals(_target))
			{
				return groupType;
			}
		}

		throw new ObjectNotFoundException(
			GroupType.class
			, _target
		);
	}

	//
	// attributes
	//
	@lombok.Getter
	private final String text;
}
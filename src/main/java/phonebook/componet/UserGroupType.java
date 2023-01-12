/*
 * Copyright (c) 2019 MedWorxs Inc. All rights reserved.
 */

package phonebook.componet;


import org.hibernate.ObjectNotFoundException;

/**
 * <h1>Special Purpose Type</h1>
 *
 * <p>Enumeration of transaction types.</p>
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
	// static
	//

	//
	// constructors
	//

	//
	// operations
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

	public static UserGroupType valueByDisplay(
		final String _target
	)
		throws ObjectNotFoundException
	{
		for (UserGroupType userGroupType : UserGroupType.values())
		{
			if (userGroupType.getText().equals(_target))
			{
				return userGroupType;
			}
		}

		throw new ObjectNotFoundException(
			UserGroupType.class
			, _target
		);
	}

	//
	// attributes
	//
	@lombok.Getter
	private final String text;
}
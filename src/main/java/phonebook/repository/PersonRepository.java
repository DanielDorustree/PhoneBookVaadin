/*
 * Copyright (c) 2023 Dorustree private limited. All rights reserved.
 */

package phonebook.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import phonebook.model.PersonModel;

/**
 * Person Repository for phonebook crud application.
 *      {@link PersonModel} entity
 */
public interface PersonRepository extends JpaRepository<PersonModel, Long>
{

    //
    // Derived Query
    //
    List<PersonModel> findByNameLikeIgnoreCase(String nameFilter);
}
/*
 * Copyright (c) 2023 Dorustree private limited. All rights reserved.
 */

package phonebook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import phonebook.model.AddressModel;

/**
 * Address Repository for phonebook crud application.
 *      {@link AddressModel} entity
 */
public interface AddressRepository extends JpaRepository<AddressModel, Long>
{
}
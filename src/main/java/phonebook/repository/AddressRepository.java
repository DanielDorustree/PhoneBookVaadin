
package phonebook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import phonebook.model.AddressModel;

/**
 * JpaRepository for phonebook crud application.
 */
public interface AddressRepository extends JpaRepository<AddressModel, Long> {
}


package phonebook.spring;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JpaRepository for phonebook crud application.
 */
public interface AddressRepository extends JpaRepository<AddressModel, Long> {
}

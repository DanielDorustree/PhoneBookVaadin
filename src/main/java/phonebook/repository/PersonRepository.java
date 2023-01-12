
package phonebook.repository;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import phonebook.model.PersonModel;

/**
 * JpaRepository for phonebook crud application.
 */
public interface PersonRepository extends JpaRepository<PersonModel, Long> {
    List<PersonModel> findByNameLikeIgnoreCase(String nameFilter);
}

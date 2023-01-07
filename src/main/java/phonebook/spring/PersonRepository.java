
package phonebook.spring;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JpaRepository for phonebook crud application.
 */
public interface PersonRepository extends JpaRepository<PersonModel, Long> {
    
    /* A version to fetch List instead of Page to avoid extra count query. */
    List<PersonModel> findAllBy(Pageable pageable);
    
    List<PersonModel> findByNameLikeIgnoreCase(String nameFilter);
    
    // For lazy loading and filtering
    List<PersonModel> findByNameLikeIgnoreCase(String nameFilter, Pageable pageable);
    
    long countByNameLikeIgnoreCase(String nameFilter);

}
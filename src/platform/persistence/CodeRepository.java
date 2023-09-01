package platform.persistence;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import platform.model.Code;

import java.util.List;
import java.util.Optional;

public interface CodeRepository extends JpaRepository<Code, String> {

    @Query("SELECT c FROM Code c WHERE c.viewsAreRestricted = false AND c.viewTimeIsRestricted = false")
    List<Code> findPublicSnippetsByOrderByDateDesc(Sort sort);
    Optional<Code> findById(String codeId);
    boolean existsById(String codeId);


}

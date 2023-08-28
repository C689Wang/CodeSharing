package platform.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import platform.model.Code;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CodeRepository extends JpaRepository<Code, String> {
    List<Code> findByOrderByDateDesc();
    Optional<Code> findByCodeId(String codeId);
    boolean existsByCodeId(String codeId);

}

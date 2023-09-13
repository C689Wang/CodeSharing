package platform.businesslayer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import platform.model.Code;
import platform.persistence.CodeRepository;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CodeService {
    private final CodeRepository repository;

    @Autowired
    public CodeService(CodeRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Code viewCodeSnippetById(String id) {
        Optional<Code> codeSnippet = repository.findById(id);
        if (codeSnippet.isPresent()) {
            Code retrievedCode = codeSnippet.get();
            if (retrievedCode.getViewsAreRestricted()) {
                if (retrievedCode.getViews() > 0) {
                    retrievedCode.setViews(retrievedCode.getViews() - 1);
                } else {
                    deleteCode(id);
                    return null;
                }
            }
            if (retrievedCode.getViewTimeIsRestricted()) {
                Duration duration = Duration.between(retrievedCode.getDate(), LocalDateTime.now());
                long secondsDifference = duration.getSeconds();
                if (secondsDifference > retrievedCode.getTimeRestriction()) {
                    deleteCode(id);
                    return null;
                } else {
                    retrievedCode.setTime(retrievedCode.getTimeRestriction() - (int) secondsDifference);
                }
            }
            return retrievedCode;
        } else {
            return null;
        }
    }

    public List<Code> findLatestCodeSnippets() {
        return repository.findPublicSnippetsByOrderByDateDesc(PageRequest.of(0, 10,
                Sort.by("date").descending()));
    }

    public boolean codeExists(String codeId) {
        return repository.existsById(codeId);
    }

    public void saveCode(Code code) {
        repository.save(code);
    }

    public void deleteCode(String id) {
        repository.deleteById(id);
    }

}

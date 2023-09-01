package platform.businesslayer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import platform.model.Code;
import platform.persistence.CodeRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CodeService {
    private final CodeRepository repository;

    @Autowired
    public CodeService(CodeRepository repository) {
        this.repository = repository;
    }

    public Code findCodeById(String id) {
        Optional<Code> codeSnippet = repository.findById(id);
        return codeSnippet.orElse(null);
    }

    public List<Code> findCodeSnippets() {
        return repository.findPublicSnippetsByOrderByDateDesc(Sort.by(Sort.Direction.DESC, "date"));
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

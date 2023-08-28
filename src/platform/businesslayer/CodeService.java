package platform.businesslayer;

import org.springframework.beans.factory.annotation.Autowired;
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

    public Code findCodeById(Long id) {
        Optional<Code> codeSnippet = repository.findByCodeId(id);
        return codeSnippet.orElse(null);
    }

    public List<Code> findCodeSnippets() {
        return repository.findByDate();
    }

    public Code saveCode(Code code) {
        return repository.save(code);
    }
}

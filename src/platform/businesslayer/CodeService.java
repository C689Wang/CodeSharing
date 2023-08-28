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

    public Code findCodeById(String id) {
        Optional<Code> codeSnippet = repository.findByCodeId(id);
        return codeSnippet.orElse(new Code());
    }

    public List<Code> findCodeSnippets() {
        return repository.findByOrderByDateDesc();
    }

    public boolean codeExists(String codeId) {
        return repository.existsByCodeId(codeId);
    }


    public Code saveCode(Code code) {
        return repository.save(code);
    }
}

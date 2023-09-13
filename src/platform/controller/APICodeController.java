package platform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import platform.businesslayer.CodeService;
import platform.model.Code;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@RestController
public class APICodeController {
    @Autowired
    CodeService codeService;

    @GetMapping("/api/code/{id}")
    public ResponseEntity<Code> getAPICode(@PathVariable String id) {
        Code retrievedCode = codeService.viewCodeSnippetById(id);
        if (retrievedCode == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(retrievedCode);
    }

    @PostMapping(value = "/api/code/new")
    public HashMap<?, ?> publishNewCode(
            @RequestBody Code newCode
    ) {
        Code code =  new Code();
        code.setCode(newCode.getCode());
        code.setDate(LocalDateTime.now());
        UUID uuid = UUID.randomUUID();
        code.setId(uuid.toString());
        code.initializeViews(newCode.getViews());
        code.initializeTime(newCode.getTime());
        codeService.saveCode(code);
        HashMap<String, String> idResponse = new HashMap<>();
        idResponse.put("id", uuid.toString());
        return idResponse;
    }

    @GetMapping("/api/code/latest")
    public List<Code> getAPICode() {
        return codeService.findLatestCodeSnippets();
    }
}

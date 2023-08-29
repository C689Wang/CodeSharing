package platform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import platform.businesslayer.CodeService;
import platform.model.Code;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@RestController
public class CodeController {
    @Autowired
    CodeService codeService;

    @GetMapping(value="/code/{id}", produces="text/html")
    public ModelAndView getPage(@PathVariable String id) {
        ModelAndView modelAndView = new ModelAndView();
        if ("latest".equals(id)) {
            List<Code> latestCodes = codeService.findCodeSnippets();
//            Collections.sort(latestCodes, Comparator.comparing(Code::getDate).reversed());
//
//            // Keep only the 10 latest objects
//            int limit = Math.min(latestCodes.size(), 10);
//            List<Code> sortedCodes = latestCodes.subList(0, limit);
            modelAndView.setViewName("latestCodeView");
            modelAndView.addObject("codeSnippets", latestCodes);
            return modelAndView;
        }
        if (codeService.codeExists(id)) {
            modelAndView.setViewName("codeView");
            Code code = codeService.findCodeById(id);
            modelAndView.addObject("date", code.getReadableDate());
            modelAndView.addObject("code", code.getCode());
        } else {
            modelAndView.setViewName("error");
        }
        return modelAndView;
    }

    @GetMapping(value="/code/new", produces = "text/html")
    public ModelAndView getCodeSubmit() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("submitForm");
        return modelAndView;
    }

    @GetMapping("/api/code/{id}")
    public Code getAPICode(@PathVariable String id) {

        return codeService.findCodeById(id);
    }

    @GetMapping("/api/code/latest")
    public List<Code> getAPICode() {
        List<Code> latestCodes = codeService.findCodeSnippets();
//        Collections.sort(latestCodes, Comparator.comparing(Code::getDate).reversed());
//
//        // Keep only the 10 latest objects
//        int limit = Math.min(latestCodes.size(), 10);
//        List<Code> sortedCodes = latestCodes.subList(0, limit);

        return latestCodes;
    }

    @PostMapping(value = "/api/code/new")
    public HashMap<?, ?> publishNewCode(
            @RequestBody Code newCode
    ) {
        Code code =  new Code();
        code.setCode(newCode.getCode());
        code.setDate(LocalDateTime.now());
        UUID uuid = UUID.randomUUID();
        code.setCodeId(uuid.toString());
        codeService.saveCode(code);
        HashMap<String, String> idResponse = new HashMap<>();
        idResponse.put("id", uuid.toString());
        return idResponse;
    }
}

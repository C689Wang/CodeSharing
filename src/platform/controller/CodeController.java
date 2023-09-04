package platform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import platform.businesslayer.CodeService;
import platform.model.Code;

import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@RestController
public class CodeController {
    @Autowired
    CodeService codeService;

    @GetMapping(value="/code/{id}", produces="text/html")
    public ModelAndView getPage(@PathVariable String id, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView();
        if ("latest".equals(id)) {
            List<Code> latestCodes = codeService.findCodeSnippets();
            modelAndView.setViewName("latestCodeView");
            modelAndView.addObject("codeSnippets", latestCodes);
            return modelAndView;
        }
        if (codeService.codeExists(id)) {
            Code code = codeService.findCodeById(id);
            // In case the search has gone wrong
            if (code == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                modelAndView.setViewName("error");
            } else {
                if (code.getViewsAreRestricted()) {
                    if (code.getViews() > 0) {
                        code.setViews(code.getViews() - 1);
                        codeService.saveCode(code);
                    } else {
                        codeService.deleteCode(id);
                        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        modelAndView.setViewName("error");
                        return modelAndView;
                    }
                }
                if (code.getViewTimeIsRestricted()) {
                    Duration duration = Duration.between(code.getDate(), LocalDateTime.now());
                    long secondsDifference = duration.getSeconds();
                    if (secondsDifference > code.getTimeRestriction()) {
                        codeService.deleteCode(id);
                        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        modelAndView.setViewName("error");
                        return modelAndView;
                    } else {
                        code.setTime(code.getTimeRestriction() - (int) secondsDifference);
                        codeService.saveCode(code);
                    }
                }
                modelAndView.setViewName("codeView");
                modelAndView.addObject("date", code.getReadableDate());
                modelAndView.addObject("code", code.getCode());
                modelAndView.addObject("viewsAreRestricted", code.getViewsAreRestricted());
                modelAndView.addObject("viewTimeIsRestricted", code.getViewTimeIsRestricted());
                modelAndView.addObject("viewsLeft", code.getViews());
                modelAndView.addObject("timeLeft", code.getTime());
            }
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
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
    public ResponseEntity<Code> getAPICode(@PathVariable String id) {
        Code retrievedCode = codeService.findCodeById(id);
        if (retrievedCode == null) {
            return ResponseEntity.notFound().build();
        } else {
            if (retrievedCode.getViewsAreRestricted()) {
                if (retrievedCode.getViews() > 0) {
                    retrievedCode.setViews(retrievedCode.getViews() - 1);
                    codeService.saveCode(retrievedCode);
                } else {
                    codeService.deleteCode(id);
                    return ResponseEntity.notFound().build();
                }
            }
            if (retrievedCode.getViewTimeIsRestricted()) {
                Duration duration = Duration.between(retrievedCode.getDate(), LocalDateTime.now());
                long secondsDifference = duration.getSeconds();
                if (secondsDifference > retrievedCode.getTimeRestriction()) {
                    codeService.deleteCode(id);
                    return ResponseEntity.notFound().build();
                } else {
                    retrievedCode.setTime(retrievedCode.getTimeRestriction() - (int) secondsDifference);
                    codeService.saveCode(retrievedCode);
                }
            }
        }
        return ResponseEntity.ok().body(retrievedCode);
    }

    @GetMapping("/api/code/latest")
    public List<Code> getAPICode() {
        return codeService.findCodeSnippets();
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
        int maxViews = newCode.getViews();
        if (maxViews > 0) {
            code.setViews(maxViews);
            code.setViewsAreRestricted(true);
        } else {
            code.setViews(0);
            code.setViewsAreRestricted(false);
        }
        int maxViewTime = newCode.getTime();
        if (maxViewTime > 0) {
            code.setTime(maxViewTime);
            code.setTimeRestriction(maxViewTime);
            code.setViewTimeIsRestricted(true);
        } else {
            code.setTime(0);
            code.setTimeRestriction(0);
            code.setViewTimeIsRestricted(false);
        }
        codeService.saveCode(code);
        HashMap<String, String> idResponse = new HashMap<>();
        idResponse.put("id", uuid.toString());
        return idResponse;
    }
}

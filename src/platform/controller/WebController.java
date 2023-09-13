package platform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;
import platform.businesslayer.CodeService;
import platform.model.Code;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class WebController {

    @Autowired
    CodeService codeService;

    @GetMapping(value="/code/{id}", produces="text/html")
    public ModelAndView getPage(@PathVariable String id, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView();
        if ("latest".equals(id)) {
            List<Code> latestCodes = codeService.findLatestCodeSnippets();
            modelAndView.setViewName("latestCodeView");
            modelAndView.addObject("codeSnippets", latestCodes);
            return modelAndView;
        }
        if (codeService.codeExists(id)) {
            Code code = codeService.viewCodeSnippetById(id);
            // In case the search has gone wrong
            if (code == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                modelAndView.setViewName("error");
            } else {
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

}

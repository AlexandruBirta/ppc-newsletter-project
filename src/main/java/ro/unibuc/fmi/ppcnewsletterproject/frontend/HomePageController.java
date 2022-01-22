package ro.unibuc.fmi.ppcnewsletterproject.frontend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomePageController {
    @RequestMapping(value = "/")
    public String index() {
        return "index";
    }
}

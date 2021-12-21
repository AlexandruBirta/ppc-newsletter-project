package ro.unibuc.fmi.ppcnewsletterproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.unibuc.fmi.ppcnewsletterproject.model.Newsletter;
import ro.unibuc.fmi.ppcnewsletterproject.service.NewsletterService;

@RestController
@RequestMapping(path = "v1")
public class NewsletterController {

    private final NewsletterService newsletterService;

    @Autowired
    public NewsletterController(NewsletterService newsletterService) {
        this.newsletterService = newsletterService;
    }

    @PostMapping(path = "/newsletters")
    public void createNewsletter(@RequestBody Newsletter newsletter) {
        newsletterService.createNewsletter(newsletter);
    }
}

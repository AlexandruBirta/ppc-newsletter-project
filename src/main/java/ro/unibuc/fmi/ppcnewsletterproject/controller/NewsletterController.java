package ro.unibuc.fmi.ppcnewsletterproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.bind.annotation.*;
import ro.unibuc.fmi.ppcnewsletterproject.model.AccountNewsletter;
import ro.unibuc.fmi.ppcnewsletterproject.model.Newsletter;
import ro.unibuc.fmi.ppcnewsletterproject.service.NewsletterService;

import java.util.Objects;
import java.util.Properties;

@RestController
@RequestMapping(path = "/v1", method = {RequestMethod.POST, RequestMethod.GET})
public class NewsletterController {

    @Autowired
    private Environment environment;

    private final NewsletterService newsletterService;

    @Autowired
    public NewsletterController(NewsletterService newsletterService) {
        this.newsletterService = newsletterService;
    }

    @PostMapping(path = "/newsletters")
    public void createNewsletter(@RequestBody Newsletter newsletter) {
        newsletterService.createNewsletter(newsletter);
    }

    //TODO: Add required params for the method call
    public void sendNewsletter(AccountNewsletter accountNewsletter){

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(accountNewsletter.getAccount().getEmail());

        msg.setSubject(accountNewsletter.getNewsletter().getName());
        msg.setText(accountNewsletter.getNewsletter().getType().toString());

        getJavaMailSender().send(msg);
    }

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(environment.getProperty("spring.mail.host"));
        mailSender.setPort(Integer.parseInt(Objects.requireNonNull(environment.getProperty("spring.mail.port"))));

        mailSender.setUsername(environment.getProperty("spring.mail.username"));

        mailSender.setPassword(environment.getProperty("spring.mail.password"));

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }

}

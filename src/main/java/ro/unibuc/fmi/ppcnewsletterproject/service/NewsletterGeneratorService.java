package ro.unibuc.fmi.ppcnewsletterproject.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import ro.unibuc.fmi.ppcnewsletterproject.exception.ApiException;
import ro.unibuc.fmi.ppcnewsletterproject.exception.ExceptionStatus;
import ro.unibuc.fmi.ppcnewsletterproject.model.AccountNewsletter;
import ro.unibuc.fmi.ppcnewsletterproject.service.newslettergenerator.NewsletterGeneratorFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Objects;
import java.util.Properties;

@Slf4j
@Service
public class NewsletterGeneratorService {

    private final NewsletterGeneratorFactory newsletterGeneratorFactory;
    private final Environment environment;

    @Autowired
    public NewsletterGeneratorService(NewsletterGeneratorFactory newsletterGeneratorFactory, Environment environment) {
        this.newsletterGeneratorFactory = newsletterGeneratorFactory;
        this.environment = environment;
    }

    public void sendEmail(AccountNewsletter accountNewsletter) {

        try {

            String content = newsletterGeneratorFactory.make(accountNewsletter).getEmailHTML();
            sendNewsletter(accountNewsletter, content);

        } catch (Exception e) {
            throw new ApiException(ExceptionStatus.UNEXPECTED_EXCEPTION, e.getClass().getSimpleName());
        }

    }

    public void sendNewsletter(AccountNewsletter accountNewsletter, String content) {

        try {

            MimeMessage msg = getJavaMailSender().createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(msg, true);

            helper.setTo(accountNewsletter.getAccount().getEmail());
            helper.setSubject("Hello " + accountNewsletter.getAccount().getFirstName() + " " + accountNewsletter.getAccount().getLastName() + "! Here is your " + accountNewsletter.getNewsletter().getName() + " newsletter!");
            helper.setText(content, true);

            getJavaMailSender().send(msg);

        } catch (MessagingException e) {
            throw new ApiException(ExceptionStatus.UNEXPECTED_EXCEPTION, e.getClass().getSimpleName());
        }



    }


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

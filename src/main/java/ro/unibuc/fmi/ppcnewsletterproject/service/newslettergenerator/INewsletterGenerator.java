package ro.unibuc.fmi.ppcnewsletterproject.service.newslettergenerator;

import ro.unibuc.fmi.ppcnewsletterproject.exception.GenerateEmailException;

import java.io.IOException;

public interface INewsletterGenerator {
    String getEmailHTML() throws GenerateEmailException, IOException;
}

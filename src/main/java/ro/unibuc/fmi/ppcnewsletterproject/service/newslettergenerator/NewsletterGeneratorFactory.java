package ro.unibuc.fmi.ppcnewsletterproject.service.newslettergenerator;

import org.springframework.stereotype.Service;
import ro.unibuc.fmi.ppcnewsletterproject.model.Account;
import ro.unibuc.fmi.ppcnewsletterproject.model.AccountNewsletter;
import ro.unibuc.fmi.ppcnewsletterproject.service.newslettergenerator.generators.BaconIpsumNewsletterGenerator;
import ro.unibuc.fmi.ppcnewsletterproject.service.newslettergenerator.generators.CatPhotoNewsletterGenerator;
import ro.unibuc.fmi.ppcnewsletterproject.service.newslettergenerator.generators.RandomArticleNewsletterGenerator;

@Service
public class NewsletterGeneratorFactory {
    public INewsletterGenerator make(AccountNewsletter accountNewsletter) throws Exception {
        INewsletterGenerator generator;
        Account account = accountNewsletter.getAccount();

        switch (accountNewsletter.getNewsletter().getType()) {
            case BACON_IPSUM -> generator = new BaconIpsumNewsletterGenerator(account);
            case CAT_PHOTO -> generator = new CatPhotoNewsletterGenerator(account);
            case WIKIPEDIA_ARTICLE -> generator = new RandomArticleNewsletterGenerator(account);

            default -> throw new Exception("Not implemented");
        }

        return generator;
    }
}

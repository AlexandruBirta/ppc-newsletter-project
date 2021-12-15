package ro.unibuc.fmi.ppcnewsletterproject.service.newslettergenerator;

import ro.unibuc.fmi.ppcnewsletterproject.model.Account;
import ro.unibuc.fmi.ppcnewsletterproject.model.Newsletter;

public class NewsletterGeneratorFactory
{
    public INewsletterGenerator makeFromModel(Newsletter newsletter, Account account) throws Exception {
        INewsletterGenerator generator = null;

        switch (newsletter.getType()) {
            case BACON_IPSUM -> generator = new BaconIpsumNewsletterGenerator(account);
            case CAT_PHOTO, WIKIPEDIA_ARTICLE -> throw new Exception("Not implemented");
        }

        return generator;
    }
}

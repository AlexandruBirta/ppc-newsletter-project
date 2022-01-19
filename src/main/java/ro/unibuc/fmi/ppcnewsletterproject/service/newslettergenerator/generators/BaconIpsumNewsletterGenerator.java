package ro.unibuc.fmi.ppcnewsletterproject.service.newslettergenerator.generators;

import ro.unibuc.fmi.ppcnewsletterproject.exception.GenerateEmailException;
import ro.unibuc.fmi.ppcnewsletterproject.model.Account;
import ro.unibuc.fmi.ppcnewsletterproject.service.newslettergenerator.BaseNewsletterGenerator;
import ro.unibuc.fmi.ppcnewsletterproject.service.newslettergenerator.INewsletterGenerator;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class BaconIpsumNewsletterGenerator extends BaseNewsletterGenerator implements INewsletterGenerator {
    private final HttpClient client;

    public BaconIpsumNewsletterGenerator(Account account) {
        super(account);

        client = HttpClient.newHttpClient();
    }

    @Override
    protected Path getTemplatePath() throws IOException {
        return Paths.get(new File(".").getCanonicalPath(), "src", "main", "resources", "templates", "email", "bacon_ipsum_newsletter.html");
    }

    @Override
    protected Map<String, String> generateContent() throws GenerateEmailException {
        Map<String, String> parameters = new HashMap<>();

        parameters.put("firstName", account.getFirstName());
        parameters.put("lastName", account.getLastName());

        try {
            parameters.put("content", retrieveRandomBacon());
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new GenerateEmailException(e);
        }

        return parameters;
    }

    private String retrieveRandomBacon() throws URISyntaxException, IOException, InterruptedException {
        HttpRequest request = HttpRequest
                .newBuilder(new URI("https://baconipsum.com/api/?type=all-meat&format=text&sentences=3"))
                .GET()
                .build();

        return client.send(request, BodyHandlers.ofString()).body();
    }
}

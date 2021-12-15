package ro.unibuc.fmi.ppcnewsletterproject.service.newslettergenerator;

import ro.unibuc.fmi.ppcnewsletterproject.exception.GenerateEmailException;
import ro.unibuc.fmi.ppcnewsletterproject.model.Account;

import java.io.IOException;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class BaconIpsumNewsletterGenerator extends BaseNewsletterGenerator implements INewsletterGenerator
{
    public BaconIpsumNewsletterGenerator(Account account) {
        super(account);
    }

    protected Path getTemplatePath() throws IOException {
        return Paths.get(new File(".").getCanonicalPath(), "src", "main", "resources", "templates", "email", "bacon_ipsum_newsletter.html");
    }

    protected Map<String, String> getTemplateParameters() throws GenerateEmailException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("firstName", account.getFirstName());
        parameters.put("lastName", account.getLastName());
        parameters.put("content", getBaconIpsum());

        return parameters;
    }

    private String getBaconIpsum() throws GenerateEmailException {
        try {
            return sendRequest();
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new GenerateEmailException(e);
        }
    }

    private String sendRequest() throws URISyntaxException, IOException, InterruptedException {
        URI uri = new URI("https://baconipsum.com/api/?type=all-meat&format=text&sentences=1");

        HttpRequest request = HttpRequest
                .newBuilder(uri)
                .GET()
                .build();

        HttpClient client = HttpClient.newHttpClient();

        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        return response.body();
    }
}

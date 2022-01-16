package ro.unibuc.fmi.ppcnewsletterproject.service.newslettergenerator.generators;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RandomArticleNewsletterGenerator extends BaseNewsletterGenerator implements INewsletterGenerator {
    private final HttpClient client;

    public RandomArticleNewsletterGenerator(Account account) {
        super(account);

        client = HttpClient.newHttpClient();
    }

    @Override
    protected Path getTemplatePath() throws IOException {
        return Paths.get(new File(".").getCanonicalPath(), "src", "main", "resources", "templates", "email", "article_newsletter.html");
    }

    @Override
    protected Map<String, String> generateContent() throws GenerateEmailException {
        Map<String, String> parameters = new HashMap<>();

        parameters.put("firstName", account.getFirstName());
        parameters.put("lastName", account.getLastName());

        try {
            JsonNode article = retrieveArticle();

            parameters.put("title", article.get("title").asText());
            parameters.put("body", article.get("extract_html").asText());

        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new GenerateEmailException(e);
        }

        return parameters;
    }

    private JsonNode retrieveArticle() throws URISyntaxException, IOException, InterruptedException {
        URI baseUri = new URI("https://en.wikipedia.org/api/rest_v1/page/random/summary");
        HttpRequest request;
        HttpResponse<String> response;

        request = HttpRequest.newBuilder(baseUri).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // follow redirect
        Optional<String> redirect = response.headers().firstValue("location");

        if (response.statusCode() != 303 || redirect.isEmpty()) {
            throw new RuntimeException("Something went wrong");
        }

        request = HttpRequest
                .newBuilder(baseUri.resolve(redirect.get()))
                .GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.readTree(response.body());
    }
}

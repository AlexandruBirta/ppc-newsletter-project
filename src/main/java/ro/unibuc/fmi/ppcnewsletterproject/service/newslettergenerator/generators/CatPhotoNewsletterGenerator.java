package ro.unibuc.fmi.ppcnewsletterproject.service.newslettergenerator.generators;

import ro.unibuc.fmi.ppcnewsletterproject.exception.GenerateEmailException;
import ro.unibuc.fmi.ppcnewsletterproject.model.Account;
import ro.unibuc.fmi.ppcnewsletterproject.service.newslettergenerator.BaseNewsletterGenerator;
import ro.unibuc.fmi.ppcnewsletterproject.service.newslettergenerator.EmailContent;
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
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CatPhotoNewsletterGenerator extends BaseNewsletterGenerator implements INewsletterGenerator {
    private final HttpClient client;

    public CatPhotoNewsletterGenerator(Account account) {
        super(account);

        client = HttpClient.newHttpClient();
    }

    @Override
    protected Path getTemplatePath() throws IOException {
        return Paths.get(new File(".").getCanonicalPath(), "src", "main", "resources", "templates", "email", "cat_picture_newsletter.html");
    }

    @Override
    protected Map<String, String> generateContent() {
        Map<String, String> parameters = new HashMap<>();

        parameters.put("firstName", account.getFirstName());
        parameters.put("lastName", account.getLastName());

        return parameters;
    }

    private HttpResponse<byte[]> retrieveImage() throws URISyntaxException, IOException, InterruptedException {
        HttpRequest request = HttpRequest
                .newBuilder(new URI("https://cataas.com/cat"))
                .GET()
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofByteArray());
    }

    private String retrieveCatImageAsBase64() throws URISyntaxException, IOException, InterruptedException {
        HttpRequest request = HttpRequest
                .newBuilder(new URI("https://cataas.com/cat"))
                .GET()
                .build();

        HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());

        String imageData = Base64.getEncoder().encodeToString(response.body());
        Optional<String> mimeType = response.headers().firstValue("content-type");

        if (mimeType.isEmpty()) {
            return imageData;
        } else {
            return "data:" + mimeType.get() + ";base64," + imageData;
        }
    }

    @Override
    public EmailContent getEmailHTML() throws GenerateEmailException, IOException {
        EmailContent content = new EmailContent();
        content.setHtml(renderTemplate());

        try {
            content.setAttachmentContentId("1");

            HttpResponse<byte[]> imageResponse = retrieveImage();

            content.setAttachment(imageResponse.body());
            imageResponse.headers().firstValue("content-type").ifPresent(content::setMimeType);

        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new GenerateEmailException(e);
        }

        return content;
    }
}

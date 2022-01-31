package ro.unibuc.fmi.ppcnewsletterproject.service.newslettergenerator;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EmailContent {
    private String html;
    private byte[] attachment;
    private String attachmentContentId;
    private String mimeType;
}

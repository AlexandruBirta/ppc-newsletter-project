package ro.unibuc.fmi.ppcnewsletterproject.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AccountDTO {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private LocalDateTime insertedDate;

    private LocalDateTime updatedDate;
}

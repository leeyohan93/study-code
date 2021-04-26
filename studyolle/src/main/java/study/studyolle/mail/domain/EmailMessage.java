package study.studyolle.mail.domain;

import lombok.Builder;
import lombok.Data;

@Data
public class EmailMessage {

    private final String to;

    private final String subject;

    private final String message;

    @Builder
    public EmailMessage(String to, String subject, String message) {
        this.to = to;
        this.subject = subject;
        this.message = message;
    }
}

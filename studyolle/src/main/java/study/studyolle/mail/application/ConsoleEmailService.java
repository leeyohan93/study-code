package study.studyolle.mail.application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import study.studyolle.mail.domain.EmailMessage;

@Profile("local")
@Component
@Slf4j
public class ConsoleEmailService implements EmailService {

    @Override
    public void sendEmail(EmailMessage emailMessage) {
        log.info("send email: {}", emailMessage.getMessage());
    }
}

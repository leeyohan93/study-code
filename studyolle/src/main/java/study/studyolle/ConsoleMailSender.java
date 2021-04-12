package study.studyolle;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;
import java.io.InputStream;


@Profile("local")
@Component
@Slf4j
public class ConsoleMailSender implements JavaMailSender {
    @Override
    public MimeMessage createMimeMessage() {
        return null;
    }

    @Override
    public MimeMessage createMimeMessage(final InputStream contentStream) throws MailException {
        return null;
    }

    @Override
    public void send(final MimeMessage mimeMessage) throws MailException {

    }

    @Override
    public void send(final MimeMessage... mimeMessages) throws MailException {

    }

    @Override
    public void send(final MimeMessagePreparator mimeMessagePreparator) throws MailException {

    }

    @Override
    public void send(final MimeMessagePreparator... mimeMessagePreparators) throws MailException {

    }

    @Override
    public void send(final SimpleMailMessage simpleMessage) throws MailException {
        log.info(simpleMessage.getText());
    }

    @Override
    public void send(final SimpleMailMessage... simpleMessages) throws MailException {

    }
}

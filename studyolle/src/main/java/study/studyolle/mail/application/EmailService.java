package study.studyolle.mail.application;

import study.studyolle.mail.domain.EmailMessage;

public interface EmailService {

    void sendEmail(EmailMessage emailMessage);
}

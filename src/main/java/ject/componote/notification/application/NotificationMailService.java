package ject.componote.notification.application;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import ject.componote.notification.dto.NotificationMailRequest;
import ject.componote.notification.template.NotificationMailTemplateProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@Service
@Slf4j
public class NotificationMailService {
    private static final String EMAIL_SENDING_FAIL_LOG_FORMAT = "이메일 발송 실패. [에러 메시지]: {}";

    private final ExecutorService mailExecutor;
    private final JavaMailSender mailSender;
    private final NotificationMailTemplateProvider notificationMailTemplateProvider;

    public NotificationMailService(@Qualifier("notificationMailExecutor") final ExecutorService mailExecutor,
                                   final JavaMailSender mailSender,
                                   final NotificationMailTemplateProvider notificationMailTemplateProvider) {
        this.mailExecutor = mailExecutor;
        this.mailSender = mailSender;
        this.notificationMailTemplateProvider = notificationMailTemplateProvider;
    }

    public void sendMail(final NotificationMailRequest request) {
        final String htmlContent = notificationMailTemplateProvider.createTemplate(request);
        final MimeMessage mimeMessage = createMimeMessage(request.receiverEmail(), htmlContent);
        CompletableFuture.runAsync(() -> {
                    mailSender.send(mimeMessage);
                }, mailExecutor
        ).exceptionally(throwable -> {
            log.error(EMAIL_SENDING_FAIL_LOG_FORMAT, throwable.getMessage());
            return null;
        });
    }

    private MimeMessage createMimeMessage(final String receiverEmail, final String htmlContent) {
        final MimeMessage message = mailSender.createMimeMessage();
        try {
            final MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(receiverEmail);
            helper.setText(htmlContent, true);
            return message;
        } catch (MessagingException e) {
            throw new IllegalArgumentException(e);  // 커스텀 예정
        }
    }
}

package ject.componote.application;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import ject.componote.dto.event.NotificationAlertRequest;
import ject.componote.template.MailTemplateProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailService {
    private static final String EMAIL_SENDING_FAIL_LOG_FORMAT = "이메일 발송 실패. [알림 ID]: {}, [에러 메시지]: {}";

    private final ExecutorService mailExecutor;
    private final JavaMailSender mailSender;
    private final MailTemplateProvider mailTemplateProvider;

    public void sendMail(final NotificationAlertRequest request) {
        final Long notificationId = request.notificationId();
        final String htmlContent = mailTemplateProvider.createVerificationCodeTemplate(request);
        final MimeMessage message = createMimeMessage(request, htmlContent);
        CompletableFuture.runAsync(() -> {
                    mailSender.send(message);
                }, mailExecutor
        ).exceptionally(throwable -> {
            log.error(EMAIL_SENDING_FAIL_LOG_FORMAT, notificationId, throwable.getMessage());
            return null;
        });
    }

    private MimeMessage createMimeMessage(final NotificationAlertRequest request, final String htmlContent) {
        final MimeMessage message = mailSender.createMimeMessage();
        try {
            final MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(request.receiverEmail());
            helper.setText(htmlContent, true);
            return message;
        } catch (MessagingException e) {
            throw new IllegalArgumentException(e);  // 커스텀 예정
        }
    }
}

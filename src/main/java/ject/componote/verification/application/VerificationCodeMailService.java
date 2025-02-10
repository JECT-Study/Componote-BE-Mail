package ject.componote.verification.application;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import ject.componote.verification.dto.VerificationCodeRequest;
import ject.componote.verification.template.VerificationMailTemplateProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@Service
@Slf4j
public class VerificationCodeMailService {
    private static final String EMAIL_SENDING_FAIL_LOG_FORMAT = "이메일 인증 코드 발송 실패. [입력한 이메일]: {}, [메시지]: {}";

    private final ExecutorService mailExecutor;
    private final JavaMailSender mailSender;
    private final VerificationMailTemplateProvider verificationMailTemplateProvider;

    public VerificationCodeMailService(@Qualifier("verificationMailExecutor") final ExecutorService mailExecutor,
                                       final JavaMailSender mailSender,
                                       final VerificationMailTemplateProvider verificationMailTemplateProvider) {
        this.mailExecutor = mailExecutor;
        this.mailSender = mailSender;
        this.verificationMailTemplateProvider = verificationMailTemplateProvider;
    }

    public void sendVerificationCode(final VerificationCodeRequest request) {
        final String email = request.email();
        final String code = request.code();
        final String htmlContent = verificationMailTemplateProvider.createVerificationCodeTemplate(code);
        final MimeMessage message = createMimeMessage(email, htmlContent);
        CompletableFuture.runAsync(() -> {
                    mailSender.send(message);
                }, mailExecutor
        ).exceptionally(throwable -> {
            log.error(EMAIL_SENDING_FAIL_LOG_FORMAT, email, throwable.getMessage());
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
            throw new IllegalArgumentException(e);
        }
    }
}

package ject.componote.verification.consumer;

import ject.componote.verification.application.VerificationCodeMailService;
import ject.componote.verification.dto.VerificationCodeRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class VerificationCodeConsumer {
    private final VerificationCodeMailService verificationCodeMailService;

    @RabbitListener(
            queues = "emailVerifyCreateQueue",
            containerFactory = "rabbitListenerContainerFactory"
    )
    public void consumeVerificationCode(final VerificationCodeRequest request) {
        log.info("인증 코드 이메일 전송 메시지 수신: {}", request);
        verificationCodeMailService.sendVerificationCode(request);
    }
}

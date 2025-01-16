package ject.componote.application;

import ject.componote.dto.event.NotificationAlertRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationConsumer {
    private final MailService mailService;

    // 예외 처리 세분화 예정
    @RabbitListener(
            queues = "emailQueue",
            containerFactory = "rabbitListenerContainerFactory"
    )
    public void consume(final NotificationAlertRequest message) throws IOException {
        try {
            mailService.sendMail(message);
            log.info("[메시지 전송 성공]: {}", message);
        } catch (Exception e) {
            log.error("[메일 전송 실패]: {}, [알림 ID]: {}", e, message.notificationId());
        }
    }
}

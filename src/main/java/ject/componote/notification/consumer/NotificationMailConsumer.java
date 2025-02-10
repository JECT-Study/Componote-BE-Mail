package ject.componote.notification.consumer;

import ject.componote.notification.application.NotificationMailService;
import ject.componote.notification.dto.NotificationMailRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationMailConsumer {
    private final NotificationMailService notificationMailService;

    @RabbitListener(
            queues = "emailQueue",
            containerFactory = "rabbitListenerContainerFactory"
    )
    public void consumeNotification(final NotificationMailRequest request) {
        try {
            notificationMailService.sendMail(request);
            log.info("메시지 전송 성공. [요청]: {}", request);
        } catch (Exception e) {
            log.error("메일 전송 실패. [요청]: {}, [에러 메시지]: {}", request, e.getMessage());
        }
    }
}

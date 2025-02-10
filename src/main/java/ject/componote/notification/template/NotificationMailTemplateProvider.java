package ject.componote.notification.template;

import ject.componote.notification.dto.NotificationMailRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationMailTemplateProvider {
    private static final String RECEIVER_NICKNAME_KEY = "receiverNickname";

    private final TemplateEngine templateEngine;

    public String createTemplate(final NotificationMailRequest request) {
        final Context context = new Context();  // Thread-Safe 하지 않아 매 요청마다 객체 생성
        context.setVariable(RECEIVER_NICKNAME_KEY, request.receiverNickname());
        return templateEngine.process(request.type().getTemplatePath(), context);
    }
}
package ject.componote.dto.event;

import ject.componote.template.MailTemplateType;

public record NotificationAlertRequest(Long notificationId, MailTemplateType type, String senderNickname, String receiverNickname, String receiverEmail) {
}

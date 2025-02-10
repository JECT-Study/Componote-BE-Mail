package ject.componote.notification.dto;

import ject.componote.notification.template.MailTemplateType;

public record NotificationMailRequest(Long notificationId, MailTemplateType type, String senderNickname, String receiverNickname, String receiverEmail) {
}
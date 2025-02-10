package ject.componote.notification.template;

import lombok.Getter;

@Getter
public enum MailTemplateType {
    COMMENT_LIKE("comment-like"),
    ROOT_REPLY("root-reply"),
    NOTICE("notice"),
    NESTED_REPLY("nested-reply");

    private final String templatePath;

    MailTemplateType(final String templatePath) {
        this.templatePath = templatePath;
    }
}

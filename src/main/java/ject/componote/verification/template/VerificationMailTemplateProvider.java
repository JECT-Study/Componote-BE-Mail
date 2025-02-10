package ject.componote.verification.template;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
@RequiredArgsConstructor
public class VerificationMailTemplateProvider {
    private static final String VERIFICATION_CODE_KEY = "verificationCode";
    private static final String EMAIL_VERIFICATION_TEMPLATE = "email-verification";

    private final TemplateEngine templateEngine;

    public String createVerificationCodeTemplate(final String code) {
        final Context context = new Context();  // Thread-Safe 하지 않아 매 요청마다 객체 생성
        context.setVariable(VERIFICATION_CODE_KEY, code);
        return templateEngine.process(EMAIL_VERIFICATION_TEMPLATE, context);
    }
}

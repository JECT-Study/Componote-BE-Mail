package ject.componote.notification.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class NotificationMailConfig {
    /**
     * 알림 메일 전송 스레드 풀 빈 등록 메서드
     * @return 메일 전송 전용 스레드 풀
     */
    @Bean("notificationMailExecutor")
    public ExecutorService mailExecutor() {
        return Executors.newFixedThreadPool(10);
    }
}

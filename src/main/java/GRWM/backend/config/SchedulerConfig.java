package GRWM.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class SchedulerConfig {
    // TaskScheduler 빈 등록
    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        // 스케줄러가 사용할 스레드 개수 설정 (동시 예약 작업 수에 따라 조정)
        scheduler.setPoolSize(10);
        scheduler.setThreadNamePrefix("my-task-scheduler-");
        scheduler.initialize();
        return scheduler;
    }
}

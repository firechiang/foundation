package net.foundation.mfetch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@EnableScheduling
@SpringBootApplication(exclude = { ErrorMvcAutoConfiguration.class })
public class FetchApplication {

    public static void main(String[] args) {
        Thread.currentThread().setUncaughtExceptionHandler((Thread t, Throwable e) -> {
            log.error(t.getId()+"="+t.getName(),e);
        });
        SpringApplication.run(FetchApplication.class,args);
    }
}

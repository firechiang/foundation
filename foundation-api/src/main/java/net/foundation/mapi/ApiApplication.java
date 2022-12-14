package net.foundation.mapi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;

@Slf4j
@SpringBootApplication(exclude = { ErrorMvcAutoConfiguration.class })
public class ApiApplication {

    public static void main(String[] args) {
        Thread.currentThread().setUncaughtExceptionHandler((Thread t, Throwable e) -> {
            log.error(t.getId()+"="+t.getName(),e);
        });
        SpringApplication.run(ApiApplication.class,args);
    }
}

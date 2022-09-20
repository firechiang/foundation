package net.foundation.mflowline;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class FlowlineApplication {

    public static void main(String[] args) {
        Thread.currentThread().setUncaughtExceptionHandler((Thread t, Throwable e) -> {
            log.error(t.getId()+"="+t.getName(),e);
        });
        SpringApplication.run(FlowlineApplication.class,args);
    }
}

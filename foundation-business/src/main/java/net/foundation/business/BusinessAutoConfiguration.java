package net.foundation.business;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "net.foundation.business.service")
public class BusinessAutoConfiguration {

}

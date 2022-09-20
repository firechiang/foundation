package net.foundation.mbusiness;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = {"net.foundation.mbusiness.service.impl","net.foundation.mbusiness.mapper"})
public class BusinessAutoConfiguration {

}

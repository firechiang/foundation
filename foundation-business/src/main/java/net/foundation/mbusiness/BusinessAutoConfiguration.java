package net.foundation.mbusiness;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@MapperScan("net.foundation.mbusiness.mapper*")
@ComponentScan(basePackages = {"net.foundation.mbusiness.service"})
public class BusinessAutoConfiguration {

}

package ba.com.zira.praksa.configuration;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

import ba.com.zira.commons.configuration.BaseApplicationConfiguration;

@EnableEurekaClient
@SpringBootApplication
@ComponentScan(basePackages = { "ba.com.zira.praksa.model", "ba.com.zira.praksa.configuration", "ba.com.zira.praksa.dao",
        "ba.com.zira.praksa.core.impl", "ba.com.zira.praksa.rest", "ba.com.zira.praksa.core.validation", "ba.com.zira.praksa.mapper" })
@EnableFeignClients(basePackages = "ba.com.zira.praksa.core.clients")
public class ApplicationConfiguration extends BaseApplicationConfiguration {

}

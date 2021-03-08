package ba.com.zira.praksa.configuration;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import ba.com.zira.commons.configuration.BaseApplicationConfiguration;

@EnableEurekaClient
@SpringBootApplication
public class ApplicationConfiguration extends BaseApplicationConfiguration {

}

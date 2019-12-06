package ba.com.zira.praksa.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EntityScan(basePackages = { "ba.com.zira.praksa.dao.model" })
public class JPAConfiguration {

}

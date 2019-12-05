package ba.com.zira.praksa.test.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import ba.com.zira.praksa.test.configuration.ApplicationTestConfiguration;
import ba.com.zira.praksa.test.configuration.hibernate.HibernateConfiguration;

@ComponentScan(
        basePackages = { "ba.com.zira.praksa.dao"},
        basePackageClasses = { HibernateConfiguration.class })
@PropertySource(value = { "classpath:test.properties" })
@EnableTransactionManagement
public class ApplicationTestConfiguration {
    
}

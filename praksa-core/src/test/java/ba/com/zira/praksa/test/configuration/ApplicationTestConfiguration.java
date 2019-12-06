package ba.com.zira.praksa.test.configuration;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.apache.commons.configuration.tree.xpath.XPathExpressionEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.io.ClassPathResource;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.model.response.ResponseCode;
import ba.com.zira.commons.validation.RequestValidator;
import ba.com.zira.commons.validation.impl.RequestValidatorImpl;

@Configuration
@ComponentScan(basePackages = {"ba.com.zira.praksa.mapper" })
@EnableAspectJAutoProxy
public class ApplicationTestConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationTestConfiguration.class);
    
    /**
     * Create Bean for Request validation
     * 
     * @return RequestValidator
     * @throws ApiException
     *             ApiException
     */
    @Bean
    public RequestValidator requestValidator() throws ApiException {
        RequestValidatorImpl requestValidator = new RequestValidatorImpl();
        requestValidator.setConfigurations(requiredValidationConfig());
        return requestValidator;
    }

    /**
     * Load XMLs for validation rules.
     * 
     * @return Map<String, XMLConfiguration>
     * @throws ApiException
     */
    private static Map<String, XMLConfiguration> requiredValidationConfig() throws ApiException {
        try {
            final ClassPathResource cpr = new ClassPathResource("requiredValidations.xml");
            final XMLConfiguration config = new XMLConfiguration(cpr.getURL());
            config.setReloadingStrategy(new FileChangedReloadingStrategy());
            config.setExpressionEngine(new XPathExpressionEngine());
            final Map<String, XMLConfiguration> configurations = new HashMap<>();
            configurations.put("default", config);
            return configurations;
        } catch (ConfigurationException | IOException e) {
            LOGGER.error("requiredValidationConfig => {}", e);
            throw ApiException.createFrom(ResponseCode.CONFIGURATION_ERROR, e.getMessage());
        }
    }
    
}

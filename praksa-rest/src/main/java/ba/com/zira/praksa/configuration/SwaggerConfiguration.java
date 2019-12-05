package ba.com.zira.praksa.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Bean
    public Docket sampleApi() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("praksa-rest-api").apiInfo(apiInfo()).select()
                .apis(RequestHandlerSelectors.basePackage("ba.com.zira.praksa.rest")).build().tags(new Tag("sample", "Sample APIs"));
    }

    private static ApiInfo apiInfo() {
        final Contact contact = new Contact("ZIRA", "http://www.zira.com.ba", "info@zira.com.ba");
        ApiInfoBuilder builder = new ApiInfoBuilder();
        builder.title("ZIRA API");
        builder.description("ZIRA API");
        builder.version("Version 0.0.1-SNAPSHOT");
        builder.contact(contact);
        return builder.build();
    }
}
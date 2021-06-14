package ba.com.zira.praksa.configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ba.com.zira.commons.http.ZiraHttpHeader;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    private static final List<Parameter> operationParameters;
    static {
        List<Parameter> parameters = new ArrayList<>();
        ParameterBuilder userHeader = new ParameterBuilder();
        userHeader.name(ZiraHttpHeader.USER_ID).modelRef(new ModelRef("string")).parameterType("header").defaultValue("swagger")
                .required(true).build();
        ParameterBuilder transactionHeader = new ParameterBuilder();
        transactionHeader.name(ZiraHttpHeader.TRANSACTION_ID).modelRef(new ModelRef("string")).parameterType("header")
                .defaultValue("swagger-TID-1").required(true).build();
        ParameterBuilder sessionHeader = new ParameterBuilder();
        sessionHeader.name(ZiraHttpHeader.SESSION_ID).modelRef(new ModelRef("string")).parameterType("header")
                .defaultValue("swagger-session-id-1").required(false).build();
        ParameterBuilder channelHeader = new ParameterBuilder();
        channelHeader.name(ZiraHttpHeader.CHANNEL).modelRef(new ModelRef("string")).parameterType("header").defaultValue("SWAGGER")
                .required(false).build();
        ParameterBuilder languageHeader = new ParameterBuilder();
        languageHeader.name(ZiraHttpHeader.LANGUAGE_ID).modelRef(new ModelRef("string")).parameterType("header").defaultValue("ba")
                .required(false).build();
        parameters.add(userHeader.build());
        parameters.add(transactionHeader.build());
        parameters.add(sessionHeader.build());
        parameters.add(channelHeader.build());
        parameters.add(languageHeader.build());
        operationParameters = Collections.unmodifiableList(parameters);
    }

    @Bean
    public Docket gameApi() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("game-api").apiInfo(apiInfo()).select()
                .apis(RequestHandlerSelectors.basePackage("ba.com.zira.praksa.rest.game")).build().tags(new Tag("game", "Game APIs"))
                .globalOperationParameters(operationParameters);
    }

    @Bean
    public Docket conceptApi() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("concept-api").apiInfo(apiInfo()).select()
                .apis(RequestHandlerSelectors.basePackage("ba.com.zira.praksa.rest.concept")).build()
                .tags(new Tag("concept", "Concept APIs")).globalOperationParameters(operationParameters);
    }

    @Bean
    public Docket linkMapApi() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("link-map-api").apiInfo(apiInfo()).select()
                .apis(RequestHandlerSelectors.basePackage("ba.com.zira.praksa.rest.linkmap")).build()
                .tags(new Tag("linkMap", "LinkMap APIs")).globalOperationParameters(operationParameters);
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
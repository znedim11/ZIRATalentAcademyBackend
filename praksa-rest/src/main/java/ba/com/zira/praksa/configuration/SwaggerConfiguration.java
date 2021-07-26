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

    private static final String STRING = "string";
    private static final String HEADER = "header";

    private static final List<Parameter> operationParameters;
    static {
        List<Parameter> parameters = new ArrayList<>();
        ParameterBuilder userHeader = new ParameterBuilder();
        userHeader.name(ZiraHttpHeader.USER_ID).modelRef(new ModelRef(STRING)).parameterType(HEADER).defaultValue("swagger").required(true)
                .build();
        ParameterBuilder transactionHeader = new ParameterBuilder();
        transactionHeader.name(ZiraHttpHeader.TRANSACTION_ID).modelRef(new ModelRef(STRING)).parameterType(HEADER)
                .defaultValue("swagger-TID-1").required(true).build();
        ParameterBuilder sessionHeader = new ParameterBuilder();
        sessionHeader.name(ZiraHttpHeader.SESSION_ID).modelRef(new ModelRef(STRING)).parameterType(HEADER)
                .defaultValue("swagger-session-id-1").required(false).build();
        ParameterBuilder channelHeader = new ParameterBuilder();
        channelHeader.name(ZiraHttpHeader.CHANNEL).modelRef(new ModelRef(STRING)).parameterType(HEADER).defaultValue("SWAGGER")
                .required(false).build();
        ParameterBuilder languageHeader = new ParameterBuilder();
        languageHeader.name(ZiraHttpHeader.LANGUAGE_ID).modelRef(new ModelRef(STRING)).parameterType(HEADER).defaultValue("ba")
                .required(false).build();
        parameters.add(userHeader.build());
        parameters.add(transactionHeader.build());
        parameters.add(sessionHeader.build());
        parameters.add(channelHeader.build());
        parameters.add(languageHeader.build());
        operationParameters = Collections.unmodifiableList(parameters);
    }

    @Bean
    public Docket characterApi() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("character-api").apiInfo(apiInfo()).select()
                .apis(RequestHandlerSelectors.basePackage("ba.com.zira.praksa.rest.character")).build()
                .tags(new Tag("character", "Character APIs")).globalOperationParameters(operationParameters);
    }

    @Bean
    public Docket gameApi() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("game-api").apiInfo(apiInfo()).select()
                .apis(RequestHandlerSelectors.basePackage("ba.com.zira.praksa.rest.game")).build().tags(new Tag("game", "Game APIs"))
                .globalOperationParameters(operationParameters);
    }

    @Bean
    public Docket objectApi() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("object-api").apiInfo(apiInfo()).select()
                .apis(RequestHandlerSelectors.basePackage("ba.com.zira.praksa.rest.object")).build().tags(new Tag("object", "Object APIs"))
                .globalOperationParameters(operationParameters);
    }

    @Bean
    public Docket mediaApi() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("media-api").apiInfo(apiInfo()).select()
                .apis(RequestHandlerSelectors.basePackage("ba.com.zira.praksa.rest.media")).build().tags(new Tag("media", "Media APIs"))
                .globalOperationParameters(operationParameters);
    }

    @Bean
    public Docket featureApi() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("feature-api").apiInfo(apiInfo()).select()
                .apis(RequestHandlerSelectors.basePackage("ba.com.zira.praksa.rest.feature")).build()
                .tags(new Tag("feature", "Feature APIs")).globalOperationParameters(operationParameters);
    }

    @Bean
    public Docket franchiseApi() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("franchise-api").apiInfo(apiInfo()).select()
                .apis(RequestHandlerSelectors.basePackage("ba.com.zira.praksa.rest.franchise")).build()
                .tags(new Tag("franchise", "Franchise APIs")).globalOperationParameters(operationParameters);
    }

    @Bean
    public Docket regionApi() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("region-api").apiInfo(apiInfo()).select()
                .apis(RequestHandlerSelectors.basePackage("ba.com.zira.praksa.rest.region")).build().tags(new Tag("region", "Region APIs"))
                .globalOperationParameters(operationParameters);
    }

    @Bean
    public Docket externalReviewApi() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("externalReview-api").apiInfo(apiInfo()).select()
                .apis(RequestHandlerSelectors.basePackage("ba.com.zira.praksa.rest.externalreview")).build()
                .tags(new Tag("externalReview", "ExternalReview APIs")).globalOperationParameters(operationParameters);
    }

    @Bean
    public Docket companyApi() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("company-api").apiInfo(apiInfo()).select()
                .apis(RequestHandlerSelectors.basePackage("ba.com.zira.praksa.rest.company")).build()
                .tags(new Tag("company", "Company APIs")).globalOperationParameters(operationParameters);
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
                .apis(RequestHandlerSelectors.basePackage("ba.com.zira.praksa.rest.linkmap")).build().tags(new Tag("link", "LinkMap APIs"))
                .globalOperationParameters(operationParameters);
    }

    @Bean
    public Docket platformApi() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("platform-api").apiInfo(apiInfo()).select()
                .apis(RequestHandlerSelectors.basePackage("ba.com.zira.praksa.rest.platform")).build()
                .tags(new Tag("platform", "Platform APIs")).globalOperationParameters(operationParameters);
    }

    @Bean
    public Docket personApi() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("person-api").apiInfo(apiInfo()).select()
                .apis(RequestHandlerSelectors.basePackage("ba.com.zira.praksa.rest.person")).build().tags(new Tag("person", "Person APIs"))
                .globalOperationParameters(operationParameters);
    }

    @Bean
    public Docket releaseApi() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("release-api").apiInfo(apiInfo()).select()
                .apis(RequestHandlerSelectors.basePackage("ba.com.zira.praksa.rest.release")).build()
                .tags(new Tag("release", "Release APIs")).globalOperationParameters(operationParameters);
    }

    @Bean
    public Docket reviewApi() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("review-api").apiInfo(apiInfo()).select()
                .apis(RequestHandlerSelectors.basePackage("ba.com.zira.praksa.rest.review")).build().tags(new Tag("review", "Review APIs"))
                .globalOperationParameters(operationParameters);

    }

    @Bean
    public Docket reviewFormulaApi() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("review-formula-api").apiInfo(apiInfo()).select()
                .apis(RequestHandlerSelectors.basePackage("ba.com.zira.praksa.rest.formula")).build()
                .tags(new Tag("review-formula", "Formula APIs")).globalOperationParameters(operationParameters);

    }

    @Bean
    public Docket userApi() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("user-api").apiInfo(apiInfo()).select()
                .apis(RequestHandlerSelectors.basePackage("ba.com.zira.praksa.rest.user")).build().tags(new Tag("user", "User APIs"))
                .globalOperationParameters(operationParameters);
    }

    @Bean
    public Docket dataTransferApi() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("data-transfer-api").apiInfo(apiInfo()).select()
                .apis(RequestHandlerSelectors.basePackage("ba.com.zira.praksa.rest.datatransfer")).build()
                .tags(new Tag("data-transfer", "Data transfer APIs")).globalOperationParameters(operationParameters);
    }

    @Bean
    public Docket multiSearchApi() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("search-api").apiInfo(apiInfo()).select()
                .apis(RequestHandlerSelectors.basePackage("ba.com.zira.praksa.rest.search")).build().tags(new Tag("search", "Search APIs"))
                .globalOperationParameters(operationParameters);
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
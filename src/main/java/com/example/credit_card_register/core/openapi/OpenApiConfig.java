package com.example.credit_card_register.core.openapi;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Value("${application.url}")
    private String applicationUrl;

    @Value("${application.documentation.name}")
    private String applicationName;

    @Value("${application.documentation.description}")
    private String applicationDescription;

    @Value("${application.documentation.version}")
    private String applicationVersion;

    @Bean
    public OpenAPI openApiInformation() {
        Server localServer = new Server().url(applicationUrl);

        Info info = new Info().description(applicationDescription).title(applicationName).version(
                applicationVersion);
        return new OpenAPI().info(info).addServersItem(localServer);
    }

    @Bean
    public GroupedOpenApi userCardRegisterApi() {
        return GroupedOpenApi.builder().group("Card Register API").pathsToMatch("/api/v1/user-card-register/**").build();
    }

    @Bean
    public GroupedOpenApi userCardProcessApi() {
        return GroupedOpenApi.builder().group("Card Process API").pathsToMatch("/api/v1/card-request/**").build();
    }

}

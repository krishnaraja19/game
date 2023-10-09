package com.kindred.game.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI schoolMicroserviceOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Online Betting Game")
                        .description("Choose yor number and Win the betting Amount!!!")
                        .version("1.0"));
    }
}

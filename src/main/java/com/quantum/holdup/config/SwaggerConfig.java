package com.quantum.holdup.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(title = "Hold Up",
                description = "Hold Up Project",
                version = "v1"))
@Configuration
public class SwaggerConfig {

}
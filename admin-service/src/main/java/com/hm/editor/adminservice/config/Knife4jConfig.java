package com.hm.editor.adminservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Knife4jConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(
            new Info()
                .title("HmEditor Admin Service API")
                .description("HmEditor管理服务接口文档")
                .version("4.2.10")
                .contact(new Contact().name("HmEditor").email(""))
        );
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
            .group("default")
            .pathsToMatch("/**")
            .packagesToScan("com.hm.editor.adminservice")
            .build();
    }
}

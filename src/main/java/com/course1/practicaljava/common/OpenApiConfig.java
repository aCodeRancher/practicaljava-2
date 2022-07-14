package com.course1.practicaljava.common;

import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;

@Configuration

public class OpenApiConfig {

    @Bean
    public OpenAPI practicalJavaOpenApi(){
          var info = new Info().title("Practical Java API")
                  .description("OpenApi (Swagger) documentation auto generated from code")
                  .version("3.0");
          return new OpenAPI().info(info);
    }
}



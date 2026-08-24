package com.yazaki.printingservice.config;
import org.springframework.context.annotation.*; import io.swagger.v3.oas.models.OpenAPI; import io.swagger.v3.oas.models.info.Info;
@Configuration public class OpenApiConfiguration { @Bean OpenAPI printingServiceOpenApi(){return new OpenAPI().info(new Info().title("Printing Service API").version("v1").description("Development API using fictitious printers and fake adapters only."));} }

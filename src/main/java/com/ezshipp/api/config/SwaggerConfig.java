package com.ezshipp.api.config;

import com.google.common.base.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.ezshipp.api.controllers"))
                .paths(ezshippPaths())
                .build()
                .apiInfo(apiInfo());
    }

    private Predicate<String> ezshippPaths() {
		return or(
				regex("/api/v1/orders.*"),
				regex("/api/v1/customers.*"),
				regex("/api/v1/downloads.*"),
				regex("/api/v1/bikers.*"),
				regex("/api/v1/dashboard.*")
		);
	}
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("eZShipp Rest API")
                .description("eZShipp API Swagger.")
                .termsOfServiceUrl("http://springfox.io")
                .contact("springfox")
                .license("Apache License Version 2.0")
                .licenseUrl("https://github.com/springfox/springfox/blob/master/LICENSE")
                .version("2.0")
                .build();
    }
}

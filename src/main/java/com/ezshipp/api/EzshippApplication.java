package com.ezshipp.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAutoConfiguration
@EnableAsync
@ComponentScan(basePackages = {"com.ezshipp.api"})
public class EzshippApplication {

	public static void main(String[] args) {
		SpringApplication.run(EzshippApplication.class, args);
	}

//	/**
//	 * API Key Filter. Used by the apiKeyFilterProxy.
//	 *
//	 * @return the api key filter
//	 */
//	@Bean(name = "APIKeyFilter")
//	public APIKeyFilter apiKeyFilter() {
//		return new APIKeyFilter();
//	}
//
//	/**
//	 * Api key filter proxy.
//	 *
//	 * @return the filter registration bean
//	 */
//	@Bean
//	public FilterRegistrationBean apiKeyFilterProxy() {
//		FilterRegistrationBean registration = new FilterRegistrationBean();
//		registration.setName("APIKeyFilter");
//		registration.setFilter(new DelegatingFilterProxy());
//
//		registration.addInitParameter("targetFilterLifecycle", "true");
//		registration.addUrlPatterns("/api/ecommerce/*", "/api/payment/*", "/api/admin/*", "/api/notification/*", "/api/loyalty/*", "/api/crs/*", "/api/propertymaster/*");
//		registration.setOrder(1);
//		return registration;
//	}
//	@Bean
//	public Docket ezshippApi() {
//		return new Docket(DocumentationType.SWAGGER_2)
//				.apiInfo(apiInfo())
//				.select()
//				.apis(RequestHandlerSelectors.any())
//				.paths(ezshippPaths())
//				.build()
//				.pathMapping("/")
//				.directModelSubstitute(LocalDate.class,
//						String.class)
//				.genericModelSubstitutes(ResponseEntity.class)
//				.alternateTypeRules(
//						newRule(typeResolver.resolve(DeferredResult.class,
//								typeResolver.resolve(ResponseEntity.class, WildcardType.class)),
//								typeResolver.resolve(WildcardType.class)))
//				.useDefaultResponseMessages(false)
//				.globalResponseMessage(RequestMethod.GET,
//						newArrayList(new ResponseMessageBuilder()
//								.code(500)
//								.message("500 message")
//								.responseModel(new ModelRef("Error"))
//								.build()))
//				.securitySchemes(newArrayList(apiKey()))
//				.securityContexts(newArrayList(securityContext()))
//				.enableUrlTemplating(true)
//				.globalOperationParameters(
//						newArrayList(new ParameterBuilder()
//								.name("someGlobalParameter")
//								.description("Description of someGlobalParameter")
//								.modelRef(new ModelRef("string"))
//								.parameterType("query")
//								.required(true)
//								.build()))
//				.tags(new Tag("Pet Service", "All apis relating to ezshipp"));
//				//.additionalModels(typeResolver.resolve(AdditionalModel.class));
//	}
//
//	@Inject
//	private TypeResolver typeResolver;
//
//	private ApiKey apiKey() {
//		return new ApiKey("mykey", "api_key", "header");
//	}
//
//	private Predicate<String> ezshippPaths() {
//		return or(
//				regex("/orders.*"),
//				regex("/customers.*")
//				//regex("/api/biker.*")
//		);
//	}
//
//	private SecurityContext securityContext() {
//		return SecurityContext.builder()
//				.securityReferences(defaultAuth())
//				.forPaths(regex("/anyPath.*"))
//				.build();
//	}
//
//	List<SecurityReference> defaultAuth() {
//		AuthorizationScope authorizationScope
//				= new AuthorizationScope("global", "accessEverything");
//		AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
//		authorizationScopes[0] = authorizationScope;
//		return newArrayList(
//				new SecurityReference("mykey", authorizationScopes));
//	}
//
//	@Bean
//	SecurityConfiguration security() {
//		return SecurityConfigurationBuilder.builder()
//				.clientId("test-app-client-id")
//				.clientSecret("test-app-client-secret")
//				.realm("test-app-realm")
//				.appName("test-app")
//				.scopeSeparator(",")
//				.additionalQueryStringParams(null)
//				.useBasicAuthenticationWithAccessCodeGrant(false)
//				.build();
//	}
//
//	@Bean
//	SecurityScheme oauth() {
//		return new OAuthBuilder()
//				.name("petstore_auth")
//				.grantTypes(grantTypes())
//				.scopes(scopes())
//				.build();
//	}
//
//	List<AuthorizationScope> scopes() {
//		return newArrayList(
//				new AuthorizationScope("write:pets", "modify pets in your account"),
//				new AuthorizationScope("read:pets", "read your pets"));
//	}
//
//	List<GrantType> grantTypes() {
//		GrantType grantType = new ImplicitGrantBuilder()
//				.loginEndpoint(new LoginEndpoint("http://petstore.swagger.io/api/oauth/dialog"))
//				.build();
//		return newArrayList(grantType);
//	}
//
//	@Bean
//	UiConfiguration uiConfig() {
//		return UiConfigurationBuilder.builder()
//				.deepLinking(true)
//				.displayOperationId(false)
//				.defaultModelsExpandDepth(1)
//				.defaultModelExpandDepth(1)
//				.defaultModelRendering(ModelRendering.EXAMPLE)
//				.displayRequestDuration(false)
//				.docExpansion(DocExpansion.NONE)
//				.filter(false)
//				.maxDisplayedTags(null)
//				.operationsSorter(OperationsSorter.ALPHA)
//				.showExtensions(false)
//				.tagsSorter(TagsSorter.ALPHA)
//				.validatorUrl(null)
//				.build();
//	}
//
//	private ApiInfo apiInfo() {
//		return new ApiInfoBuilder()
//				.title("eZShipp Rest API")
//				.description("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum " +
//						"has been the industry's standard dummy text ever since the 1500s, when an unknown printer "
//						+ "took a " +
//						"galley of type and scrambled it to make a type specimen book. It has survived not only five " +
//						"centuries, but also the leap into electronic typesetting, remaining essentially unchanged. " +
//						"It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum " +
//						"passages, and more recently with desktop publishing software like Aldus PageMaker including " +
//						"versions of Lorem Ipsum.")
//				.termsOfServiceUrl("http://springfox.io")
//				.contact("springfox")
//				.license("Apache License Version 2.0")
//				.licenseUrl("https://github.com/springfox/springfox/blob/master/LICENSE")
//				.version("2.0")
//				.build();
//	}

}

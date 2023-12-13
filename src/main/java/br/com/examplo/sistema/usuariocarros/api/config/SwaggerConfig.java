package br.com.examplo.sistema.usuariocarros.api.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.examplo.sistema.usuariocarros.api.exceptionhandler.ApiExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestMethod;

import io.swagger.models.auth.In;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Vitor B.
 *
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("br.com.carros.api.controller"))
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false)
                .globalResponseMessage(RequestMethod.GET, defaultResponseForErrorMessage())
                .globalResponseMessage(RequestMethod.POST, defaultResponseForErrorMessage())
                .globalResponseMessage(RequestMethod.PUT, defaultResponseForErrorMessage())
                .globalResponseMessage(RequestMethod.DELETE, defaultResponseForErrorMessage())
                .securitySchemes(Arrays.asList(new ApiKey("Token Access", HttpHeaders.AUTHORIZATION, In.HEADER.name())))
                .securityContexts(Arrays.asList(securityContext()))
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()//				.
                .version("1.0.0")//
                .license("Apache License Version 2.0")//
                .licenseUrl("https://www.apache.org/licenses/LICENSE-2.0")//
                .build();
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()//
                .securityReferences(defaultAuth())//
                .forPaths(PathSelectors.ant("/api/cars/**"))//
                .forPaths(PathSelectors.ant("/me/**"))//
                .build();
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("ADMIN", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference("Token Access", authorizationScopes));
    }

    private List<ResponseMessage> defaultResponseForErrorMessage() {
        return new ArrayList<ResponseMessage>() {
            private static final long serialVersionUID = 1L;

            {
                add(new ResponseMessageBuilder()//
                        .code(500)//
                        .message(ApiExceptionHandler.MSG_ERRO_GENERICA_USUARIO_FINAL)//
                        .build());//
            }
        };
    }

}

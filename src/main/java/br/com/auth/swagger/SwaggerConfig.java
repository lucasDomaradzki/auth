package br.com.auth.swagger;

import java.util.ArrayList;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	
	@Bean
	public Docket api() {
		return new Docket( DocumentationType.SWAGGER_2 ).select().apis( RequestHandlerSelectors.basePackage( "br.com.auth" ) ).paths( PathSelectors.any() ).build().apiInfo( this.apiInfo() );
	}

	/**
	 * Informações da API
	 * 
	 * @return Api Info
	 */
	private ApiInfo apiInfo() {
		final ApiInfo apiInfo = new ApiInfo( "Auth", "API de Autenticação", "1.0.0.0", "Terms of service", new Contact( "Lucas/Domaris", "www.apex.com.br", "apex@email.com" ), "Direitos de Apex Serviços", "www.apex.com.br", new ArrayList<>() );

		return apiInfo;
	}

}

package com.streaming_online.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@SpringBootApplication
@EnableConfigurationProperties(UriConfiguration.class)
@RestController
public class GatewayApplication {

	public static void main(String[] args) {
		// Retrieve execution profile from environment variable. Otherwise, default
		// profile is used.
		String profile = System.getenv("PROFILE");
		System.setProperty("spring.profiles.active", profile != null ? profile : "default");
		SpringApplication.run(GatewayApplication.class, args);
	}

	@Bean
	public RouteLocator myRoutes(RouteLocatorBuilder builder, UriConfiguration uriConfiguration) {
		// String httpUri = uriConfiguration.getHttpbin();
		String cantoralUri = "http://frontend:3000/";
		String serviceAUri = "http://service-a:8081/";
		String serviceBUri = "http://service-b:8082/";

		return builder.routes()
				.route(p -> p
						.path("/helloWorld")
						.uri(serviceAUri))
				.route(p -> p
						.path("/helloEureka")
						.uri(serviceBUri))
				.route(p -> p
						.path("/api/**")
						.uri(serviceAUri))
				.route(p -> p
						.path("/bpi/**")
						.uri(serviceBUri))
				.route(p -> p
						.path("/**")
						.uri(cantoralUri))
				.build();
	}

	@RequestMapping("/version")
	public Mono<String> fallback() {
		return Mono.just("Gateway v1.0");
	}
}

@ConfigurationProperties
class UriConfiguration {

	private String httpbin = "http://127.0.0.1:123";

	public String getHttpbin() {
		return httpbin;
	}

	public void setHttpbin(String httpbin) {
		this.httpbin = httpbin;
	}
}

package pl.service3.service3.service.sources;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ApiConf {

    @Bean
    public static WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}

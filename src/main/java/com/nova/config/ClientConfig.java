package com.nova.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.client.RestClient;

@Configuration
@EnableConfigurationProperties({AppDispatchProperties.class, AppKakaoProperties.class})
public class ClientConfig {

    @Bean("kakaoRestClient")
    public RestClient kakaoRestClient() {
        return RestClient.builder()
                .build();
    }
}

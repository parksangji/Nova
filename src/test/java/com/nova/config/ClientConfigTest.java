package com.nova.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.client.RestClient;

class ClientConfigTest {

    @Test
    void registersRestClientAndBindsProperties() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext()) {
            context.register(ClientConfig.class);
            context.refresh();

            assertThat(context.getBean("kakaoRestClient", RestClient.class)).isNotNull();
        }

        AppDispatchProperties dispatchProperties = new AppDispatchProperties(
                "0 0 9 * * *",
                "Asia/Seoul",
                "Daily Message",
                "Morning message",
                "01084627902",
                "test@example.com"
        );
        AppKakaoProperties kakaoProperties = new AppKakaoProperties(
                true,
                "https://example.com/kakao",
                "token",
                "sender-key",
                "template-code"
        );

        assertThat(dispatchProperties.recipientPhone()).isEqualTo("01084627902");
        assertThat(dispatchProperties.zone()).isEqualTo("Asia/Seoul");
        assertThat(kakaoProperties.webhookUrl()).isEqualTo("https://example.com/kakao");
        assertThat(kakaoProperties.enabled()).isTrue();
    }
}

package com.nova.service.sender;

import com.nova.domain.entity.Notification;
import com.nova.domain.constants.NotificationType;
import com.nova.service.sender.impl.NotificationSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoNotificationSender implements NotificationSender {

    private final RestClient kakaoRestClient;

    @Value("${kakao.api.key}")
    private String apiKey;

    @Value("${kakao.api.template-id}")
    private String templateId;

    @Override
    public void send(Notification notification) {
        log.info("Sending KakaoTalk to: {}", notification.getRecipient());

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("template_id", templateId);
        formData.add("template_args", "{\"msg\":\"" + notification.getContent() + "\"}");

        kakaoRestClient.post()
                .uri("/v2/api/talk/memo/send")
                .header("Authorization", "Bearer " + apiKey)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(formData)
                .retrieve()
                .toBodilessEntity();
    }

    @Override
    public boolean supports(NotificationType type) {
        return type == NotificationType.KAKAO;
    }
}

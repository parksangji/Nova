package com.nova.controller.notification;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.nova.service.notification.NotificationCommandService;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class NotificationControllerTest {

    private final NotificationCommandService notificationCommandService = mock(NotificationCommandService.class);
    private final MockMvc mockMvc = MockMvcBuilders
            .standaloneSetup(new NotificationController(notificationCommandService))
            .build();

    @Test
    void createsNotification() throws Exception {
        when(notificationCommandService.requestNotification(any())).thenReturn(1L);

        mockMvc.perform(post("/api/v1/notifications")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "recipient": "01084627902",
                                  "content": "hello",
                                  "type": "KAKAO"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.notificationId").value(1L));

        verify(notificationCommandService).requestNotification(any());
    }

    @Test
    void returnsBadRequestForInvalidRequest() throws Exception {
        mockMvc.perform(post("/api/v1/notifications")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "recipient": "",
                                  "content": "",
                                  "type": null
                                }
                                """))
                .andExpect(status().isBadRequest());
    }
}

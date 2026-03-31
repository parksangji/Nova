package com.nova.controller;

import com.nova.controller.dto.DailyNotificationScheduleResponse;
import com.nova.domain.NotificationType;
import com.nova.service.DailyNotificationScheduleService;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DailyNotificationScheduleControllerTest {

    private final DailyNotificationScheduleService dailyNotificationScheduleService = mock(DailyNotificationScheduleService.class);
    private final MockMvc mockMvc = MockMvcBuilders
            .standaloneSetup(new DailyNotificationScheduleController(dailyNotificationScheduleService))
            .build();

    @Test
    void getsSchedule() throws Exception {
        when(dailyNotificationScheduleService.getSchedule()).thenReturn(response());

        mockMvc.perform(get("/api/v1/daily-notifications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recipient").value("01084627902"))
                .andExpect(jsonPath("$.type").value("KAKAO"));
    }

    @Test
    void updatesSchedule() throws Exception {
        when(dailyNotificationScheduleService.updateSchedule(any())).thenReturn(response());

        mockMvc.perform(put("/api/v1/daily-notifications")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "enabled": true,
                                  "recipient": "01084627902",
                                  "content": "good morning",
                                  "type": "KAKAO"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("good morning"));

        verify(dailyNotificationScheduleService).updateSchedule(any());
    }

    @Test
    void triggersManualSend() throws Exception {
        when(dailyNotificationScheduleService.getSchedule()).thenReturn(response());

        mockMvc.perform(post("/api/v1/daily-notifications/trigger"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastDispatchedOn").value("2026-03-31"));

        verify(dailyNotificationScheduleService).triggerNow();
    }

    @Test
    void returnsBadRequestForInvalidUpdateRequest() throws Exception {
        mockMvc.perform(put("/api/v1/daily-notifications")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "enabled": true,
                                  "recipient": "",
                                  "content": "",
                                  "type": null
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    private DailyNotificationScheduleResponse response() {
        return new DailyNotificationScheduleResponse(
                true,
                "01084627902",
                "good morning",
                NotificationType.KAKAO,
                "0 0 9 * * *",
                "Asia/Seoul",
                LocalDate.of(2026, 3, 31)
        );
    }
}

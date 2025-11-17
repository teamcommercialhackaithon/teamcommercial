package com.teamcommercial.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Base64;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDTO {
    private Long eventId;
    private String type;
    private String macAddress;
    private String serial;
    private String message;
    private LocalDateTime date;
    private String payloadBase64; // Base64 encoded payload for JSON transmission
    private Integer payloadSize; // Size of payload in bytes
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Helper method to convert byte[] to Base64 string
    public void setPayloadFromBytes(byte[] payload) {
        if (payload != null && payload.length > 0) {
            this.payloadBase64 = Base64.getEncoder().encodeToString(payload);
            this.payloadSize = payload.length;
        } else {
            this.payloadBase64 = null;
            this.payloadSize = 0;
        }
    }

    // Helper method to convert Base64 string to byte[]
    public byte[] getPayloadAsBytes() {
        if (payloadBase64 != null && !payloadBase64.isEmpty()) {
            return Base64.getDecoder().decode(payloadBase64);
        }
        return null;
    }
}


package com.teamcommercial.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

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
    private String payload; // JSON string payload (stored as BLOB in database)
    private Boolean processed;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Helper method to convert byte[] to String
    public void setPayloadFromBytes(byte[] payloadBytes) {
        if (payloadBytes != null && payloadBytes.length > 0) {
            this.payload = new String(payloadBytes, StandardCharsets.UTF_8);
        } else {
            this.payload = null;
        }
    }

    // Helper method to convert String to byte[]
    public byte[] getPayloadAsBytes() {
        if (payload != null && !payload.isEmpty()) {
            return payload.getBytes(StandardCharsets.UTF_8);
        }
        return null;
    }
}


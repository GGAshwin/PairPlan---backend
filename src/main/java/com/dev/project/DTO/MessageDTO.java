package com.dev.project.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO {
    private String message;

    // Factory method for creating success messages
    public static MessageDTO success(String message) {
        return new MessageDTO(message);
    }

    // Factory method for creating error messages
    public static MessageDTO error(String message) {
        return new MessageDTO(message);
    }
}

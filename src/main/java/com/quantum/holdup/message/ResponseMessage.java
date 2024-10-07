package com.quantum.holdup.message;

import lombok.*;

@NoArgsConstructor
@Getter
public class ResponseMessage {

    private String message;
    private Object result;

    @Builder
    public ResponseMessage(String message, Object result) {
        this.message = message;
        this.result = result;
    }
}
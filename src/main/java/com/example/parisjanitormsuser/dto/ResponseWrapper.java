package com.example.parisjanitormsuser.dto;


import lombok.*;

@Setter
@Getter
@NoArgsConstructor
public class ResponseWrapper<T>{
    private boolean success;
    private String message;
    private T data;
    /*
     private String error;
     private Instant timestamp;
     private String message;
     private String path;
    private String errorCode;
    private String message;*/

    public ResponseWrapper(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

}

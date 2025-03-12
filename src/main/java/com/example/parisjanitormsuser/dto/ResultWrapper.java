package com.example.parisjanitormsuser.dto;


import lombok.*;

@Setter
@Getter
@NoArgsConstructor
public class ResultWrapper <T>{
    private boolean state;
    private String msg;
    private T data;
    /*
     private String error;
     private Instant timestamp;
     private String message;
     private String path;
    private String errorCode;
    private String message;*/

    public ResultWrapper(boolean state, String msg, T data) {
        this.state = state;
        this.msg = msg;
        this.data = data;
    }

}

package com.example.parisjanitormsuser.dto;


import lombok.*;
import org.springframework.http.HttpStatus;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseWrapper<T>{
    private boolean success;
    private String message;
    private T data;
    private int status;
    private String path;

    public ResponseWrapper(boolean success, String message, T data, int status) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.status = status;
    }
    public static <T> ResponseWrapper<T> ok(T data, String message) {
        return new ResponseWrapper<>(true, message, data, HttpStatus.OK.value());
    }
    public static <T> ResponseWrapper<T> badRequest(String message) {
        return new ResponseWrapper<>(false,message,null,HttpStatus.BAD_REQUEST.value());
    }
    public static <T> ResponseWrapper<T> notFound(String message) {
        return new ResponseWrapper<>(false,message,null,HttpStatus.NOT_FOUND.value());
    }
    public static <T> ResponseWrapper<T> conflict(String message) {
        return new ResponseWrapper<>(false,message,null,HttpStatus.CONFLICT.value());
    }
    public static <T> ResponseWrapper<T> unauthorized(String message) {
        return new ResponseWrapper<>(false,message,null,HttpStatus.UNAUTHORIZED.value());
    }
    public static <T> ResponseWrapper<T> internalServerError(String message) {
        return new ResponseWrapper<>(false,message,null,HttpStatus.INTERNAL_SERVER_ERROR.value());
    }


}

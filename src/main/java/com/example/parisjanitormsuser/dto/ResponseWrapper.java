package com.example.parisjanitormsuser.dto;


import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
//@AllArgsConstructor
public class ResponseWrapper<T>{

    // --- Getters & Setters (pour Jackson) ---
    private boolean success;
    private String message;
    private T data;
    private int status;
    private String path;

    // --- Constructeurs utiles ---
    public ResponseWrapper(boolean success, String message, T data, int status) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.status = status;
    }


    public ResponseWrapper(boolean success, String message, T data, int status, String path) {
        this(success, message, data, status);
        this.path = path;
    }

    public ResponseWrapper(boolean success,  T data, int status, String path) {
        this.success=success;
        this.data=data;
        this.status=status;
        this.path = path;
    }


    public  ResponseWrapper(boolean success,int status, String path) {
        this.success=success;
        this.status=status;
        this.path = path;
    }

    // --- Méthodes de fabrique statiques ---

    public static <T> ResponseWrapper<T> error(T data, String message, int status, String path) {
        return new ResponseWrapper<>(false, message, data, status, path);
    }

    public static <T> ResponseWrapper<T> ok(String message,String path) {
        return new ResponseWrapper<>(true,  HttpStatus.OK.value(),path);
    }

    public static <T> ResponseWrapper<T> ok(T data,String path) {
        return new ResponseWrapper<>(true,  data, HttpStatus.OK.value(),path);
    }

    public static <T> ResponseWrapper<T> ok(T data, String message, String path) {
        return new ResponseWrapper<>(true, message, data, HttpStatus.OK.value());
    }

    public static <T> ResponseWrapper<T> badRequest(String message) {
        return new ResponseWrapper<>(false, message, null, HttpStatus.BAD_REQUEST.value());
    }

    public static <T> ResponseWrapper<T> notFound(String message) {
        return new ResponseWrapper<>(false, message, null, HttpStatus.NOT_FOUND.value());
    }

    public static <T> ResponseWrapper<T> conflict(String message) {
        return new ResponseWrapper<>(false, message, null, HttpStatus.CONFLICT.value());
    }

    public static <T> ResponseWrapper<T> unauthorized(String message) {
        return new ResponseWrapper<>(false, message, null, HttpStatus.UNAUTHORIZED.value());
    }

    public static <T> ResponseWrapper<T> internalServerError(String message) {
        return new ResponseWrapper<>(false, message, null, HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
    
}

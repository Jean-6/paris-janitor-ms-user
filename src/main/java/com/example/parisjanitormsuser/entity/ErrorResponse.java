package com.example.parisjanitormsuser.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {

   /* private int status;
    private String error;
    private Instant timestamp;
    private String message;
    private String path;*/
   private String errorCode;
    private String message;

}

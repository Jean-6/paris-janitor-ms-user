package com.example.parisjanitormsuser.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorRes {
    private String error;
    private String code;
    private String details;
}
/*
{
  "status": 400,
  "error": "Invalid product ID",
  "code": "BAD_REQUEST",
  "details": "The provided product ID is not valid"
}
 */
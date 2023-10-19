package com.example.springboot.controllers.handler;

public record ResponseError (java.time.LocalDateTime timestamp, String status, String error, String path){

}

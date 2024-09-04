package com.ennov.gestion_tickets.exception;


import org.springframework.http.HttpStatus;

public class ApiException extends  RuntimeException{
    private  String message;
    private HttpStatus status;

    public ApiException(HttpStatus status, String message){
        super(message);
        this.status = status;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }
}

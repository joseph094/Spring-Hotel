package com.project.firstTry.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Annotation indicating that if an exception of this type is thrown, it should result in a 404 Not Found HTTP response
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class RessourceNotFoundException extends  RuntimeException{

    public RessourceNotFoundException(String message){
        super(message);
    }
}

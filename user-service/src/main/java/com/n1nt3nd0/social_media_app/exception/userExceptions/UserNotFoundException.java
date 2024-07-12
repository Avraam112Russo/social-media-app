package com.n1nt3nd0.social_media_app.exception.userExceptions;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String message){
        super(message);
    }
}

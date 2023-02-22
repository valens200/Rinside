package com.example.demo.exceptions;

public class InvalidInputs  extends RuntimeException{
    public  InvalidInputs(String message){
        super(message);
    }
}

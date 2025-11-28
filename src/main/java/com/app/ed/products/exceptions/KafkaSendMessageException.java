package com.app.ed.products.exceptions;

public class KafkaSendMessageException extends RuntimeException{

    public KafkaSendMessageException(String message) {
        super(message);
    }
}

package com.example.curs9.ubbcluj.map.domain.MyValidators;

public class MessageRepoValidator extends Exception{
    /**
     * Constructor default
     */
    public MessageRepoValidator() {
    }

    /**
     * Constructorul MessageRepoValidator cu mesaj
     * @param message
     */
    public MessageRepoValidator(String message){super(message);}
}

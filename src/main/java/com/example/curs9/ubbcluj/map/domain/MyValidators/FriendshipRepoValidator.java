package com.example.curs9.ubbcluj.map.domain.MyValidators;

public class FriendshipRepoValidator extends Exception{

    /**
     * Constructor FriendshipRepoValidator default
     */
    public FriendshipRepoValidator() {
    }

    /**
     * Constructor FriendshipRepoValidator cu mesaj
     * @param message
     */
    public FriendshipRepoValidator(String message) {
        super(message);
    }
}

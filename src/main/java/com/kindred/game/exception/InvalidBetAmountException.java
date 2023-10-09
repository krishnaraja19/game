package com.kindred.game.exception;

public class InvalidBetAmountException extends RuntimeException{
    public InvalidBetAmountException(){
        super("Invalid bet amount");
    }
}

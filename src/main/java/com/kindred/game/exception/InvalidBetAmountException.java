package com.kindred.game.exception;

public class InvalidBetAmountException extends RuntimeException{
    public InvalidBetAmountException(String minBetAmount, String maxBetAmount) {
        super("Invalid bet amount, Please provide bet amount within this range from "+minBetAmount+" to "+maxBetAmount);
    }
}

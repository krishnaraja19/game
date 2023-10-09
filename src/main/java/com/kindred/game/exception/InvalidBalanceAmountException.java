package com.kindred.game.exception;

public class InvalidBalanceAmountException extends RuntimeException{
    public InvalidBalanceAmountException(double balanceAmount){
        super("Your current balance is : "+balanceAmount+".   It is invalid amount. Please top up your balance." +
                "Play again and have fun.");
    }
}

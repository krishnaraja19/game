package com.kindred.game.service;

import com.kindred.game.entity.GameResult;
import com.kindred.game.entity.User;
import com.kindred.game.exception.InvalidBalanceAmountException;
import com.kindred.game.exception.InvalidBetAmountException;
import com.kindred.game.exception.UserNotFoundException;
import com.kindred.game.repository.GameResultRepository;
import com.kindred.game.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class GameService {

    @Value("${game.min-bet-amount}")
    private String minBetAmount;

    @Value("${game.max-bet-amount}")
    private String maxBetAmount;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GameResultRepository gameResultRepository;

    // Create an ExecutorService for thread pooling
    private ExecutorService executorService = Executors.newFixedThreadPool(10); // Adjust the pool size as needed

    // Create a lock object for synchronization
    private final Object betLock = new Object();

    public GameResult placeBet(Long userId, double betAmount, int chosenNumber) {
        // Use synchronized block to ensure thread safety within this method
        synchronized (betLock) {
            User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
            // Validate the user balance
            if(user.getBalance() <= 0)
                throw new InvalidBalanceAmountException(user.getBalance());
            if(betAmount > user.getBalance())
                throw new InvalidBalanceAmountException(user.getBalance());

            // Validate bet amount
            if (betAmount < Double.parseDouble(minBetAmount) || betAmount > Double.parseDouble(maxBetAmount))
                throw new InvalidBetAmountException(minBetAmount,maxBetAmount);

            // Generate a random number (1-6)
            int winningNumber = generateRandomNumber();
            String gameStatus = "";
            // Determine the outcome and update the balance
            if (chosenNumber == winningNumber) {
                user.setBalance(user.getBalance() + betAmount);
                gameStatus = "Win";
            } else {
                user.setBalance(user.getBalance() - betAmount);
                gameStatus = "Loss";
            }
            // Save the updated user
            userRepository.save(user);
            var gameResult = GameResult.builder()
                    .diceNumber(winningNumber)
                    .chosenNumber(chosenNumber)
                    .gameStatus(gameStatus)
                    .createdDate(LocalDateTime.now())
                    .user(user)
                    .build();
            return gameResultRepository.save(gameResult);

        }

    }

    private int generateRandomNumber() {
        return new Random().nextInt(6) + 1;
    }

}

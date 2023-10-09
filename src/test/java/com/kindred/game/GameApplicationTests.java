package com.kindred.game;

import com.kindred.game.entity.GameResult;
import com.kindred.game.entity.User;
import com.kindred.game.repository.GameResultRepository;
import com.kindred.game.repository.UserRepository;
import com.kindred.game.service.GameService;
import com.kindred.game.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GameApplicationTests {

    @LocalServerPort
    private int port;

    private String baseUrl = "http://localhost";
    private String baseGameUrl = baseUrl;

    @Autowired
    UserRepository userRepository;

    @Autowired
    GameResultRepository gameResultRepository;

    @Autowired
    UserService userService;

    @Autowired
    GameService gameService;

    private static TestRestTemplate testRestTemplate;

    @BeforeAll
    public static void init(){
        testRestTemplate = new TestRestTemplate();
    }

    @BeforeEach
    public void setUp(){
        baseUrl = baseUrl+":"+port+"/users/api/v0";
        baseGameUrl = baseGameUrl+":"+port+"/game/api/v0";
    }

    @Test
    public void givenUserObject_whenCreateUser_thenReturnSavedUserObject(){
        var user = User.builder().username("Name1").balance(10.0).build();

        URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl).path("/addUser").build().toUri();


        User response =testRestTemplate.postForObject(uri,user,User.class);

        assertAll(
                ()->assertEquals("Name1",response.getUsername()),
                ()->assertEquals(10.0,response.getBalance())
        );

    }

    @Test
    public void givenUserId_whenDeleteUser_thenReturn200(){
        var user = userRepository.save(User.builder().username("Gully").balance(10.0).build());

        testRestTemplate.delete(baseUrl+"/deleteUser/{id}",user.getId());

        assertTrue(userRepository.findById(user.getId()).isEmpty());
    }

    @Test
    public void givenUserIdAndBetAmountAndChoosenNumber_whenPlaceBet_thenReturnGameResult(){
        var user = userRepository.save(User.builder().username("Gully").balance(10.0).build());
        URI uri = UriComponentsBuilder.fromHttpUrl(baseGameUrl)
                .path("/place-bet")
                .queryParam("userId",user.getId())
                .queryParam("betAmount",1)
                .queryParam("chosenNumber",2)
                .build().toUri();

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<?> entity = new HttpEntity<>(headers);
        GameResult gameResult=testRestTemplate.postForObject(uri,entity,GameResult.class);

        assertAll(
                ()->assertEquals(gameResult.getChosenNumber(),2),
                ()->assertNotNull(gameResult.getGameStatus()),
                ()->assertNotEquals(user.getBalance(),gameResult.getUser().getBalance()),
                ()->assertEquals(user.getId(),gameResult.getUser().getId()),
                ()->assertEquals(user.getUsername(),gameResult.getUser().getUsername())
        );
        gameResultRepository.deleteById(gameResult.getId());
        userRepository.deleteById(user.getId());

    }
}

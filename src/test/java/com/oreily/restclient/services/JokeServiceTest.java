package com.oreily.restclient.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.test.StepVerifier;

import java.time.Duration;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JokeServiceTest {

    private Logger logger = LoggerFactory.getLogger(JokeServiceTest.class);

    @Autowired
    private JokeService target;

    @Test
    public void getJokeSync() {
        String joke = target.getJokeSync("Chuck", "Norris");
        logger.info("\n\n" + joke);
        assertTrue(joke.contains("Chuck") || joke.contains("Norris"));

    }

    @Test
    public void getJokeAsync() {
        String joke = target.getJokeAsync("Chuck", "Norris")
                .block(Duration.ofSeconds(2));
        logger.info("\n\n" + joke);
        assertTrue(joke.contains("Chuck") || joke.contains("Norris"));
    }

    @Test
    public void useStepVerifier() {
        StepVerifier.create(target.getJokeAsync("Craig", "Walls"))
                .assertNext(joke -> {
                    logger.info(joke);
                    assertTrue(joke.contains("Craig") || joke.contains("Walls"));
                })
                .verifyComplete();
    }
}
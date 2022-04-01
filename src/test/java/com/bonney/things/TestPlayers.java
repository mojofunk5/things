package com.bonney.things;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

import com.bonney.things.domain.Player;

public class TestPlayers {

    public static Player randomPlayer() {
        return new Player(randomAlphanumeric(10));
    }
}

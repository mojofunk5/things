package com.bonney.things.state;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class GameTest {

    @Test
    public void newGameIsNotStarted() {
        assertThat(Game.newGame(), instanceOf(PlayersJoining.class));
    }
}

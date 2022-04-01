package com.bonney.things.state;

import static com.bonney.things.TestPlayers.randomPlayer;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class NotStartedTest {

    @Test
    public void addingPlayerReturnsSameState() {
        Game gameState = Game.newGame();
        Game newGameState = gameState.add(randomPlayer());
        assertThat(newGameState, is(sameInstance(gameState)));
    }

    @Test
    public void returnsNumberOfPlayers() {
        Game game = Game.newGame();
        game.add(randomPlayer());
        game.add(randomPlayer());
        game.add(randomPlayer());
        assertThat(game.getNumberPlayers(), is(3));
    }

    @Test(expected = IllegalStateException.class)
    public void cannotStartRoundIfLessThanTwoPlayers() {
        Game game = Game.newGame();
        game.add(randomPlayer());
        game.newRound();
    }

    @Test
    public void canStartRoundWithTwoPlayers() {
        Game game = Game.newGame();
        game.add(randomPlayer());
        game.add(randomPlayer());
        Game newState = game.newRound();
        assertThat(newState, is(instanceOf(WaitingForAnswers.class)));
    }
}
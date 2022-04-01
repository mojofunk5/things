package com.bonney.things.state;

import static com.bonney.things.TestPlayers.randomPlayer;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class RoundFinishedTest {

    private Game initialState;

    @Before
    public void before() {
        Context context = new Context();
        context.addPlayer(randomPlayer());
        context.addPlayer(randomPlayer());
        this.initialState = new RoundFinished(context);
    }

    @Test
    public void canStartNewRound() {
        assertThat(this.initialState.newRound(), is(instanceOf(WaitingForAnswers.class)));
    }

    @Test
    public void canFinishGame() {
        assertThat(this.initialState.finishGame(), is(instanceOf(GameFinished.class)));
    }
}

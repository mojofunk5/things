package com.bonney.things.state;

import static com.bonney.things.TestPlayers.randomPlayer;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.bonney.things.domain.Round;

public class RoundInProgressTest {

    private Context context;

    private Game initialState;

    @Before
    public void before() {
        this.context = new Context();
        this.context.addPlayer(randomPlayer());
        this.context.addPlayer(randomPlayer());
        Round round = new Round(1, context.getPlayers(), randomAlphanumeric(20));
        round.submitAnswer(this.context.getPlayers().get(0), "first correct answer");
        round.submitAnswer(this.context.getPlayers().get(1), "second correct answer");

        this.initialState = new RoundInProgress(context, round);
    }

    @Test
    public void incorrectGuessLeavesRoundInProgress() {
        Game newState = this.initialState.guessAnswer(context.getPlayers().get(0), context.getPlayers().get(1),
                randomAlphanumeric(12));

        assertThat(newState, is(sameInstance(this.initialState)));
    }

    @Test
    public void correctFinalGuessMovesToEndOfRound() {
        Game newState = this.initialState.guessAnswer(context.getPlayers().get(0), context.getPlayers().get(1),
                "second correct answer");
        assertThat(newState, is(instanceOf(RoundFinished.class)));
    }
}

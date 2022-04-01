package com.bonney.things.state;

import static com.bonney.things.TestPlayers.randomPlayer;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class ContextTest {

    private Context context;

    @Before
    public void before() {
        this.context = new Context();
        this.context.addPlayer(randomPlayer());
        this.context.addPlayer(randomPlayer());
        this.context.addPlayer(randomPlayer());
    }

    @Test
    public void createdWithId() {
        assertThat(this.context.getId(), is(notNullValue()));
    }

    @Test
    public void reportsNumberOfRoundsAsZeroAtTheStart() {
        assertThat(this.context.getNumberRounds(), is(0));
    }

    @Test
    public void newRoundReturnsANewRound() {
        assertThat(this.context.newRound(), is(notNullValue()));
    }

    @Test
    public void newRoundIncrementsTheNumberOfRounds() {
        this.context.newRound();
        assertThat(this.context.getNumberRounds(), is(1));
    }

    @Test
    public void newRoundsKeepTrackOfNumber() {
        assertThat(this.context.newRound().getRoundNumber(), is(1));
        assertThat(this.context.newRound().getRoundNumber(), is(2));
        assertThat(this.context.newRound().getRoundNumber(), is(3));
    }
}
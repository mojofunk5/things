package com.bonney.things.state;

import com.bonney.things.domain.Player;
import com.bonney.things.domain.Round;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class IdentifyDuplicateAnswersTest {

    @Mock
    private Round mockRound;

    private Game gameState;

    @Before
    public void before() {
        Context context = new Context();
        context.addPlayer(randomPlayer());
        context.addPlayer(randomPlayer());
        this.gameState = new IdentifyDuplicateAnswers(context, this.mockRound);
    }

    @Test
    public void markingDuplicateAnswersNotifiesRound() {
        String newAnswer = randomAlphanumeric(12);
        this.gameState.markDuplicateAnswers(this.gameState.getPlayers(), newAnswer);
        verify(this.mockRound).markDuplicateAnswers(this.gameState.getPlayers(), newAnswer);
    }

    @Test
    public void startRoundMovesStateToInProgress() {
        assertThat(this.gameState.startRound(), is(instanceOf(RoundInProgress.class)));
    }

    private Player randomPlayer() {
        return new Player(randomAlphanumeric(12));
    }
}

package com.bonney.things.state;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.bonney.things.domain.Player;
import com.google.common.collect.ImmutableList;

public class SetupRoundTest {

    @Test
    public void hasCurrentQuestion() {
        Game setupRound = moveToSetupRound();
        assertThat(setupRound.getCurrentQuestion().isPresent(), is(true));
    }

    @Test
    public void submittingAnswerWillReturnSameStateWhilstNotAllPlayersHaveResponded() {
        Game setupRound = moveToSetupRound();
        Game nextState = setupRound.submitAnswer(setupRound.getPlayers().get(0), randomAlphanumeric(12));
        assertThat(nextState, is(sameInstance(setupRound)));
    }

    @Test
    public void submittingAllAnswersMovesToStateWhereReaderHasToSortDuplicateAnswers() {
        Game initialState = moveToSetupRound();
        ImmutableList<Player> players = initialState.getPlayers();
        Game nextState = initialState
                .submitAnswer(players.get(0), randomAlphanumeric(12))
                .submitAnswer(players.get(1), randomAlphanumeric(12));
        assertThat(nextState, is(instanceOf(IdentifyDuplicateAnswers.class)));
    }

    private Game moveToSetupRound() {
        Game game = Game.newGame();
        game.add(new Player(randomAlphanumeric(10)));
        game.add(new Player(randomAlphanumeric(10)));
        return game.newRound();
    }
}

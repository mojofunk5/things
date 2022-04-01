package com.bonney.things.domain;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import java.util.Collection;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableList;

public class RoundTest {

    private String question;

    private Player playerOne;
    private String playerOneAnswer;

    private Player playerTwo;
    private String playerTwoAnswer;

    private Player playerThree;
    private String playerThreeAnswer;

    private Round round;

    @Before
    public void before() {
        this.question = randomAlphanumeric(20);

        this.playerOne = randomPlayer();
        this.playerOneAnswer = randomAnswer();

        this.playerTwo = randomPlayer();
        this.playerTwoAnswer = randomAnswer();

        this.playerThree = randomPlayer();
        this.playerThreeAnswer = randomAnswer();

        this.round = new Round(1, ImmutableList.of(this.playerOne, this.playerTwo, this.playerThree), this.question);
    }

    @Test
    public void reportsQuestion() {
        assertThat(this.round.getQuestion(), is(this.question));
    }

    @Test
    public void reportsWhenNotAllAnswersReceived() {
        assertThat(this.round.everyPlayerAnswered(), is(false));
    }

    @Test
    public void reportsWhenAllAnswersReceived() {
        submitAllPlayersAnswers();
        assertThat(this.round.everyPlayerAnswered(), is(true));
    }

    @Test
    public void reportsPlayerOnCurrentTurn() {
        assertThat(this.round.getCurrentTurn(), is(this.playerOne));
    }

    @Test
    public void firstPlayerOnTurnChangesForEachRound() {
        ImmutableList<Player> players = ImmutableList.of(playerOne, playerTwo, playerThree);
        assertThat(new Round(2, players, randomAnswer()).getCurrentTurn(),
                is(playerTwo));
        assertThat(new Round(6, players, randomAnswer()).getCurrentTurn(),
                is(playerThree));
    }

    @Test
    public void movesPlayerTurnOnFollowingAnIncorrectGuess() {
        submitAllPlayersAnswers();
        makeIncorrectGuess();
        assertThat(this.round.getCurrentTurn(), is(this.playerTwo));
    }

    @Test
    public void turnReturnsToStartAfterLastPlayerGuessesIncorrectly() {
        submitAllPlayersAnswers();
        makeIncorrectGuess();
        makeIncorrectGuess();
        makeIncorrectGuess();
        assertThat(this.round.getCurrentTurn(), is(this.playerOne));
    }

    @Test
    public void turnDoesNotMoveOnFollowingCorrectGuess() {
        submitAllPlayersAnswers();
        this.round.guess(this.round.getCurrentTurn(), this.playerTwo, this.playerTwoAnswer);
        assertThat(this.round.getCurrentTurn(), is(this.playerOne));
    }

    @Test
    public void reportsWhenGuessIncorrect() {
        submitAllPlayersAnswers();
        assertThat(makeIncorrectGuess(), is(false));
    }

    @Test
    public void reportsWhenGuessCorrect() {
        submitAllPlayersAnswers();
        boolean correctGuess = this.round.guess(this.round.getCurrentTurn(), this.playerTwo, this.playerTwoAnswer);
        assertThat(correctGuess, is(true));
    }

    @Test
    public void reportsRemainingAnswersAtStartOfRound() {
        submitAllPlayersAnswers();
        assertThat(this.round.remainingAnswers(), hasSize(3));
    }

    @Test
    public void removesAnswerFromRemainingFollowingCorrectGuess() {
        submitAllPlayersAnswers();

        this.round.guess(this.round.getCurrentTurn(), this.playerTwo, this.playerTwoAnswer);

        Collection<String> remainingAnswers = this.round.remainingAnswers();
        assertThat(remainingAnswers, hasSize(2));
        assertThat(remainingAnswers, not(hasItem(this.playerTwoAnswer)));
    }

    @Test
    public void addsAnswerByPlayerToGuessCollection() {
        submitAllPlayersAnswers();

        this.round.guess(this.round.getCurrentTurn(), this.playerTwo, this.playerTwoAnswer);

        assertThat(this.round.guessedAnswers().size(), is(1));
    }

    @Test
    public void playersScoreForRoundIsZeroAtStart() {
        Map<Player, Integer> scores = this.round.getScores();
        assertThat(scores.get(this.playerOne), is(0));
        assertThat(scores.get(this.playerTwo), is(0));
        assertThat(scores.get(this.playerThree), is(0));
    }

    @Test
    public void recordsPointWhenPlayerMakesCorrectGuess() {
        submitAllPlayersAnswers();

        this.round.guess(this.round.getCurrentTurn(), this.playerTwo, this.playerTwoAnswer);

        assertThat(this.round.getScores().get(this.playerOne), is(Integer.valueOf(1)));
    }

    @Test
    public void roundIsNotFinishedAtTheStart() {
        assertThat(this.round.isFinished(), is(false));
    }

    @Test
    public void roundIsFinishedWhenOnlyOneUnguessedAnswerRemains() {
        submitAllPlayersAnswers();
        this.round.guess(this.round.getCurrentTurn(), this.playerTwo, this.playerTwoAnswer);
        this.round.guess(this.round.getCurrentTurn(), this.playerThree, this.playerThreeAnswer);
        assertThat(this.round.isFinished(), is(true));
    }

    @Test
    public void markingDuplicateAnswersReplacesPlayersOriginalAnswerWithNewOne() {
        submitAllPlayersAnswers();
        String newAnswer = randomAlphanumeric(14);
        this.round.markDuplicateAnswers(ImmutableList.of(this.playerTwo, this.playerThree), newAnswer);
        assertThat(this.round.guess(this.playerOne, this.playerTwo, newAnswer), is(true));
        assertThat(this.round.guess(this.playerOne, this.playerThree, newAnswer), is(true));
    }

    private boolean makeIncorrectGuess() {
        return this.round.guess(this.round.getCurrentTurn(), this.playerTwo, randomAlphanumeric(12));
    }

    private void submitAllPlayersAnswers() {
        this.round.submitAnswer(this.playerOne, this.playerOneAnswer);
        this.round.submitAnswer(this.playerTwo, this.playerTwoAnswer);
        this.round.submitAnswer(this.playerThree, this.playerThreeAnswer);
    }

    private Player randomPlayer() {
        return new Player(randomAlphanumeric(10));
    }

    private String randomAnswer() {
        return randomAlphanumeric(10);
    }
}
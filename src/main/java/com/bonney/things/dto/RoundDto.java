package com.bonney.things.dto;

import java.util.Collection;

public class RoundDto {

    private int roundNumber;
    private String question;
    private Collection<String> unguessedAnswers;
//    private Map<PlayerDto, String> guessedAnwsers;
    private PlayerDto currentTurn;

    public RoundDto() {
        super();
    }

    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }

    public void setUnguessedAnswers(Collection<String> unguessedAnswers) {
        this.unguessedAnswers = unguessedAnswers;
    }

    public Collection<String> getUnguessedAnswers() {
        return unguessedAnswers;
    }

//    public void setGuessedAnwsers(Map<PlayerDto, String> guessedAnwsers) {
//        this.guessedAnwsers = guessedAnwsers;
//    }
//
//    public Map<PlayerDto, String> getGuessedAnwsers() {
//        return guessedAnwsers;
//    }

    public void setCurrentTurn(PlayerDto currentTurn) {
        this.currentTurn = currentTurn;
    }

    public PlayerDto getCurrentTurn() {
        return currentTurn;
    }
}

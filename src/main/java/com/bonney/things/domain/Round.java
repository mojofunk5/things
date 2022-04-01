package com.bonney.things.domain;

import static java.util.stream.Collectors.toMap;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.bonney.things.dto.RoundDto;
import com.google.common.collect.ImmutableList;

public class Round {

    private final int roundNumber;
    private final String question;
    private final List<Player> players;
    private final Map<Player, String> remainingAnswersByPlayer;
    private final Map<Player, String> guessedAnswersByPlayer;
    private final Map<Player, AtomicInteger> scoresByPlayer;
    private final AtomicInteger currentTurn;

    public Round(int roundNumber, List<Player> players, String question) {
        this.roundNumber = roundNumber;
        this.question = question;
        this.players = ImmutableList.copyOf(players);
        this.remainingAnswersByPlayer = new HashMap<>();
        this.guessedAnswersByPlayer = new HashMap<>();
        this.scoresByPlayer = new HashMap<>();
        this.players.forEach(p -> this.scoresByPlayer.put(p, new AtomicInteger(0)));
        this.currentTurn = new AtomicInteger((this.roundNumber - 1) % this.players.size());
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public String getQuestion() {
        return question;
    }

    public boolean guess(Player guessingPlayer, Player suspectedAuthor, String answer) {
        boolean correctGuess = this.remainingAnswersByPlayer.get(suspectedAuthor).equals(answer);

        if (correctGuess) {
            this.remainingAnswersByPlayer.remove(suspectedAuthor);
            this.guessedAnswersByPlayer.put(suspectedAuthor, answer);
            this.scoresByPlayer.get(guessingPlayer).incrementAndGet();
        } else {
            moveTurnOn();
        }

        return correctGuess;
    }

    public boolean everyPlayerAnswered() {
        return (this.remainingAnswersByPlayer.size() + this.guessedAnswersByPlayer.size()) == numberPlayers();
    }

    private int numberPlayers() {
        return this.players.size();
    }

    public void submitAnswer(Player player, String answer) {
        this.remainingAnswersByPlayer.put(player, answer);
    }

    public void markDuplicateAnswers(List<Player> players, String newAnswer) {
        players.stream().forEach(p -> submitAnswer(p, newAnswer));
    }

    public Player getCurrentTurn() {
        return this.players.get(this.currentTurn.get());
    }

    public Collection<String> remainingAnswers() {
        return this.remainingAnswersByPlayer.values();
    }

    public Map<Player, String> guessedAnswers() {
        return this.guessedAnswersByPlayer;
    }

    public Map<Player, Integer> getScores() {
        return this.scoresByPlayer.entrySet()
                .stream()
                .collect(toMap(Map.Entry::getKey, e -> Integer.valueOf(e.getValue().get())));
    }

    public boolean isFinished() {
        return everyPlayerAnswered() && this.remainingAnswersByPlayer.size() <= 1;
    }

    public RoundDto toDto() {
        RoundDto dto = new RoundDto();
        dto.setRoundNumber(this.roundNumber);
        dto.setQuestion(this.question);
        if (!isFinished()) {
            dto.setCurrentTurn(getCurrentTurn().toDto());
            dto.setUnguessedAnswers(remainingAnswers());
        }
        return dto;
    }

    private void moveTurnOn() {
        int nextTurn = this.currentTurn.incrementAndGet();

        if (nextTurn == numberPlayers()) {
            this.currentTurn.set(0);
        }
    }
}

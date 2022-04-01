package com.bonney.things.state;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.bonney.things.domain.Player;
import com.bonney.things.domain.Questions;
import com.bonney.things.domain.Round;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

public class Context {

    private final UUID id;
    private final List<Player> players;
    private final Questions questions;
    private final List<Round> rounds;

    Context() {
        this.id = UUID.randomUUID();
        this.players = new ArrayList<>();
        this.questions = new Questions();
        this.rounds = new ArrayList<>();
    }

    public UUID getId() {
        return id;
    }

    public ImmutableList<Player> getPlayers() {
        return ImmutableList.copyOf(this.players);
    }

    public int getNumberPlayers() {
        return this.players.size();
    }

    public int getNumberRounds() {
        return this.rounds.size();
    }

    public Round newRound() {
        int newRoundNumber = this.rounds.size() + 1;
        Round round = new Round(newRoundNumber, getPlayers(), nextQuestion());
        this.rounds.add(round);
        return round;
    }

    public List<Round> getPlayedRounds() {
        return this.rounds.stream().filter(Round::isFinished).collect(toList());
    }

    public Optional<Round> getCurrentRound() {
        if (this.rounds.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(Iterables.getLast(this.rounds));
    }

    void addPlayer(Player player) {
        this.players.add(player);
    }

    void shufflePlayers() {
        Collections.shuffle(this.players);
    }

    private String nextQuestion() {
        return this.questions.nextQuestion();
    }
}
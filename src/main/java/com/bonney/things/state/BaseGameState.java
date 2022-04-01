package com.bonney.things.state;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.bonney.things.domain.Player;
import com.bonney.things.domain.Round;
import com.bonney.things.dto.GameDto;
import com.google.common.collect.ImmutableList;

abstract class BaseGameState implements Game {

    private final Context context;

    BaseGameState(Context context) {
        this.context = context;
    }

    @Override
    public UUID getId() {
        return getContext().getId();
    }

    @Override
    public Game add(Player player) {
        throw new IllegalStateException("Can only add players at the start of the game");
    }

    @Override
    public Game submitAnswer(Player player, String answer) {
        throw new IllegalStateException("Not in a state where currently accepting answers");
    }

    @Override
    public Game markDuplicateAnswers(List<Player> players, String answer) {
        throw new IllegalStateException("Can only mark answers as duplicate when setting up the round");
    }

    @Override
    public Game guessAnswer(Player guessingPlayer, Player suspectedAuthor, String answer) {
        throw new IllegalStateException("Not currently playing a round so can't submit a guess");
    }

    @Override
    public Game newRound() {
        throw new IllegalStateException("Can only create a new round once the current round is finished");
    }

    @Override
    public Game startRound() {
        throw new IllegalStateException(
                "Can only start the new round once all the duplicate answers have been identified");
    }

    @Override
    public Game finishGame() {
        throw new IllegalStateException("Can only finish game at the end of a round");
    }

    @Override
    public ImmutableList<Player> getPlayers() {
        return this.context.getPlayers();
    }

    @Override
    public int getNumberPlayers() {
        return this.context.getNumberPlayers();
    }

    @Override
    public Optional<String> getCurrentQuestion() {
        return getContext().getCurrentRound().map(Round::getQuestion);
    }

    @Override
    public Optional<Player> getCurrentTurn() {
        return getContext().getCurrentRound().map(Round::getCurrentTurn);
    }

    @Override
    public GameDto toDto() {
        GameDto dto = new GameDto();
        dto.setId(context.getId());
        dto.setPlayers(context.getPlayers().stream().map(Player::toDto).collect(toList()));
        dto.setPlayedRounds(context.getPlayedRounds().stream().map(Round::toDto).collect(toList()));
        context.getCurrentRound().ifPresent(currentRound -> dto.setCurrentRound(currentRound.toDto()));
        dto.setFinished(getClass().equals(GameFinished.class));
        return dto;
    }

    Context getContext() {
        return this.context;
    }

    void internalAddPlayer(Player player) {
        this.context.addPlayer(player);
    }
}

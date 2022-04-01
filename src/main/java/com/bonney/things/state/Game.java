package com.bonney.things.state;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.bonney.things.domain.Player;
import com.bonney.things.dto.GameDto;
import com.google.common.collect.ImmutableList;

public interface Game {

    UUID getId();

    Game add(Player player);

    Game newRound();

    Game startRound();

    ImmutableList<Player> getPlayers();

    Optional<String> getCurrentQuestion();

    Optional<Player> getCurrentTurn();

    Game submitAnswer(Player player, String answer);

    Game markDuplicateAnswers(List<Player> players, String answer);

    Game guessAnswer(Player guessingPlayer, Player suspectedAuthor, String answer);

    Game finishGame();

    int getNumberPlayers();

    GameDto toDto();

    static Game newGame() {
        return new PlayersJoining();
    }
}

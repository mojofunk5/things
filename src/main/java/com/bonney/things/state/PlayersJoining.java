package com.bonney.things.state;

import com.bonney.things.domain.Player;

public class PlayersJoining extends BaseGameState {

    PlayersJoining() {
        super(new Context());
    }

    @Override
    public Game add(Player player) {
        internalAddPlayer(player);
        return this;
    }

    @Override
    public Game newRound() {
        if (getNumberPlayers() < 2) {
            throw new IllegalStateException("Need at least 2 players to start the round");
        }

        Context context = getContext();
        context.shufflePlayers();
        return new WaitingForAnswers(context);
    }
}

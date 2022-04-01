package com.bonney.things.domain;

import java.time.Duration;
import java.util.UUID;

import com.bonney.things.state.Game;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public class Games {

    private final Cache<UUID, Game> games;

    public Games() {
        this.games = CacheBuilder.newBuilder().expireAfterAccess(Duration.ofDays(30)).build();
    }

    public UUID newGame() {
        Game game = Game.newGame();
        this.games.put(game.getId(), game);
        return game.getId();
    }

    public Game get(UUID gameId) {
        return this.games.getIfPresent(gameId);
    }

    public void update(UUID gameId, Game newState) {
        this.games.put(gameId, newState);
    }
}

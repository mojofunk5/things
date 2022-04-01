package com.bonney.things.dto;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GameDto {

    private UUID id;
    private List<PlayerDto> players;
    private List<RoundDto> playedRounds;
    private RoundDto currentRound;
    private boolean finished;

    public GameDto() {
        super();
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public void setPlayers(List<PlayerDto> players) {
        this.players = players;
    }

    public List<PlayerDto> getPlayers() {
        return players;
    }

    public void setPlayedRounds(List<RoundDto> playedRounds) {
        this.playedRounds = playedRounds;
    }

    public List<RoundDto> getPlayedRounds() {
        return playedRounds;
    }

    public void setCurrentRound(RoundDto currentRound) {
        this.currentRound = currentRound;
    }

    public RoundDto getCurrentRound() {
        return currentRound;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public boolean isFinished() {
        return finished;
    }
}

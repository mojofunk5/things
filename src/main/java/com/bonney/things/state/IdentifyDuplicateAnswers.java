package com.bonney.things.state;

import java.util.List;

import com.bonney.things.domain.Player;
import com.bonney.things.domain.Round;

public class IdentifyDuplicateAnswers extends BaseGameState {

    private final Round round;

    IdentifyDuplicateAnswers(Context context, Round round) {
        super(context);
        this.round = round;
    }

    @Override
    public Game markDuplicateAnswers(List<Player> players, String answer) {
        this.round.markDuplicateAnswers(players, answer);
        return this;
    }

    @Override
    public Game startRound() {
        return new RoundInProgress(getContext(), this.round);
    }
}

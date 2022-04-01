package com.bonney.things.state;

import com.bonney.things.domain.Player;
import com.bonney.things.domain.Round;

public class RoundInProgress extends BaseGameState {

    private final Round round;

    RoundInProgress(Context context, Round round) {
        super(context);
        this.round = round;
    }

    @Override
    public Game guessAnswer(Player guessingPlayer, Player suspectedAuthor, String answer) {
        this.round.guess(guessingPlayer, suspectedAuthor, answer);

        if (this.round.isFinished()) {
            return new RoundFinished(getContext());
        }
        return this;
    }
}
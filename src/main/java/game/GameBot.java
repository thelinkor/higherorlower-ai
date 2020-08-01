package game;

import player.action.ActionProvider;
import player.observers.ResultObserver;

public class GameBot {
    public static final int MAX_NUMBER_OF_MOVES = 1_000;

    private final HighOrLowGame game;
    private final ActionProvider actionProvider;
    private final ResultObserver resultObserver;

    public GameBot(ActionProvider actionProvider, ResultObserver resultObserver) {
        game = new HighOrLowGame();
        this.actionProvider = actionProvider;
        this.resultObserver = resultObserver;
    }

    public int getScore(){
        for(int i = 1 ; i < MAX_NUMBER_OF_MOVES ; i++){
            int guess = actionProvider.guess();
            int result = game.makeGuess(guess);
            resultObserver.observe(result,guess);
            if (result == 0) {
                game.reset();
                return i;
            }
        }
        game.reset();
        return MAX_NUMBER_OF_MOVES+1;
    }
}
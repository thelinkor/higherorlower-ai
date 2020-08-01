package player.observers;

import player.observers.ResultObserver;

public class ConsoleObserver implements ResultObserver {

    @Override
    public void observe(int gameResult, int guess) {
        System.out.print("Guess: " + guess + "  ");
        if(gameResult > 0)
            System.out.println("Low");
        else if(gameResult  < 0)
            System.out.println("High");
        else
            System.out.println("Perfect");
    }
}

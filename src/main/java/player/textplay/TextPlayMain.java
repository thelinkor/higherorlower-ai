package player.textplay;

import game.GameBot;
import player.action.ConsoleAction;
import player.observers.ConsoleObserver;

public class TextPlayMain {
    public static void main(String[] args) {
        int score = new GameBot(new ConsoleAction(),new ConsoleObserver()).getScore();
        System.out.println("Won the game in " + score  + " rounds");
    }
}

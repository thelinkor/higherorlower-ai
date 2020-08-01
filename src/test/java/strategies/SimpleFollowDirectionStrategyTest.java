package strategies;

import game.GameBot;
import game.HighOrLowGame;
import org.junit.Test;
import player.action.ActionProvider;
import player.observers.MultiObserve;
import player.observers.ResultObserver;
import player.observers.ResultRecorder;
import player.observers.ConsoleObserver;

public class SimpleFollowDirectionStrategyTest {



    @Test
    public void textShowStrategy(){
        ResultRecorder recorder = new ResultRecorder();
        ResultObserver gameObservers = new MultiObserve(recorder, new ConsoleObserver());
        ActionProvider strategy = new SimpleFollowDirectionStrategy(HighOrLowGame.DEFAULT_HIGHEST_BOUND/2,recorder);
        int score = new GameBot(strategy,gameObservers).getScore();
        System.out.println("Game finished with score: " + score);
    }

    @Test
    public void evaluateStrategy(){
        ResultRecorder recorder = new ResultRecorder();
        Strategy strategy = new SimpleFollowDirectionStrategy(HighOrLowGame.DEFAULT_HIGHEST_BOUND/2,recorder);
        StrategyEvaluator strategyEvaluator = StrategyEvaluator.evaluatorNoObserver(strategy);
        System.out.println("Evaluation score: " + strategyEvaluator.evaluateStrategy());
    }

}
package strategies;

import game.GameBot;
import player.observers.ConsoleObserver;
import player.observers.MultiObserve;
import player.observers.ResultObserver;

public class StrategyEvaluator {
    public static final int ROUNDS_TO_EVALUATE_STRATEGY = 50_000;

    private final GameBot strategyEvaluationBot;
    private final Strategy strategy;

    private StrategyEvaluator(Strategy strategy){
        this.strategy = strategy;
        strategyEvaluationBot = new GameBot(strategy,strategy.connectedObserver());
    }

    private StrategyEvaluator(Strategy strategy, ResultObserver extraObserver){
        this.strategy = strategy;
        strategyEvaluationBot = new GameBot(strategy,new MultiObserve(strategy.connectedObserver(), extraObserver));
    }

    public static StrategyEvaluator evaluatorNoObserver(Strategy strategy){
        return new StrategyEvaluator(strategy);
    }

    public static StrategyEvaluator evaluatorWithTextObserver(Strategy strategy){
        return new StrategyEvaluator(strategy, new ConsoleObserver());
    }


    public double evaluateStrategy(){
        long totalScore = 0;
        for(int i = 0 ; i < ROUNDS_TO_EVALUATE_STRATEGY ; i++){
            totalScore += strategyEvaluationBot.getScore();
            strategy.reset();
        }
        return ((double)totalScore)/ROUNDS_TO_EVALUATE_STRATEGY;
    }

}

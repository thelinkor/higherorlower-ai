package jeneticsai;

import strategies.PriorityChoiceStrategy;
import strategies.SimpleFollowDirectionStrategy;
import strategies.Strategy;
import strategies.StrategyEvaluator;

public class Fitness {
    public static Double simpleFollowDirectionEvaluation(int startBet){
        return StrategyEvaluator
                .evaluatorNoObserver(new SimpleFollowDirectionStrategy(startBet))
                .evaluateStrategy();
    }

    public static Double priorityChoiceStrategyEvaluation(int[] priorities){
        return StrategyEvaluator
                .evaluatorNoObserver(new PriorityChoiceStrategy(priorities))
                .evaluateStrategy();
    }
}

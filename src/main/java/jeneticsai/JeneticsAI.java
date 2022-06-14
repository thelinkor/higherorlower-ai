package jeneticsai;

import game.HighOrLowGame;
import io.jenetics.*;
import io.jenetics.engine.*;
import io.jenetics.util.IntRange;

public class JeneticsAI {


    public static void main(String[] args) {
        evaluatePriorityChoiceStrategy();
    }


    public static void evaluatePriorityChoiceStrategy(){
        IntRange[] initialChoices = new IntRange[HighOrLowGame.DEFAULT_HIGHEST_BOUND];
        for(int i = 0 ; i < initialChoices.length ;i++){
            initialChoices[i] = IntRange.of(0,HighOrLowGame.DEFAULT_HIGHEST_BOUND);
        }

        final Engine<IntegerGene,Double> engine = Engine
                .builder(Fitness::priorityChoiceStrategyEvaluation,
                        Codecs.ofVector(initialChoices))
                .populationSize(100)
                .optimize(Optimize.MINIMUM)
                .selector(new TournamentSelector<>())
                .alterers(new Mutator<>(0.04)
                        ,   new SinglePointCrossover<>(0.6))
                .build();

        final EvolutionStatistics<Double,?> statistics = EvolutionStatistics.ofNumber();

        final Phenotype<IntegerGene, Double> best =
                engine.stream()
                        .limit(Limits.bySteadyFitness(20))
                        .limit(200)
                        .peek(statistics)
                        .collect(EvolutionResult.toBestPhenotype());

        System.out.println(statistics);
        System.out.println(best);
        System.out.println("Fitness: " + best.fitness());
    }

    public static void evaluateFollowDirection(){
        final Engine<IntegerGene,Double> engine = Engine
                .builder(Fitness::simpleFollowDirectionEvaluation,
                        Codecs.ofScalar(IntRange.of(0,HighOrLowGame.DEFAULT_HIGHEST_BOUND)))
                .populationSize(100)
                .optimize(Optimize.MINIMUM)
                .selector(new TournamentSelector<>())
                .alterers(new Mutator<>(0.55)
                        ,   new SinglePointCrossover<>(0.1))
                .build();

        final EvolutionStatistics<Double,?> statistics = EvolutionStatistics.ofNumber();

        final Phenotype<IntegerGene, Double> best =
                engine.stream()
                        .limit(Limits.bySteadyFitness(8))
                        .limit(100)
                        .peek(statistics)
                        .collect(EvolutionResult.toBestPhenotype());

        System.out.println(statistics);
        System.out.println(best);
        System.out.println("Optimal Strarting guess:  " + best.genotype().gene().allele()
                + "  Fitness: " + best.fitness());
    }

}

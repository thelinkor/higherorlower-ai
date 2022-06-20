package neurlanet.reinforced;

import game.GameBot;
import game.HighOrLowGameWithState;
import neurlanet.NNGameBotProvider;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.common.util.ArrayUtil;
import org.nd4j.linalg.api.ndarray.INDArray;

import java.util.Arrays;

import static neurlanet.reinforced.NNWholeRoundScoreMethod.*;

public class HoLReinforcementTrainer {
    public static final int SIZE_OF_GAME = 31;
    public static final int NUMBER_OF_GAME_ROUNDS  = 20_000;

    public static void  main(String[] args){
        MultiLayerNetwork net = trainMultiLayerNetwork();
        testNet(net);

    }

    public static MultiLayerNetwork trainMultiLayerNetwork(){
        HighOrLowGameWithState game = new HighOrLowGameWithState(SIZE_OF_GAME);
        MultiLayerNetwork network = NNWholeRoundScoreMethod.createNet(SIZE_OF_GAME);

        for(int i = 0 ; i < NUMBER_OF_GAME_ROUNDS ; i++){
            game.reset();
            int tryCount = 0;
            while(!game.isSolutionFound() && tryCount< SIZE_OF_GAME){
                tryCount++;

                int[] oldBoardState = ArrayUtil.copy(game.getCurrentBoardState());
                int nextMove = NNWholeRoundScoreMethod.getNextMoveFromNetwork(network, oldBoardState);
                int oldGoodChoices = game.getNumberOfGoodChoices();
                int guessResult  = game.makeGuess(nextMove);


                double scoreForMove =                       guessResult == 0 ? 1.0/(tryCount+1) :
                             oldGoodChoices == game.getNumberOfGoodChoices() ? -1 :
                                    1.0/((SIZE_OF_GAME-(oldGoodChoices-game.getNumberOfGoodChoices()))*tryCount);



                updateNetwork(oldBoardState, network, scoreForMove);
            }
        }

        return network;
    }

    public static void updateNetwork(int[] oldBoardState, MultiLayerNetwork network, double scoreForMove){
        INDArray stateBefore =  toINDArray(oldBoardState);
        INDArray output = network.output(stateBefore);
        INDArray updateOutput = output.putScalar(getMaxValueIndex(output.toFloatVector()), scoreForMove);

        network.fit(stateBefore, updateOutput);
    }


    private static void testNet(MultiLayerNetwork net){
        NNGameBotProvider gameBotProvider = new NNProbabilisticGameBotProvider(net, SIZE_OF_GAME);
        int[] scores = new int[SIZE_OF_GAME];
        for(int i = 0 ; i < SIZE_OF_GAME ; i++) {
            GameBot gameBot = new GameBot(gameBotProvider,gameBotProvider, SIZE_OF_GAME, i);
            scores[i] = gameBot.getScore();
            System.out.println("Score: "+ scores[i]);
            gameBotProvider.reset();
        }
        System.out.println(Arrays.toString(scores));
        System.out.println("ScoreSum: "+ Arrays.stream(scores).sum());
    }
}

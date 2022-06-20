package neurlanet;

import game.GameBot;
import game.HighOrLowGame;
import org.deeplearning4j.datasets.iterator.utilty.ListDataSetIterator;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Nesterovs;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import java.util.*;

public class NNSingleBoardWorker {
    public static final long DATA_GEN_SEED = 51L;
    public static final double LEARNING_RATE = 0.0015;
    public static final int NUM_INPUT = 15;
    public static final int BATCH_SIZE = 2_000;
    public static final int NUMBER_ROUNDS_TO_GENERATE = 200_000;

    public static DataSetIterator generateRounds(int roundsToGenerate, int sizeOfGame, int batchSize) {
        Random random = new Random(DATA_GEN_SEED);
        HighOrLowGame game = new HighOrLowGame(sizeOfGame, random);

        double[][] input = new double[sizeOfGame][roundsToGenerate];
        double[] output = new double[roundsToGenerate];

        for (int i = 0; i < roundsToGenerate; i++) {
            output[i] = game.getTargetNumber();
            int lengthOfGame = random.nextInt(sizeOfGame);
            for (int j = 0; j < sizeOfGame; j++) {
                input[j][i] = 0;
            }
            for(int j = 0 ; j < lengthOfGame ; j++){
                int guess  = random.nextInt(sizeOfGame);
                input[guess][i] = game.makeGuess(guess);
                if(input[guess][i] == 0)
                    break;
            }
            game.reset();
        }
        List<INDArray> inputList = new ArrayList<>();
        for (int j = 0; j < sizeOfGame; j++) {
            inputList.add(Nd4j.create(input[j], roundsToGenerate, 1));
        }
        INDArray inputNDArray = Nd4j.hstack(inputList);
        INDArray outPut = Nd4j.create(output, roundsToGenerate, 1);
        DataSet dataSet = new DataSet(inputNDArray, outPut);
        List<DataSet> listDs = dataSet.asList();
        Collections.shuffle(listDs, new Random());
        return new ListDataSetIterator<>(listDs, batchSize);
    }

    private static MultiLayerNetwork createNet(int numInput){
        int numOutputs = 1;
        int nHidden = numInput;
        int layerCount = 0;
        MultiLayerNetwork net = new MultiLayerNetwork(new NeuralNetConfiguration.Builder()
                .seed(12345)
                .weightInit(WeightInit.XAVIER)
                .updater(new Nesterovs(LEARNING_RATE, 0.9))
                .list()
                .layer(layerCount++, new DenseLayer.Builder().nIn(numInput).nOut(nHidden)
                        .activation(Activation.RELU)
                        .build())
                .layer(layerCount++, new DenseLayer.Builder().nIn(numInput).nOut(nHidden)
                        .activation(Activation.TANH)
                        .build())
                .layer(layerCount++, new DenseLayer.Builder().nIn(numInput).nOut(nHidden)
                        .activation(Activation.RELU)
                        .build())
                .layer(layerCount++, new DenseLayer.Builder().nIn(numInput).nOut(nHidden)
                        .activation(Activation.TANH)
                        .build())
                .layer(layerCount++, new DenseLayer.Builder().nIn(numInput).nOut(nHidden)
                        .activation(Activation.RELU)
                        .build())

                .layer(layerCount++, new OutputLayer.Builder(LossFunctions.LossFunction.MSE)
                        .activation(Activation.IDENTITY)
                        .nIn(nHidden).nOut(numOutputs).build())
                .build()
        );
        net.init();
        net.setListeners(new ScoreIterationListener(100));
        return net;
    }

    public static void main(String[] args){
        int numInput = NUM_INPUT;
        int batchSize = BATCH_SIZE;
        //Generate the training data
        DataSetIterator iterator = generateRounds(NUMBER_ROUNDS_TO_GENERATE, numInput, batchSize);
        MultiLayerNetwork net = createNet(numInput);

        for (int i = 0; i < 100; i++) {
            iterator.reset();
            net.fit(iterator);
        }

        NNGameBotProvider gameBotProvider = new NNGameBotProvider(net, numInput);
        int[] scores = new int[numInput];
        for(int i = 0 ; i < numInput ; i++) {
            GameBot gameBot = new GameBot(gameBotProvider,gameBotProvider, numInput, i);
            scores[i] = gameBot.getScore();
            System.out.println("Score: "+ scores[i]);
            gameBotProvider.reset();
        }
        System.out.println(Arrays.toString(scores));
        System.out.println("ScoreSum: "+ Arrays.stream(scores).sum());
    }
}

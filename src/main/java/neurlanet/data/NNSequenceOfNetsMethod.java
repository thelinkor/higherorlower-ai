package neurlanet.data;

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
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

public class NNSequenceOfNetsMethod {
    public static final int TOTAL_NN_ITERATIONS = 4;
/*
    private final int sizeOfTheGame;
    private final int[] numberOfDataSetsForRounds;
    private final int totalNumberOfdata;
    private final Random random;

    public NNDataGeneration(int sizeOfTheGame, int[] numberOfRounds) {
        this.sizeOfTheGame = sizeOfTheGame;
        this.numberOfDataSetsForRounds = numberOfRounds;
        totalNumberOfdata = Arrays.stream(numberOfRounds).sum();
        this.random = new Random();
    }
*/
    public static DataSetIterator generateFirstRound(int roundsToGenerate, int sizeOfGame, int batchSize) {
        double[][] input = new double[sizeOfGame][roundsToGenerate];
        double[] output = new double[roundsToGenerate];
        Random random = new Random();

        for (int i = 0; i < roundsToGenerate; i++) {
            output[i] = random.nextInt(sizeOfGame);
            for (int j = 0; j < sizeOfGame; j++) {
                input[j][i] = 0;
            }
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

    public static DataSetIterator generateSecondRound(MultiLayerNetwork net, int roundsToGenerate, int sizeOfGame, int batchSize, int numberOfGameSteps) {
        double[][] input = new double[sizeOfGame][roundsToGenerate];
        double[] output = new double[roundsToGenerate];
        HighOrLowGame highOrLowGame = new HighOrLowGame(sizeOfGame);
        double[] thisRound = new double[sizeOfGame];

        for (int i = 0; i < roundsToGenerate; i++) {
            output[i] = highOrLowGame.getTargetNumber();
            for (int j = 0; j < sizeOfGame; j++) {
                input[j][i] = 0;
                thisRound[j] = 0;
            }
            for(int gameRound = 0 ; gameRound < numberOfGameSteps ; gameRound++) {
                int choice = (int) Math.round(net.output(Nd4j.create(thisRound, 1, thisRound.length), false).getDouble(0));
                if (choice != highOrLowGame.getTargetNumber()) {
                    input[choice][i] = highOrLowGame.makeGuess(choice);
                    thisRound[choice] = input[choice][i];
                }
            }
            highOrLowGame.reset();
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


    public static void main(String[] args) {
        int numInput = 31;
        int batchSize = 500;
        //Generate the training data
        DataSetIterator iterator = generateFirstRound(10_000, numInput, batchSize);
        MultiLayerNetwork net = createNet(numInput);


        //Train the network on the full data set, and evaluate in periodically
        for (int i = 0; i < 100; i++) {
            iterator.reset();
            net.fit(iterator);
        }

        DataSetIterator secondRound;
        MultiLayerNetwork secondNet = null;
        for(int numberLearningSteps = 1 ; numberLearningSteps < Math.log(numInput)/Math.log(2) + 1 ; numberLearningSteps++) {
            secondRound = generateSecondRound(net, 10_000, numInput, batchSize, numberLearningSteps);
            secondNet = createNet(numInput);
            for (int i = 0; i < 60; i++) {
                secondRound.reset();
                secondNet.fit(secondRound);
            }
            net = secondNet;
        }
        // Test the addition of 2 numbers (Try different numbers here)

        testNumbers(net, IntStream.range(0, numInput).map(x -> 0).asDoubleStream().toArray());
        testNumbers(secondNet, IntStream.range(0, numInput).map(x -> 0).asDoubleStream().toArray());
        double[] v = IntStream.range(0, numInput).map(x -> 0).asDoubleStream().toArray();
        v[7] = 1;
        testNumbers(secondNet, v);
        double[] u = IntStream.range(0, numInput).map(x -> 0).asDoubleStream().toArray();
        u[7] = -1;
        testNumbers(secondNet, u);

        u = IntStream.range(0, numInput).map(x -> 0).asDoubleStream().toArray();
        u[7] = -1;
        u[3] = -1;
        testNumbers(secondNet, u);

        u = IntStream.range(0, numInput).map(x -> 0).asDoubleStream().toArray();
        u[7] = -1;
        u[3] = 1;
        testNumbers(secondNet, u);

        u = IntStream.range(0, numInput).map(x -> 0).asDoubleStream().toArray();
        u[7] = -1;
        u[3] = 1;
        u[5] = -1;
        testNumbers(secondNet, u);

        NNGameBotProvider gameBotProvider = new NNGameBotProvider(net, numInput);
        GameBot gameBot = new GameBot(gameBotProvider,gameBotProvider, numInput);
        for(int i = 0 ; i < 20 ; i++) {
            System.out.println("Score: "+ gameBot.getScore());
            gameBotProvider.reset();
        }

    }

    private static void testNumbers(MultiLayerNetwork net, double... round) {
        final INDArray input = Nd4j.create(round, 1, round.length);
        INDArray out = net.output(input, false);
        System.out.println(Math.round(out.getDouble(0)));
    }

    private static MultiLayerNetwork createNet(int numInput){
        int numOutputs = 1;
        int nHidden = numInput;
        int layerCount = 0;
        MultiLayerNetwork net = new MultiLayerNetwork(new NeuralNetConfiguration.Builder()
                .seed(1234)
                .weightInit(WeightInit.XAVIER)
                .updater(new Nesterovs(0.00025, 0.9))
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
}

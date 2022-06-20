package neurlanet.reinforced;

import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.BackpropType;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import java.util.Random;

public class NNWholeRoundScoreMethod {
    public static final double LEARNING_RATE = 0.00008;
    public static final int PRINT_ITERATIONS_COUNT = 2_500;
    public static final int NUMBER_OF_HIDDEN_LAYERS = 200;


    public static MultiLayerNetwork createNet(int numInput){
        int numOutputs = numInput;
        int nHidden = NUMBER_OF_HIDDEN_LAYERS;
        int layerCount = 0;
        MultiLayerNetwork net = new MultiLayerNetwork(new NeuralNetConfiguration.Builder()
                .seed(12345)
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                .weightInit(WeightInit.XAVIER)
                .updater(new Adam(LEARNING_RATE))
                .list()
                .layer(layerCount++, new DenseLayer.Builder().nIn(numInput).nOut(nHidden)
                        .activation(Activation.RELU)
                        .build())
                .layer(layerCount++, new DenseLayer.Builder().nIn(numInput).nOut(nHidden)
                        .activation(Activation.RELU)
                        .build())
                .layer(layerCount++, new DenseLayer.Builder().nIn(numInput).nOut(nHidden)
                        .activation(Activation.RELU)
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
                .backpropType(BackpropType.Standard)
                .build()
        );
        net.init();
        net.setListeners(new ScoreIterationListener(PRINT_ITERATIONS_COUNT));
        return net;
    }

    public static final Random epsilonRandom = new Random();
    private static double epsilon = 1.0;
    public static int getNextMoveFromNetwork(MultiLayerNetwork network, int[] gameState){
        INDArray output  = network.output(toINDArray(gameState),false);

        final float[] outputValues = output.data().asFloat();
        int outPutMove;
        if(epsilon > epsilonRandom.nextDouble()){
            outPutMove = epsilonRandom.nextInt(gameState.length);
            epsilon -= 0.00125;
        }else{
            outPutMove = getMaxValueIndex(outputValues, gameState);
        }

        return outPutMove;
    }


    /*
     * UTILS
     */

    public static INDArray toINDArray(int[] gameState){
        return Nd4j.create(new int[][]{gameState});
    }

    public static int getMaxValueIndex(final float[] values) {
        int maxAt = 0;

        for (int i = 0; i < values.length; i++) {
            maxAt = values[i] > values[maxAt] ? i : maxAt;
        }

        return maxAt;
    }

    public static int getMaxValueIndex(final float[] values, final int[] currentStates) {
        int maxAt = -1;
        float maxValue = Integer.MIN_VALUE;

        for (int i = 0; i < values.length; i++) {
            if(currentStates[i] == 0 &&  values[i] > maxValue) {
                maxAt = i;
                maxValue = values[i];
            }
        }

        return maxAt;
    }
}

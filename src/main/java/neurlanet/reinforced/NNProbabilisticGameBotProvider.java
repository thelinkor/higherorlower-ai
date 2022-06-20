package neurlanet.reinforced;

import neurlanet.NNGameBotProvider;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;

import java.util.Arrays;

import static neurlanet.reinforced.NNWholeRoundScoreMethod.getMaxValueIndex;
import static neurlanet.reinforced.NNWholeRoundScoreMethod.toINDArray;

public class NNProbabilisticGameBotProvider extends NNGameBotProvider {
    public NNProbabilisticGameBotProvider(MultiLayerNetwork net, int gameSize) {
        super(net, gameSize);
    }

    @Override
    public int guess() {
        int[] currentStates = Arrays.stream(getCurrentSetup()).mapToInt(x->(int)x).toArray();
        INDArray output  = getNet().output(toINDArray(currentStates),false);
        return getMaxValueIndex(output.data().asFloat(), currentStates);
    }
}

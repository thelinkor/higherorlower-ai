package neurlanet;

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.factory.Nd4j;
import player.action.ActionProvider;
import player.observers.ResultObserver;

import java.util.Arrays;
import java.util.stream.IntStream;

public class NNGameBotProvider implements ActionProvider, ResultObserver {
    private final MultiLayerNetwork net;
    private double[] currentSetup;
    private final int gameSize;

    public NNGameBotProvider(MultiLayerNetwork net, int gameSize){
        this.net = net;
        this.gameSize = gameSize;
        reset();
    }

    @Override
    public int guess() {
        return (int)Math.round(net.output(Nd4j.create(currentSetup, 1, currentSetup.length),false).getDouble(0));
    }

    @Override
    public void observe(int gameResult, int guess) {
        currentSetup[guess] = gameResult;
        printCurrent();
        if(gameResult == 0 ){
            System.out.println("FOUND SOLUTION: " + guess );
        }
    }

    public void reset(){
        this.currentSetup = IntStream.range(0,gameSize).map(x -> 0).asDoubleStream().toArray();
        System.out.println("--------- RESET ---------");
    }

    public void printCurrent(){
        System.out.println(Arrays.toString(Arrays.stream(currentSetup).mapToInt(x -> (int)x).toArray()));
    }

    public MultiLayerNetwork getNet() {
        return net;
    }

    public double[] getCurrentSetup() {
        return currentSetup;
    }
}

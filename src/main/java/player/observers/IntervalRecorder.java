package player.observers;

public class IntervalRecorder implements ResultObserver{
    private final int originalMin;
    private final int originalMax;

    private int currentMin;
    private int currentMax;

    public IntervalRecorder(int originalMin, int originalMax) {
        this.originalMin = originalMin;
        this.originalMax = originalMax;
        currentMax = originalMax;
        currentMin = originalMin;
    }

    public IntervalRecorder(int originalMax) {
        this(0,originalMax);
    }

    @Override
    public void observe(int gameResult, int guess) {
        if(gameResult > 0)
            currentMin = guess+1;
        else if(gameResult < 0)
            currentMax = guess;
        else
            reset();
    }

    public void reset(){
        currentMax = originalMax;
        currentMin = originalMin;
    }

    public int getCurrentMin() {
        return currentMin;
    }

    public int getCurrentMax() {
        return currentMax;
    }
}

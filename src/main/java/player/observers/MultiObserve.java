package player.observers;

public class MultiObserve implements ResultObserver{
    private final ResultObserver[] observers;

    public MultiObserve(ResultObserver... observers) {
        this.observers = observers;
    }

    @Override
    public void observe(int gameResult, int guess) {
        for(ResultObserver observer : observers)
            observer.observe(gameResult, guess);
    }
}

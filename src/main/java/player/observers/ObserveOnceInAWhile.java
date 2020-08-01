package player.observers;

public class ObserveOnceInAWhile  implements ResultObserver{
    private final int howOftenToObserve;
    private final ResultObserver observerToUse;

    private int counter;

    public ObserveOnceInAWhile(int howOftenToObserve, ResultObserver observerToUse) {
        this.howOftenToObserve = howOftenToObserve;
        this.observerToUse = observerToUse;
        counter = 0;
    }

    @Override
    public void observe(int gameResult, int guess) {
        counter++;
        if(counter == howOftenToObserve){
            counter = 0;
            observerToUse.observe(gameResult,guess);
        }
    }
}

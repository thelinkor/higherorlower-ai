package player.observers;

public final class DoNotObserve implements ResultObserver{
    @Override
    public void observe(int gameResult, int guess) {
        //Empty On Purpose
    }
}

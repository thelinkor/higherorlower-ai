package strategies;

import player.action.ActionProvider;
import player.observers.ResultObserver;

public interface Strategy extends ActionProvider {
    void reset();

    ResultObserver connectedObserver();
}

package player.observers;

import java.util.ArrayList;
import java.util.List;

public class ResultRecorder implements ResultObserver{
    private final List<Integer> resultList;
    private final List<Integer> guessList;

    public ResultRecorder() {
        resultList = new ArrayList<>();
        guessList = new ArrayList<>();
    }

    @Override
    public void observe(int gameResult,int guess) {
        resultList.add(gameResult);
        guessList.add(guess);
    }

    public List<Integer> getResultList() {
        return resultList;
    }

    public List<Integer> getGuessList() {
        return guessList;
    }

    public Integer getLastGuess(){
        return guessList.get(guessList.size()-1);
    }

    public Integer getLastResult(){
        return resultList.get(resultList.size()-1);
    }

    public void reset(){
        resultList.clear();
        guessList.clear();
    }
}

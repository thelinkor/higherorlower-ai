package strategies;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

public class PriorityChoiceStrategyTest {
    PriorityChoiceStrategy strategy;

    int[] increasingArray;
    int[] decreasingArray;
    int[] multipleOfTheSame;

    @Before
    public void setup(){
        increasingArray = IntStream.range(0,10).toArray();
        decreasingArray = new int[]{5,4,3,2,1,0};
        multipleOfTheSame = new int[]{0,4,5,7,0,4,0,3,2,5,10};

    }

    @Test
    public void testincreasing(){
        strategy = new PriorityChoiceStrategy(increasingArray);
        for(int i = 0 ; i < 10 ; i++){
            Assert.assertEquals(i,strategy.guess());
            strategy.connectedObserver().observe(1,i);
        }
    }

    @Test
    public void testDecreasing(){
        strategy = new PriorityChoiceStrategy(decreasingArray);
        for(int i = 5 ; i >= 0; i--){
            Assert.assertEquals(i,strategy.guess());
            strategy.connectedObserver().observe(-1,i);
        }
    }

    @Test
    public void allPossibleShouldAppearSometimesIfMultipleToChooseFrom(){
        strategy = new PriorityChoiceStrategy(multipleOfTheSame);
        Set<Integer> integerSet = new HashSet<Integer>(3);
        int iterations = 10_000;
        for(int i = 0 ; i < iterations ;i++){
            integerSet.add(strategy.guess());
        }
        Assert.assertEquals(3,integerSet.size());
    }

}
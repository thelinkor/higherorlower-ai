package player.action;

import java.util.Scanner;

public class ConsoleAction implements ActionProvider {
    public static final Scanner input = new Scanner(System.in);

    @Override
    public int guess() {
        System.out.println("Guess: ");
        return input.nextInt();
    }
}

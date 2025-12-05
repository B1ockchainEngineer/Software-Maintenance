package assignment.util;

import java.util.InputMismatchException;
import java.util.Scanner;
import static assignment.util.ConsoleUtil.clearScreen;

public class ValidationUtil {
    // Shared Scanner instance for the whole application.
    // Exposed as public so legacy code that accesses ValidationUtil.scanner still compiles.
    public static final Scanner scanner = new Scanner(System.in);

    public static int intValidation(int startingNum, int endingNum) {
        int input;

        try {
            if (!scanner.hasNextInt()) {
                System.out.println("PLEASE ENTER AN INTEGER INPUT.\n");
                scanner.nextLine(); // Consume bad input
                return -9999;
            }

            input = scanner.nextInt();
            scanner.nextLine();

        } catch (InputMismatchException ex) {
            System.out.println("PLEASE ENTER AN INTEGER INPUT.\n");
            return -9999;
        }

        if (endingNum != 0) {
            if (input < startingNum || input > endingNum) {
                System.out.println("THE INPUT IS OUT OF RANGE, PLEASE INPUT A CORRECT ONE!!\n");
                clearScreen();
                return -9999;
            }
        } else if (startingNum != 0) {
            if (input < startingNum) {
                System.out.println("THE INPUT IS OUT OF RANGE, PLEASE INPUT A CORRECT ONE!!\n");
                clearScreen();
                return -9999;
            }
        }

        return input;
    }

    public static char charValidation() {
        char input;

        try {
            String line = scanner.nextLine();
            if (line.isEmpty()) {
                System.out.println("PLEASE ENTER A CHARACTER INPUT. \n");
                return 'O'; // 'O' used as generic invalid character
            }
            input = line.charAt(0);
        } catch (Exception ex) {
            System.out.println("PLEASE ENTER A CHARACTER INPUT. \n");
            return 'O';
        }

        return Character.toUpperCase(input);
    }

    public static double doubleValidation() {
        double input;

        try {
            if (!scanner.hasNextDouble()) {
                System.out.println("PLEASE ENTER A VALID DOUBLE INPUT. \n");
                scanner.nextLine(); // Consume bad input
                return -9999;
            }
            input = scanner.nextDouble();
            scanner.nextLine(); // Consume the newline character
        } catch (InputMismatchException ex) {
            System.out.println("PLEASE ENTER A VALID DOUBLE INPUT. \n");
            return -9999;
        }

        return input;
    }
}
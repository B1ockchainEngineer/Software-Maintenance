package assignment.util;

import java.util.InputMismatchException;
import java.util.Scanner;
import static assignment.util.ConsoleUtil.clearScreen;
import java.time.LocalDate;
import java.time.DateTimeException;

public class ValidationUtil {
    // Shared Scanner instance for the whole application.
    // Exposed as public so legacy code that accesses ValidationUtil.scanner still compiles.
    public static final Scanner scanner = new Scanner(System.in);

    public static String digitOnlyValidation(int length) {
        String input = scanner.nextLine().trim();

        // Check if empty or contains non-digit characters
        if (input.isEmpty() || !input.matches("\\d+")) {
            System.out.println("PLEASE ENTER DIGITS ONLY (NO SPACES OR LETTERS).\n");
            return null;
        }

        if(length != input.length()){
            System.out.println("THE INPUT LENGTH MUST BE IN " + length + " DIGITS!!\n");
            return null;
        }

        return input;  // valid digit-only string
    }

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

    public static String icValidation() {
        String input = scanner.nextLine().trim();

        // Must be exactly 12 digits
        if (input.length() != 12 || !input.matches("\\d+")) {
            System.out.println("<<< INVALID IC - MUST BE EXACTLY 12 DIGITS ONLY >>>\n");
            return null;
        }

        try {
            int yy = Integer.parseInt(input.substring(0, 2));
            int mm = Integer.parseInt(input.substring(2, 4));
            int dd = Integer.parseInt(input.substring(4, 6));
            int pb = Integer.parseInt(input.substring(6, 8));

            // Place of birth: only 01-16 allowed
            if (pb < 1 || pb > 16) {
                System.out.println("<<< INVALID PLACE OF BIRTH CODE (7th-8th digits): MUST BE 01-16 >>>\n");
                return null;
            }

            LocalDate now = LocalDate.now();
            LocalDate minBirthDateForIC = now.minusYears(12); // anyone born on/after this date +1 day is <12 years old today

            // First try 2000 + yy (21st century)
            try {
                LocalDate birthDate20 = LocalDate.of(2000 + yy, mm, dd);

                // If this person is at least 12 years old today to be accepted as 20xx (they can have IC)
                if (!birthDate20.isAfter(minBirthDateForIC)) {
                    return input;
                }

            } catch (DateTimeException ignored) {
                // Invalid date in 20xx, so force to 19xx
            }

            // Change to 1900 + yy (20th century)
            LocalDate birthDate19 = LocalDate.of(1900 + yy, mm, dd);
            return input;

        } catch (DateTimeException e) {
            System.out.println("<<< INVALID BIRTH DATE (e.g. 30 Feb, 32nd day, or 29 Feb on non-leap year) >>>\n");
            return null;
        } catch (Exception e) {
            System.out.println("<<< INVALID IC FORMAT >>>\n");
            return null;
        }
    }
}
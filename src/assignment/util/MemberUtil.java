package assignment.util;

import java.time.DateTimeException;
import java.time.LocalDate;


import assignment.util.ValidationUtil;

public class MemberUtil {
    /**
     * Helper to validate IC input.
     * Checks for length, numeric format, and valid date of birth.
     * Returns valid IC string or null if invalid.
     */
    public static String icValidation() {
        String input = ValidationUtil.scanner.nextLine().trim();

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

    /**
     * Checks if the name contains only alphabet characters.
     * Returns true if valid, false otherwise.
     */
    public static boolean nameValidation(String name){
        if (name.matches("^[a-zA-Z ]+$")) {
            return true;
        } else {
            System.out.println("Invalid input. Please enter a name with alphabet characters only. \n");
            return false;
        }
    }

    /**
     * Validates Malaysian phone number format.
     * Must start with '01'.
     * Returns valid string or null if invalid.
     */
    public static String hpValidation() {
        String data = ValidationUtil.scanner.nextLine().trim();

        if (!data.matches("\\d+")) {
            System.out.println(MemberConfig.ErrorMessage.INVALID_HP);
            return null;
        }

        if (!data.startsWith("01")) {
            System.out.println(MemberConfig.ErrorMessage.INVALID_HP);
            return null;
        }

        if (data.startsWith("011")) {
            if (data.length() == 11) {
                return data;
            }
        } else {
            if (data.length() == 10) {
                return data;
            }
        }

        System.out.println(MemberConfig.ErrorMessage.INVALID_HP);
        return null;
    }

    /**
     * Asks a Yes/No question and gets user input.
     * Returns 'Y' or 'N'.
     */
    public static char confirmValidation(String question){
        char yesNo;
        do {
            System.out.print(question);
            yesNo = ValidationUtil.charValidation();
            if (yesNo != 'Y' && yesNo != 'N')
                System.out.println("Invalid Option! Please Re-enter!");
        } while (yesNo != 'Y' && yesNo != 'N');

        return yesNo;
    }



}

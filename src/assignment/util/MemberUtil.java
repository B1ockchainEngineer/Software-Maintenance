package assignment.util;

import java.time.DateTimeException;
import java.time.LocalDate;


import assignment.util.config.MemberConfig;

public class MemberUtil {

    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(MemberUtil.class.getName());
    /**
     * Helper to validate IC input.
     * Checks for length, numeric format, and valid date of birth.
     * Returns valid IC string or null if invalid.
     */
    public static String icValidation() {
        String input = ValidationUtil.scanner.nextLine().trim();

        // Check if empty
        if (input.isEmpty()) {
            String errorMsg = "<<< INVALID IC - MUST BE EXACTLY 12 DIGITS ONLY >>>";
            LOGGER.severe(errorMsg);
            ConsoleUtil.delayForLog();
            System.out.println();
            return null;
        }

        // Must be exactly 12 digits
        if (input.length() != 12) {
            String errorMsg = "<<< INVALID IC - MUST BE EXACTLY 12 DIGITS ONLY >>>";
            LOGGER.severe(errorMsg);
            ConsoleUtil.delayForLog();
            System.out.println();
            return null;
        }
        
        // Check if all characters are digits (only check this if length is 12)
        if (!input.matches("\\d{12}")) {
            String errorMsg = "<<< INVALID IC - MUST BE EXACTLY 12 DIGITS ONLY >>>";
            LOGGER.severe(errorMsg);
            ConsoleUtil.delayForLog();
            System.out.println();
            return null;
        }

        try {
            int yy = Integer.parseInt(input.substring(0, 2));
            int mm = Integer.parseInt(input.substring(2, 4));
            int dd = Integer.parseInt(input.substring(4, 6));
            int pb = Integer.parseInt(input.substring(6, 8));

            // Place of birth: only 01-16 allowed
            if (pb < 1 || pb > 16) {
                String errorMsg = "<<< INVALID PLACE OF BIRTH CODE (7th-8th digits): MUST BE 01-16 >>>";
                LOGGER.severe(errorMsg);
            ConsoleUtil.delayForLog();
                System.out.println();
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
                    // Invalid date in 20xx, so try 19xx
                }

                // Try 1900 + yy (20th century)
                // If this doesn't throw an exception, the date is valid and person is definitely >= 12 years old
                LocalDate.of(1900 + yy, mm, dd);
                return input;

            } catch (DateTimeException e) {
                String errorMsg = "<<< INVALID BIRTH DATE (e.g. 30 Feb, 32nd day, or 29 Feb on non-leap year) >>>";
                LOGGER.severe(errorMsg);
            ConsoleUtil.delayForLog();
                System.out.println();
                return null;

        } catch (Exception e) {
            String errorMsg = "<<< INVALID IC FORMAT >>>";
            LOGGER.severe(errorMsg);
            ConsoleUtil.delayForLog();
            System.out.println();
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
            LOGGER.severe("Invalid input. Please enter a name with alphabet characters only. \n");
            ConsoleUtil.delayForLog();
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
            LOGGER.severe(MemberConfig.ErrorMessage.INVALID_HP);
            ConsoleUtil.delayForLog();
            return null;
        }

        if (!data.startsWith("01")) {
            LOGGER.severe(MemberConfig.ErrorMessage.INVALID_HP);
            ConsoleUtil.delayForLog();
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

        LOGGER.severe(MemberConfig.ErrorMessage.INVALID_HP);
        return null;
    }





}

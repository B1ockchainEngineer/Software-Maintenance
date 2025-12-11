package assignment.util;

import java.io.Console;

/**
 * Utility class for password input handling.
 */
public class PasswordUtil {

    /**
     * Reads a password from console.
     * Uses Console.readPassword() if available (hides input in terminals),
     * otherwise falls back to regular Scanner input.
     *
     * @param prompt The prompt to display
     * @return The password string entered by user
     */
    public static String readPassword(String prompt) {
        Console console = System.console();

        if (console != null) {
            // Use Console.readPassword() for native password hiding (works in terminal)
            char[] passwordArray = console.readPassword(prompt);
            return new String(passwordArray);
        } else {
            // Fallback for IDEs that don't support Console
            System.out.print(prompt);
            return ValidationUtil.scanner.nextLine();
        }
    }

    /**
     * Masks a password string for display purposes.
     *
     * @param password The password to mask
     * @return Masked password (e.g., "********")
     */
    public static String maskPassword(String password) {
        if (password == null || password.isEmpty()) {
            return "";
        }
        StringBuilder masked = new StringBuilder();
        for (int i = 0; i < password.length(); i++) {
            masked.append("*");
        }
        return masked.toString();
    }
}


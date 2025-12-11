package assignment.util;

/**
 * Utility class for checking password strength and providing feedback.
 */
public class PasswordStrengthUtil {

    /**
     * Enum representing password strength levels.
     */
    public enum PasswordStrength {
        VERY_WEAK("Very Weak", 1),
        WEAK("Weak", 2),
        FAIR("Fair", 3),
        GOOD("Good", 4),
        STRONG("Strong", 5);

        private final String displayName;
        private final int level;

        PasswordStrength(String displayName, int level) {
            this.displayName = displayName;
            this.level = level;
        }

        public String getDisplayName() {
            return displayName;
        }

        public int getLevel() {
            return level;
        }
    }

    /**
     * Checks the strength of a password and returns a PasswordStrength enum.
     *
     * @param password The password to check
     * @return PasswordStrength enum indicating the strength level
     */
    public static PasswordStrength checkPasswordStrength(String password) {
        if (password == null || password.isEmpty()) {
            return PasswordStrength.VERY_WEAK;
        }

        int score = 0;
        boolean hasLower = false;
        boolean hasUpper = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;

        // Check length
        if (password.length() >= 8) score++;
        if (password.length() >= 12) score++;
        if (password.length() >= 16) score++;

        // Check character types
        for (char c : password.toCharArray()) {
            if (Character.isLowerCase(c)) hasLower = true;
            else if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isDigit(c)) hasDigit = true;
            else if (!Character.isLetterOrDigit(c)) hasSpecial = true;
        }

        if (hasLower) score++;
        if (hasUpper) score++;
        if (hasDigit) score++;
        if (hasSpecial) score++;

        // Check for common patterns (weak passwords)
        if (isCommonPassword(password)) {
            score = Math.max(1, score - 2);
        }

        // Check for repeated characters
        if (hasRepeatedCharacters(password)) {
            score--;
        }

        // Determine strength level
        if (score <= 2) return PasswordStrength.VERY_WEAK;
        if (score <= 4) return PasswordStrength.WEAK;
        if (score <= 6) return PasswordStrength.FAIR;
        if (score <= 8) return PasswordStrength.GOOD;
        return PasswordStrength.STRONG;
    }

    /**
     * Checks if password contains common weak patterns.
     */
    private static boolean isCommonPassword(String password) {
        String lowerPassword = password.toLowerCase();
        String[] commonPasswords = {
                "password", "12345678", "qwerty", "abc123", "password123",
                "admin", "letmein", "welcome", "monkey", "123456789"
        };

        for (String common : commonPasswords) {
            if (lowerPassword.contains(common)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if password has too many repeated characters.
     */
    private static boolean hasRepeatedCharacters(String password) {
        if (password.length() < 3) return false;

        int maxRepeat = 0;
        int currentRepeat = 1;

        for (int i = 1; i < password.length(); i++) {
            if (password.charAt(i) == password.charAt(i - 1)) {
                currentRepeat++;
                maxRepeat = Math.max(maxRepeat, currentRepeat);
            } else {
                currentRepeat = 1;
            }
        }

        return maxRepeat >= 4; // 4 or more consecutive same characters
    }

    /**
     * Gets a visual strength indicator bar.
     *
     * @param strength The password strength level
     * @return A string representing the strength bar
     */
    public static String getStrengthBar(PasswordStrength strength) {
        int level = strength.getLevel();
        StringBuilder bar = new StringBuilder("[");

        for (int i = 1; i <= 5; i++) {
            if (i <= level) {
                bar.append("█");
            } else {
                bar.append("░");
            }
        }
        bar.append("]");

        return bar.toString();
    }

    /**
     * Gets detailed feedback about password requirements.
     *
     * @param password The password to analyze
     * @return A string with feedback about what's missing
     */
    public static String getPasswordFeedback(String password) {
        if (password == null || password.isEmpty()) {
            return "Password cannot be empty";
        }

        StringBuilder feedback = new StringBuilder();
        boolean hasLower = password.matches(".*[a-z].*");
        boolean hasUpper = password.matches(".*[A-Z].*");
        boolean hasDigit = password.matches(".*[0-9].*");
        boolean hasSpecial = password.matches(".*[^a-zA-Z0-9].*");

        if (password.length() < 8) {
            feedback.append("• At least 8 characters required\n");
        }
        if (!hasLower) {
            feedback.append("• Add lowercase letters\n");
        }
        if (!hasUpper) {
            feedback.append("• Add uppercase letters\n");
        }
        if (!hasDigit) {
            feedback.append("• Add numbers\n");
        }
        if (!hasSpecial) {
            feedback.append("• Add special characters (!@#$%^&*)\n");
        }

        return feedback.length() > 0 ? feedback.toString() : "All requirements met!";
    }
}

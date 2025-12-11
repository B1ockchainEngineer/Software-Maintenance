package assignment.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Utility class for password hashing and verification.
 * Uses SHA-256 with salt for secure password storage.
 */
public class PasswordHashUtil {

    private static final String ALGORITHM = "SHA-256";
    private static final int SALT_LENGTH = 16; // 16 bytes = 128 bits

    /**
     * Hashes a password with a randomly generated salt.
     * Returns the salt and hash combined as: salt:hash (both base64 encoded)
     *
     * @param password The plain text password to hash
     * @return A string containing salt:hash (both base64 encoded)
     */
    public static String hashPassword(String password) {
        try {
            // Generate random salt
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[SALT_LENGTH];
            random.nextBytes(salt);

            // Hash password with salt
            MessageDigest md = MessageDigest.getInstance(ALGORITHM);
            md.update(salt);
            byte[] hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));

            // Encode salt and hash to base64
            String saltBase64 = Base64.getEncoder().encodeToString(salt);
            String hashBase64 = Base64.getEncoder().encodeToString(hashedPassword);

            // Return salt:hash format
            return saltBase64 + ":" + hashBase64;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }

    /**
     * Verifies a password against a stored hash.
     *
     * @param password The plain text password to verify
     * @param storedHash The stored hash in format salt:hash
     * @return true if password matches, false otherwise
     */
    public static boolean verifyPassword(String password, String storedHash) {
        try {
            // Handle legacy plain text passwords (for backward compatibility)
            if (storedHash == null || storedHash.isEmpty()) {
                return false;
            }

            // Check if it's a legacy plain text password (no colon separator)
            if (!storedHash.contains(":")) {
                // Legacy plain text - compare directly (for migration)
                return password.equals(storedHash);
            }

            // Split salt and hash
            String[] parts = storedHash.split(":", 2);
            if (parts.length != 2) {
                return false;
            }

            String saltBase64 = parts[0];
            String hashBase64 = parts[1];

            // Decode salt and hash
            byte[] salt = Base64.getDecoder().decode(saltBase64);
            byte[] storedHashBytes = Base64.getDecoder().decode(hashBase64);

            // Hash the input password with the same salt
            MessageDigest md = MessageDigest.getInstance(ALGORITHM);
            md.update(salt);
            byte[] hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));

            // Compare hashes (constant-time comparison to prevent timing attacks)
            return MessageDigest.isEqual(hashedPassword, storedHashBytes);
        } catch (Exception e) {
            // If any error occurs (invalid format, etc.), return false
            return false;
        }
    }

    /**
     * Checks if a stored password is hashed (contains colon separator).
     *
     * @param storedPassword The stored password string
     * @return true if it appears to be a hash, false if plain text
     */
    public static boolean isHashed(String storedPassword) {
        return storedPassword != null && storedPassword.contains(":");
    }
}


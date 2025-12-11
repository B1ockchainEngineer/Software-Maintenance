package assignment.util.config;

/**
 * Shared application-wide constants used across all modules.
 * This config should only contain constants that are used by multiple domains.
 */
public class AppConfig {
    private AppConfig() {
        // prevent instantiation
    }

    // ================== FILE & PATH CONFIG ==================
    public static final String DATA_DIR = "data/";
    public static final String TEMP_DIR = DATA_DIR + "temp/";
    
    // ================== GENERAL UI PROMPTS ==================
    public static final String PROMPT_SELECTION = "ENTER YOUR SELECTION: ";
    public static final String MSG_INVALID_OPTION = "<<<INVALID OPTION!>>>";
    
    // ================== SEPARATORS ==================
    public static final String SEPARATOR_LINE = "-------------------------------------------------------";
    public static final String SEPARATOR_SHORT = "-----------------------";
    public static final String SEPARATOR_LONG = "================================================================================";
}

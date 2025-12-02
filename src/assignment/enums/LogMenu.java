package assignment.enums;

public enum LogMenu {
    SIGN_UP(1, "SIGN UP"),
    LOG_IN(2, "LOG IN"),
    EXIT(3, "EXIT");

    private final int option;
    private final String description;

    LogMenu(int option, String description) {
        this.option = option;
        this.description = description;
    }

    public int getOption() {
        return option;
    }

    public String getDescription() {
        return description;
    }

    public static LogMenu getByOption(int opt) {
        for (LogMenu menu : values()) {
            if (menu.option == opt) {
                return menu;
            }
        }
        return null;
    }
}


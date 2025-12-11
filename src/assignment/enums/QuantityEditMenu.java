package assignment.enums;

/**
 * Enum representing quantity edit operations for stock management.
 */
public enum QuantityEditMenu {
    ADD_STOCK(1, "ADD STOCK"),
    REDUCE_STOCK(2, "REDUCE STOCK");

    private final int option;
    private final String description;

    QuantityEditMenu(int option, String description) {
        this.option = option;
        this.description = description;
    }

    public int getOption() {
        return option;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Gets the QuantityEditMenu enum by option number.
     * @param opt The option number
     * @return The corresponding QuantityEditMenu, or null if not found
     */
    public static QuantityEditMenu getByOption(int opt) {
        for (QuantityEditMenu menu : values()) {
            if (menu.option == opt) {
                return menu;
            }
        }
        return null;
    }
}



package assignment.enums;

public enum StockMenu {
    BACK_TO_MAIN(0, "BACK TO MAIN MENU"),
    ADD_PRODUCT(1, "ADD NEW PRODUCT"),
    DELETE_PRODUCT(2, "DELETE PRODUCT"),
    VIEW_PRODUCT_LIST(3, "VIEW PRODUCT LIST");

    private final int option;
    private final String description;

    // ... Constructors and methods (same pattern as MainMenu) ...

    StockMenu(int option, String description) {
        this.option = option;
        this.description = description;
    }

    public int getOption() {
        return option;
    }

    public String getDescription() {
        return description;
    }

    public static StockMenu getByOption(int opt) {
        for (StockMenu menu : values()) {
            if (menu.option == opt) {
                return menu;
            }
        }
        return null;
    }
}
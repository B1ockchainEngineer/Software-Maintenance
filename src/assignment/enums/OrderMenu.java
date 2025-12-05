package assignment.enums;

public enum OrderMenu {
    BACK_TO_PREVIOUS(0, "BACK TO PREVIOUS PAGE"),
    ADD_ORDER(1, "ADD NEW ORDER"),
    EDIT_ORDER(2, "EDIT AN ORDER"),
    SEARCH_ORDER(3, "SEARCH AN ORDER"),
    REMOVE_ORDER(4, "REMOVE AN ORDER"),
    MAKE_PAYMENT(5, "MAKE PAYMENT");

    private final int option;
    private final String description;

    // ... Constructors and methods (same pattern as MainMenu) ...

    OrderMenu(int option, String description) {
        this.option = option;
        this.description = description;
    }

    public int getOption() {
        return option;
    }

    public String getDescription() {
        return description;
    }

    public static OrderMenu getByOption(int opt) {
        for (OrderMenu menu : values()) {
            if (menu.option == opt) {
                return menu;
            }
        }
        return null;
    }
}
package assignment.enums;

public enum SalesMenu {
    BACK_TO_MAIN(0, "BACK TO MAIN MENU"),
    MAKE_ORDER(1, "MAKE ORDER"),
    TRANSACTION_REPORT(2, "TRANSACTION REPORT");

    private final int option;
    private final String description;

    SalesMenu(int option, String description) {
        this.option = option;
        this.description = description;
    }

    public int getOption() {
        return option;
    }

    public String getDescription() {
        return description;
    }

    public static SalesMenu getByOption(int opt) {
        for (SalesMenu menu : values()) {
            if (menu.option == opt) {
                return menu;
            }
        }
        return null;
    }
}
package assignment.enums;

public enum MainMenu {
    LOGOUT(0, "LOGOUT"),
    MEMBERSHIP_MANAGEMENT(1, "MEMBERSHIP MANAGEMENT"),
    SALES_MANAGEMENT(2, "SALES MANAGEMENT"),
    STAFF_MANAGEMENT(3, "STAFF MANAGEMENT"),
    STOCK_MANAGEMENT(4, "FOOD AND BEVERAGES (Stock)");

    private final int option;
    private final String description;

    MainMenu(int option, String description) {
        this.option = option;
        this.description = description;
    }

    public int getOption() {
        return option;
    }

    public String getDescription() {
        return description;
    }

    public static MainMenu getByOption(int opt) {
        for (MainMenu menu : values()) {
            if (menu.option == opt) {
                return menu;
            }
        }
        return null; // Return null for invalid options
    }
}
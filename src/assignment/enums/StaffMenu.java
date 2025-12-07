package assignment.enums;

public enum StaffMenu {
    BACK_TO_MAIN(0, "BACK TO MAIN MENU"),
    ADD_STAFF(1, "ADD NEW STAFF"),
    DELETE_STAFF(2, "DELETE STAFF"),
    SEARCH_STAFF(3, "SEARCH STAFF"),
    VIEW_STAFF_LIST(4, "VIEW REGISTERED STAFF LIST");

    private final int option;
    private final String description;

    StaffMenu(int option, String description) {
        this.option = option;
        this.description = description;
    }

    public int getOption() {
        return option;
    }

    public String getDescription() {
        return description;
    }

    public static StaffMenu getByOption(int opt) {
        for (StaffMenu menu : values()) {
            if (menu.option == opt) {
                return menu;
            }
        }
        return null;
    }
}



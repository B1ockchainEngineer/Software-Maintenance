package assignment.enums;

public enum MemberMenu {
    BACK_TO_MAIN(0, "BACK TO MAIN MENU"),
    ADD_MEMBER(1, "ADD NEW MEMBER"),
    DELETE_MEMBER(2, "DELETE MEMBER"),
    EDIT_MEMBER(3, "EDIT MEMBER"),
    SEARCH_MEMBER(4, "SEARCH MEMBER"),
    VIEW_MEMBER_LIST(5, "VIEW REGISTERED MEMBER LIST");

    private final int option;
    private final String description;

    MemberMenu(int option, String description) {
        this.option = option;
        this.description = description;
    }

    public int getOption() {
        return option;
    }

    public String getDescription() {
        return description;
    }

    public static MemberMenu getByOption(int opt) {
        for (MemberMenu menu : values()) {
            if (menu.option == opt) {
                return menu;
            }
        }
        return null;
    }
}



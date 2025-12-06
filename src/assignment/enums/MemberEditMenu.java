package assignment.enums;

public enum MemberEditMenu {

    BACK_TO_PREVIOUS_MENU(0, "BACK TO PREVIOUS MENU"),
    MEMBER_NAME(1, "MEMBER NAME"),
    MEMBER_HP(2, "MEMBER HP"),
    MEMBER_IC(3, "MEMBER IC"),
    MEMBER_TYPE(4, "MEMBER TYPE");

    private final int option;
    private final String description;

    MemberEditMenu(int option, String description) {
        this.option = option;
        this.description = description;
    }

    public int getOption() {
        return option;
    }

    public String getDescription() {
        return description;
    }

    public static MemberEditMenu getByOption(int opt) {
        for (MemberEditMenu menu : values()) {
            if (menu.option == opt) {
                return menu;
            }
        }
        return null;
    }
}

package assignment.enums;

public enum TierMenu {
    BACK_TO_MEMBER_MENU(0, "BACK TO MEMBER MENU"),
    NORMAL_MEMBER(1, "NORMAL MEMBER"),
    GOLD_MEMBER(2, "GOLD MEMBER"),
    PREMIUM_MEMBER(3, "PREMIUM MEMBER");

    private final int option;
    private final String description;

    TierMenu(int option, String description) {
        this.option = option;
        this.description = description;
    }

    public int getOption() {
        return option;
    }

    public String getDescription() {
        return description;
    }

    public static TierMenu getByOption(int opt) {
        for (TierMenu menu : values()) {
            if (menu.option == opt) {
                return menu;
            }
        }
        return null;
    }
}

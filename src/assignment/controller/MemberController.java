package assignment.controller;

import assignment.enums.MemberMenu;
import assignment.model.GoldMember;
import assignment.model.Membership;
import assignment.model.NormalMember;
import assignment.model.PremiumMember;
import assignment.util.ConsoleUtil;
import assignment.util.ValidationUtil;

/**
 * Controller for membership-related flows.
 * Handles menu presentation and delegates work to Membership subclasses.
 */
public class MemberController {

    // We use NormalMember as a concrete handler for shared file-based operations
    private final Membership membershipManager = new NormalMember();

    public void manageMembers() {
        while (true) {
            ConsoleUtil.clearScreen();
            ConsoleUtil.logo();
            printMemberMenu();
            System.out.print("ENTER YOUR SELECTION: ");

            int memberOpt = ValidationUtil.intValidation(0, 4);

            if (memberOpt == -9999) {
                ConsoleUtil.systemPause();
                continue;
            }

            MemberMenu selection = MemberMenu.getByOption(memberOpt);
            if (selection == null) {
                System.out.println("<<<INVALID OPTION>>>");
                ConsoleUtil.systemPause();
                continue;
            }

            switch (selection) {
                case ADD_MEMBER -> {
                    ConsoleUtil.clearScreen();
                    addToWhichMember();
                }
                case DELETE_MEMBER -> {
                    ConsoleUtil.clearScreen();
                    membershipManager.delete();
                }
                case SEARCH_MEMBER -> {
                    ConsoleUtil.clearScreen();
                    membershipManager.search();
                }
                case VIEW_MEMBER_LIST -> {
                    ConsoleUtil.clearScreen();
                    membershipManager.view();
                }
                case BACK_TO_MAIN -> {
                    System.out.println("BACK TO MAIN MENU...");
                    ConsoleUtil.systemPause();
                    return;
                }
            }
        }
    }

    private void printMemberMenu() {
        System.out.println("[ MEMBER MANAGEMENT SYSTEM ]");
        System.out.println("-------------------------------------------------------");
        for (MemberMenu menu : MemberMenu.values()) {
            System.out.printf("%d. %s%n", menu.getOption(), menu.getDescription());
        }
        System.out.println("-------------------------------------------------------");
    }

    /**
     * Prompts user for which membership type to create and calls the appropriate
     * Membership subclass add() method.
     */
    private void addToWhichMember() {
        while (true) {
            ConsoleUtil.clearScreen();
            ConsoleUtil.logo();
            System.out.println("[ REGISTER MEMBER ]");
            System.out.println("-------------------------------------------------------");
            System.out.println("CHOOSE MEMBER TYPE:");
            System.out.println("1 = NORMAL");
            System.out.println("2 = GOLD");
            System.out.println("3 = PREMIUM");
            System.out.println("ENTER 'E' TO RETURN MAIN MENU");
            System.out.print("YOUR CHOICE: ");

            char userChoice = ValidationUtil.charValidation();

            if (userChoice == 'O') {
                continue;
            }

            System.out.println("-------------------------------------------------------");

            switch (userChoice) {
                case '1' -> {
                    NormalMember normalMember = new NormalMember();
                    normalMember.setMemberType("Normal");
                    normalMember.add();
                }
                case '2' -> {
                    GoldMember goldMember = new GoldMember();
                    goldMember.setMemberType("Gold");
                    goldMember.add();
                }
                case '3' -> {
                    PremiumMember premiumMember = new PremiumMember();
                    premiumMember.setMemberType("Premium");
                    premiumMember.add();
                }
                case 'E' -> {
                    return;
                }
                default -> System.out.println("<<<INVALID OPTION>>>");
            }

            System.out.print("ADD MORE MEMBER ? (Y = YES , N = NO): ");
            char addAnother = ValidationUtil.charValidation();
            if (addAnother == 'N') {
                return;
            }
        }
    }
}



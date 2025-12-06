package assignment.controller;

import assignment.enums.TierMenu;
import assignment.enums.MemberMenu;
import assignment.model.GoldMember;
import assignment.model.Membership;
import assignment.model.NormalMember;
import assignment.model.PremiumMember;
import assignment.service.MemberService;
import assignment.util.ConsoleUtil;
import assignment.util.ValidationUtil;

import java.util.Random;
import java.util.Scanner;


/**
 * Controller for membership-related flows.
 * Handles menu presentation and delegates work to Membership subclasses.
 */
public class MemberController {

    private final Scanner scanner = new Scanner(System.in);
    private final MemberService memberService;

    // Constructor injection
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

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
                    add();
                }
                case DELETE_MEMBER -> {
                    ConsoleUtil.clearScreen();
                    delete();
                }
                case EDIT_MEMBER -> {
                    ConsoleUtil.clearScreen();
                    edit();
                }
                case SEARCH_MEMBER -> {
                    ConsoleUtil.clearScreen();
                    search();
                }
                case VIEW_MEMBER_LIST -> {
                    ConsoleUtil.clearScreen();
                    view();
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

    private void printTierMenu(){

        System.out.println("[ REGISTER MEMBER ]");
        System.out.println("-------------------------------------------------------");
        for (TierMenu menu : TierMenu.values()){
            System.out.printf("%d. %s%n", menu.getOption(), menu.getDescription());
        }
        System.out.println("-------------------------------------------------------");
    }

    private void add() {
        while (true) {
            ConsoleUtil.clearScreen();
            ConsoleUtil.logo();
            printTierMenu();
            System.out.print("YOUR CHOICE: ");

            int option = ValidationUtil.intValidation(0, 4);
            TierMenu userChoice = TierMenu.getByOption(option);

            if (userChoice == null) {
                System.out.println("<<<INVALID OPTION>>>");
                ConsoleUtil.systemPause();
                continue;
            }

            switch (userChoice) {
                case NORMAL_MEMBER -> handleAddMember(new NormalMember());
                case GOLD_MEMBER   -> handleAddMember(new GoldMember());
                case PREMIUM_MEMBER -> handleAddMember(new PremiumMember());
                case BACK_TO_MEMBER_MENU -> { return; }
                default -> System.out.println("<<<INVALID OPTION>>>");
            }

            System.out.print("ADD MORE MEMBER? (Y = YES , N = NO): ");
            char addAnother = ValidationUtil.charValidation();
            if (addAnother == 'N') {
                return;
            }
        }
    }


    private void handleAddMember(Membership member) {
        String memberName, memberHP, memberIC;
        boolean validName;

        Random random = new Random();
        member.setId(random.nextInt(898) + 100);

        System.out.println("MEMBER ID >> M-" + member.getId());
        System.out.println("[THIS IS YOUR MEMBER ID]");

        do {
            System.out.println("Press enter key to continue...");
            scanner.nextLine(); // consume newline

            // IC
            do {
                do {
                    System.out.print("ENTER MEMBER IC: ");
                    memberIC = ValidationUtil.icValidation();
                } while (memberIC == null);

                if (memberService.icExists("members.txt", memberIC)) {
                    System.out.println("<<<IC already exists in the file. Please reenter!>>>");
                } else {
                    break;
                }
            } while (true);

            // Name
            do {
                System.out.print("ENTER MEMBER NAME: ");
                memberName = scanner.nextLine();
                validName = ValidationUtil.nameValidation(memberName);
            } while (!validName);

            // HP
            do {
                System.out.print("ENTER MEMBER HP: ");
                memberHP = scanner.nextLine();

                if (memberHP.matches("\\d{10,11}")) {
                    break;
                } else {
                    System.out.println("<<<Invalid input. Please enter a 10 or 11-digit number!>>>");
                }
            } while (true);

            // Set values on the object
            member.setIc(memberIC);
            member.setName(memberName);
            member.setMemberHp(memberHP);

            System.out.println("---------------------------------------------------");
            System.out.println("ARE YOU CONFIRM THE MEMBER DETAILS ABOVE ARE CORRECT ?");
            char yesNo = ValidationUtil.confirmValidation("ENTER YOUR OPTION (Y = YES, N = No): ");

            if (yesNo == 'Y') {
                memberService.addMember(member);

                System.out.println("NEW MEMBER ADDED TO THE SYSTEM...");
                System.out.println("---------------------------------------------------");
                System.out.println("MEMBER ID >> " + member.getId());
                System.out.println("MEMBER IC >> " + member.getIc());
                System.out.println("MEMBER NAME >> " + member.getName());
                System.out.println("MEMBER HP >> " + member.getMemberHp());
                System.out.println("MEMBER TYPE >> " + member.getMemberType());
                System.out.println("---------------------------------------------------");
                System.out.println();

                break;
            }
        } while (true);
    }

    public void delete() {
        Scanner scanner = new Scanner(System.in);

        ConsoleUtil.logo();
        System.out.println("[ DELETE MEMBER ]");
        System.out.println("-------------------------------------------------------");

        System.out.print("ENTER MEMBER ID TO DELETE (ENTER 'E' TO CANCEL): M-");
        String input = scanner.nextLine().trim();

        if (input.equalsIgnoreCase("E")) {
            ConsoleUtil.clearScreen();
            return;
        }

        try {
            int memberIdToDelete = Integer.parseInt(input);

            Membership target = null;
            for (Membership member : memberService.getAllMembers()) {
                if (member.getId() == memberIdToDelete) {
                    target = member;
                    break;
                }
            }

            if (target == null) {
                System.out.println("MEMBER WITH ID M-" + memberIdToDelete + " NOT FOUND OR DELETION CANCELLED.");
            } else {
                System.out.println("Member Details to Delete:");
                System.out.println("MEMBER ID >> " + target.getId());
                System.out.println("MEMBER IC >> " + target.getIc());
                System.out.println("MEMBER NAME >> " + target.getName());
                System.out.println("MEMBER HP >> " + target.getMemberHp());
                System.out.println("MEMBER TYPE >> " + target.getMemberType());
                System.out.println("-------------------------------------------------------");
                // Use confirm validation to eliminate redundancy
                char confirm = ValidationUtil.confirmValidation("CONFIRM DELETION? (Y = YES, N = No): ");

                if (confirm == 'Y') {
                    boolean deleted = memberService.deleteMemberById(memberIdToDelete);
                    if (deleted) {
                        System.out.println("MEMBER WITH ID M-" + memberIdToDelete + " HAS BEEN DELETED.");
                    } else {
                        System.out.println("Failed to delete member from file.");
                    }
                }
            }

        } catch (NumberFormatException e) {
            System.out.println("<<<Invalid input. Please enter a valid member ID or 'E' to cancel.>>>");
        }
        ConsoleUtil.systemPause();
        ConsoleUtil.clearScreen();
    }

    public void view() {
        ConsoleUtil.logo();
        System.out.println("[ VIEW ALL MEMBERS ]");
        System.out.println("-------------------------------------------------------");

        if (memberService.getAllMembers().isEmpty()) {
            System.out.println("THERE IS NO MEMBER TO DISPLAY...");
        } else {
            while (true){
                System.out.println("FILTER MEMBER BY MEMBERSHIP TYPE:");
                for (TierMenu menu : TierMenu.values()){
                    System.out.printf("%d. %s%n", menu.getOption(), menu.getDescription());
                }
                System.out.print("ENTER YOUR CHOICE > ");
                int option = ValidationUtil.intValidation(0, 3);
                TierMenu choice = TierMenu.getByOption(option);

                if (choice == null) {
                    System.out.println("<<<INVALID OPTION>>>");
                    ConsoleUtil.systemPause();
                    continue;
                }

                switch (choice) {
                    case NORMAL_MEMBER -> displayMembersByType("Normal");
                    case GOLD_MEMBER   -> displayMembersByType("Gold");
                    case PREMIUM_MEMBER -> displayMembersByType("Premium");
                    case BACK_TO_MEMBER_MENU -> { return; }
                    default -> System.out.println("<<<INVALID OPTION>>>");
                }
            }
        }

        ConsoleUtil.systemPause();
        ConsoleUtil.clearScreen();
    }

    public void search() {
        Scanner scanner = new Scanner(System.in);

        ConsoleUtil.logo();
        System.out.println("[ SEARCH MEMBER BY ID ]");
        System.out.println("-------------------------------------------------------");

        System.out.print("ENTER MEMBER ID TO SEARCH (3 DIGIT ONLY) OR 'E' TO CANCEL: M-");
        String input = scanner.nextLine().trim();

        if (input.equalsIgnoreCase("E")) {
            ConsoleUtil.clearScreen();
            return;
        }

        try {
            int memberIdToSearch = Integer.parseInt(input);

            boolean found = false;

            for (Membership member : memberService.getAllMembers()) {
                if (member.getId() == memberIdToSearch) {
                    found = true;
                    System.out.println("\tMEMBER FOUND !");
                    System.out.println(member);
                    break;
                }
            }

            if (!found) {
                System.out.println("\tMEMBER NOT FOUND...");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid member ID or 'E' to cancel.");
        }

        ConsoleUtil.systemPause();
        System.out.println();
        ConsoleUtil.clearScreen();
    }

    public void edit() {
        ConsoleUtil.clearScreen();
        ConsoleUtil.logo();
        System.out.println("[ EDIT MEMBER ]");
        System.out.println("-------------------------------------------------------");

        System.out.print("ENTER MEMBER ID (e.g. 741 or M-741): ");
        String rawId = scanner.nextLine().trim();

        int memberId;
        try {
            if (rawId.toUpperCase().startsWith("M-")) {
                memberId = Integer.parseInt(rawId.substring(2));
            } else {
                memberId = Integer.parseInt(rawId);
            }
        } catch (NumberFormatException e) {
            System.out.println("<<< INVALID MEMBER ID FORMAT >>>");
            ConsoleUtil.systemPause();
            return;
        }

        // Ask service to find the member
        Membership member = memberService.findMemberById(memberId);

        if (member == null) {
            System.out.println("<<< MEMBER NOT FOUND >>>");
            ConsoleUtil.systemPause();
            return;
        }

        // Show current details
        System.out.println("CURRENT MEMBER DETAILS:");
        System.out.println("-------------------------------------------------------");
        printMemberDetails(member);
        System.out.println("-------------------------------------------------------");

        boolean done = false;
        while (!done) {
            System.out.println("WHAT DO YOU WANT TO EDIT?");
            System.out.println("1. MEMBER NAME");
            System.out.println("2. MEMBER HP");
            System.out.println("3. MEMBER IC");
            System.out.println("4. MEMBER TYPE");
            System.out.println("0. BACK");
            System.out.print("YOUR CHOICE: ");

            int choice = ValidationUtil.intValidation(0, 4);

            switch (choice) {
                case 1 -> { // Edit name
                    String newName;
                    boolean validName;
                    do {
                        System.out.print("ENTER NEW MEMBER NAME: ");
                        newName = scanner.nextLine();
                        validName = ValidationUtil.nameValidation(newName);
                    } while (!validName);

                    member.setName(newName);
                    System.out.println("<<< MEMBER NAME UPDATED >>>");
                }
                case 2 -> { // Edit HP
                    String newHp;
                    do {
                        System.out.print("ENTER NEW MEMBER HP (10–11 digits): ");
                        newHp = scanner.nextLine();
                        if (newHp.matches("\\d{10,11}")) {
                            break;
                        } else {
                            System.out.println("<<<Invalid input. Please enter a 10 or 11-digit number!>>>");
                        }
                    } while (true);

                    member.setMemberHp(newHp);
                    System.out.println("<<< MEMBER HP UPDATED >>>");
                }
                case 3 -> { // Edit IC
                    String newIc;
                    do {
                        System.out.print("ENTER NEW MEMBER IC: ");
                        newIc = ValidationUtil.icValidation();
                        if (newIc == null) continue;

                        // check IC used by another member
                        if (memberService.icExists(newIc, member.getIc())) {
                            System.out.println("<<<IC already exists for another member. Please reenter!>>>");
                        } else {
                            break;
                        }
                    } while (true);

                    member.setIc(newIc);
                    System.out.println("<<< MEMBER IC UPDATED >>>");
                }
                case 4 -> { // Edit member type
                    System.out.println("SELECT NEW MEMBER TYPE:");
                    System.out.println("1. NORMAL");
                    System.out.println("2. GOLD");
                    System.out.println("3. PREMIUM");
                    System.out.print("YOUR CHOICE: ");

                    int typeOpt = ValidationUtil.intValidation(1, 3);
                    switch (typeOpt) {
                        case 1 -> member.setMemberType("Normal");
                        case 2 -> member.setMemberType("Gold");
                        case 3 -> member.setMemberType("Premium");
                    }
                    System.out.println("<<< MEMBER TYPE UPDATED >>>");
                }
                case 0 -> done = true;
            }

            if (!done) {
                System.out.println("-------------------------------------------------------");
                System.out.println("UPDATED MEMBER DETAILS:");
                printMemberDetails(member);
                System.out.println("-------------------------------------------------------");

                System.out.print("EDIT MORE FIELDS FOR THIS MEMBER? (Y = YES, N = NO): ");
                char more = Character.toUpperCase(ValidationUtil.charValidation());
                if (more == 'N') {
                    done = true;
                }
            }
        }

        // Persist all changes via service
        memberService.saveAllMembers();
        System.out.println("<<< MEMBER DETAILS SAVED >>>");
        ConsoleUtil.systemPause();
        ConsoleUtil.clearScreen();
    }


    private void displayMembersByType(String membershipType) {
        System.out.println("---------------------------------------------------------------------------------------");
        System.out.printf("%-9s | %-22s | %-11s | %-11s | %-12s%n",
                "MEMBER ID", "MEMBER NAME", "MEMBER HP", "MEMBER TYPE", "MEMBER IC");
        System.out.println("---------------------------------------------------------------------------------------");

        boolean foundMembers = false;

        for (Membership member : memberService.getAllMembers()) {
            if (membershipType.equals("Normal") && member instanceof NormalMember) {
                printMemberDetails(member);
                foundMembers = true;
            } else if (membershipType.equals("Gold") && member instanceof GoldMember) {
                printMemberDetails(member);
                foundMembers = true;
            } else if (membershipType.equals("Premium") && member instanceof PremiumMember) {
                printMemberDetails(member);
                foundMembers = true;
            }
        }

        if (!foundMembers) {
            System.out.println("NO " + membershipType + " MEMBERS FOUND.");
        }
        System.out.println("---------------------------------------------------------------------------------------");
    }

    private void printMemberDetails(Membership member) {
        String memberId = "M-" + member.getId();

        System.out.printf("%-9s | %-22s | %-11s | %-11s | %-12s%n",
                memberId,
                member.getName(),
                member.getMemberHp(),
                member.getMemberType(),
                member.getIc());
    }


}



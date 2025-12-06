package assignment.controller;

import assignment.enums.MemberEditMenu;
import assignment.enums.MemberMenu;
import assignment.enums.TierMenu;
import assignment.model.GoldMember;
import assignment.model.Membership;
import assignment.model.NormalMember;
import assignment.model.PremiumMember;
import assignment.service.MemberService;
import assignment.util.ConsoleUtil;
import assignment.util.MemberConfig;
import assignment.util.MemberUtil;
import assignment.util.ValidationUtil;
import assignment.view.MemberView;

import java.util.List;
import java.util.Random;


/**
 * Controller for membership-related flows.
 * Handles menu presentation and delegates work to Membership subclasses.
 */
public class MemberController {

    //Scanner removed to prevent conflicts

    private final MemberService memberService;
    private final MemberView memberView;

    // Constructor injection
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
        this.memberView = new MemberView();
    }

    /**
     * Main loop for the member management system.
     * Shows the menu and handles user selection.
     */
    public void manageMembers() {
        while (true) {
            ConsoleUtil.clearScreen();
            ConsoleUtil.logo();
            memberView.printMemberMenu();
            System.out.print("ENTER YOUR SELECTION: ");

            int memberOpt = ValidationUtil.intValidation(0, 5);

            if (memberOpt == -9999) {
                ConsoleUtil.systemPause();
                continue;
            }

            MemberMenu selection = MemberMenu.getByOption(memberOpt);
            if (selection == null) {
                System.out.println(MemberConfig.ErrorMessage.INVALID_OPTION);
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

    /**
     * Shows a menu to choose member type and registers a new member.
     */
    private void add() {
        while (true) {
            ConsoleUtil.clearScreen();
            ConsoleUtil.logo();
            System.out.println(MemberConfig.TITLE_REGISTER_MEMBER);
            memberView.printTierMenu();
            System.out.print("YOUR CHOICE: ");

            int option = ValidationUtil.intValidation(0, 4);
            TierMenu userChoice = TierMenu.getByOption(option);

            if (userChoice == null) {
                System.out.println(MemberConfig.ErrorMessage.INVALID_OPTION);
                ConsoleUtil.systemPause();
                continue;
            }

            switch (userChoice) {
                case NORMAL_MEMBER -> {
                    NormalMember normalMember = new NormalMember();
                    normalMember.setMemberType(MemberConfig.MEMBER_TYPE_NORMAL);
                    handleAddMember(normalMember);
                }
                case GOLD_MEMBER   -> {
                    GoldMember goldMember = new GoldMember();
                    goldMember.setMemberType(MemberConfig.MEMBER_TYPE_GOLD);
                    handleAddMember(goldMember);
                }
                case PREMIUM_MEMBER -> {
                    PremiumMember premiumMember = new PremiumMember();
                    premiumMember.setMemberType(MemberConfig.MEMBER_TYPE_PREMIUM);
                    handleAddMember(premiumMember);
                }
                case BACK_TO_MEMBER_MENU -> { return; }
            }

            System.out.print("ADD MORE MEMBER? (Y = YES , N = NO): ");
            char addAnother = ValidationUtil.charValidation();
            if (addAnother == 'N') {
                return;
            }
        }
    }


    /**
     * Collects details (IC, Name, HP) and saves the member.
     */
    private void handleAddMember(Membership member) {
        String memberName, memberHP, memberIC;
        boolean validName;
        int randomNumber;

        Random random = new Random();
        do {
            randomNumber = random.nextInt(898) + 100;
        } while (memberService.checkIdExists(randomNumber));

        member.setId(randomNumber);

        System.out.println("MEMBER ID >> M-" + member.getId());
        System.out.println("[THIS IS YOUR MEMBER ID]");

        do {
            System.out.println("Press enter key to continue...");
            ValidationUtil.scanner.nextLine(); // consume newline

            // IC
            do {
                do {
                    System.out.print("ENTER MEMBER IC: ");
                    memberIC = MemberUtil.icValidation();
                } while (memberIC == null);
                if (memberService.icExists(memberIC)) {
                    System.out.println(MemberConfig.ErrorMessage.IC_ALREADY_EXISTS);
                } else {
                    break;
                }
            } while (true);

            // Name
            do {
                System.out.print("ENTER MEMBER NAME: ");
                memberName = ValidationUtil.scanner.nextLine();
                validName = MemberUtil.nameValidation(memberName);
            } while (!validName);

            // HP
            do {
                System.out.print("ENTER MEMBER HP: ");
                memberHP = MemberUtil.hpValidation();
            } while (memberHP == null);
            
            // Set values on the object

            member.setIc(memberIC);
            member.setName(memberName);
            member.setMemberHp(memberHP);

            System.out.println("---------------------------------------------------");
            System.out.println("ARE YOU CONFIRM THE MEMBER DETAILS ABOVE ARE CORRECT ?");
            char yesNo = MemberUtil.confirmValidation("ENTER YOUR OPTION (Y = YES, N = No): ");

            if (yesNo == 'Y') {
                memberService.addMember(member);

                System.out.println(MemberConfig.SuccessfulMessage.MEMBER_ADDED);
                System.out.println("---------------------------------------------------");
                memberView.displayMemberDetails(member);
                System.out.println("---------------------------------------------------");
                System.out.println();

                break;
            }
        } while (true);
    }

    /**
     * Handles the logic to delete a member.
     * Asks for ID and confirms deletion.
     */
    public void delete() {

        ConsoleUtil.logo();
        System.out.println(MemberConfig.TITLE_DELETE_MEMBER);
        System.out.println("-------------------------------------------------------");

        System.out.print("ENTER MEMBER ID TO DELETE (ENTER 'E' TO CANCEL): M-");
        String input = ValidationUtil.scanner.nextLine().trim();

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
                System.out.println(String.format(MemberConfig.ErrorMessage.DELETE_CANCELLED_OR_NOT_FOUND, memberIdToDelete));
            } else {
                System.out.println("Member Details to Delete:");
                memberView.displayMemberDetails(target);
                System.out.println("-------------------------------------------------------");
                // Use confirm validation to eliminate redundancy
                char confirm = MemberUtil.confirmValidation("CONFIRM DELETION? (Y = YES, N = No): ");

                if (confirm == 'Y') {
                    boolean deleted = memberService.deleteMemberById(memberIdToDelete);
                    if (deleted) {
                        System.out.println(String.format(MemberConfig.SuccessfulMessage.MEMBER_DELETED, memberIdToDelete));
                    } else {
                        System.out.println(MemberConfig.ErrorMessage.DELETE_FAILED);
                    }
                }
            }

        } catch (NumberFormatException e) {
            System.out.println(MemberConfig.ErrorMessage.INVALID_MEMBER_ID_FORMAT);
        }
        ConsoleUtil.systemPause();
        ConsoleUtil.clearScreen();
    }

    /**
     * Displays all members or filters them by type.
     */
    public void view() {
        int option;
        ConsoleUtil.logo();
        System.out.println(MemberConfig.TITLE_VIEW_MEMBERS);
        System.out.println("-------------------------------------------------------");

        if (memberService.getAllMembers().isEmpty()) {
            System.out.println(MemberConfig.ErrorMessage.NO_MEMBER_TO_DISPLAY);
        } else {
            while (true){
                System.out.println("FILTER MEMBER BY MEMBERSHIP TYPE:");
                memberView.printTierMenu();
                System.out.print("ENTER YOUR CHOICE > ");

                option = ValidationUtil.intValidation(0, 3);

                if (option == -9999) {
                    ConsoleUtil.systemPause();
                    continue;
                }

                TierMenu choice = TierMenu.getByOption(option);

                if (choice == null) {
                    System.out.println(MemberConfig.ErrorMessage.INVALID_OPTION);
                    ConsoleUtil.systemPause();
                    continue;
                }

                switch (choice) {
                    case BACK_TO_MEMBER_MENU -> { return; }
                    case NORMAL_MEMBER -> memberView.displayMembersByType(memberService.getAllMembers(), MemberConfig.MEMBER_TYPE_NORMAL);
                    case GOLD_MEMBER   -> memberView.displayMembersByType(memberService.getAllMembers(), MemberConfig.MEMBER_TYPE_GOLD);
                    case PREMIUM_MEMBER -> memberView.displayMembersByType(memberService.getAllMembers(), MemberConfig.MEMBER_TYPE_PREMIUM);
                    default -> System.out.println(MemberConfig.ErrorMessage.INVALID_OPTION);
                }
            }
        }

        ConsoleUtil.systemPause();
        ConsoleUtil.clearScreen();
    }

    /**
     * Searches for a member by their 3-digit ID.
     */
    public void search() {
        ConsoleUtil.logo();
        System.out.println(MemberConfig.TITLE_SEARCH_MEMBER);
        System.out.println("-------------------------------------------------------");

        System.out.print("ENTER MEMBER ID TO SEARCH (3 DIGIT ONLY) OR 'E' TO CANCEL: M-");
        String input = ValidationUtil.scanner.nextLine().trim();

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
                    System.out.println(MemberConfig.SuccessfulMessage.MEMBER_FOUND);
                    System.out.println(member);
                    break;
                }
            }

            if (!found) {
                System.out.println(MemberConfig.ErrorMessage.MEMBER_NOT_FOUND);
            }
        } catch (NumberFormatException e) {
            System.out.println(MemberConfig.ErrorMessage.INVALID_MEMBER_ID_FORMAT);
        }

        ConsoleUtil.systemPause();
        System.out.println();
        ConsoleUtil.clearScreen();
    }

    /**
     * Allows editing of member details (Name, HP, IC, Type).
     */
    public void edit() {
        String rawId;
        List<Membership> memberList = memberService.getAllMembers();
        int typeOption;

        ConsoleUtil.clearScreen();
        ConsoleUtil.logo();
        System.out.println(MemberConfig.TITLE_EDIT_MEMBER);
        System.out.println("-------------------------------------------------------");
        do {
            System.out.print("ENTER MEMBER ID (e.g. 741 ): M-");
            rawId = ValidationUtil.digitOnlyValidation(3);
        } while (rawId == null);

        int memberId = Integer.parseInt(rawId);

        // Ask service to find the member
        Membership memberFound = memberService.findMemberById(memberId);
        int memberIndexNo = memberService.findMemberIndexById(memberList, memberId);

        if (memberFound == null) {
            System.out.println(MemberConfig.ErrorMessage.MEMBER_NOT_FOUND);
            ConsoleUtil.systemPause();
            return;
        }

        // Show current details
        System.out.println();
        System.out.println("CURRENT MEMBER DETAILS:");
        System.out.println("-------------------------------------------------------");
        memberView.displayMemberDetails(memberFound);
        System.out.println("-------------------------------------------------------");
        System.out.println();

        boolean done = false;
        while (!done) {
            System.out.println("WHAT DO YOU WANT TO EDIT?");
            memberView.printEditMenu();
            System.out.print("YOUR CHOICE: ");

            int option = ValidationUtil.intValidation(0, 4);

            if (option == -9999) {
                ConsoleUtil.systemPause();
                continue;
            }

            MemberEditMenu userEditChoice = MemberEditMenu.getByOption(option);

            if (userEditChoice == null) {
                System.out.println(MemberConfig.ErrorMessage.INVALID_OPTION);
                ConsoleUtil.systemPause();
                continue;
            }

            switch (userEditChoice) {
                case MEMBER_NAME -> { // Edit name
                    String newName;
                    boolean validName;
                    do {
                        System.out.print("ENTER NEW MEMBER NAME: ");
                        newName = ValidationUtil.scanner.nextLine();
                        validName = MemberUtil.nameValidation(newName);
                    } while (!validName);

                    memberFound.setName(newName);
                    System.out.println(MemberConfig.SuccessfulMessage.MEMBER_NAME_UPDATED);
                }
                case MEMBER_HP -> { // Edit HP
                    String newHp;
                    do {
                        System.out.print("ENTER NEW MEMBER HP (10–11 digits): ");
                        newHp = MemberUtil.hpValidation();
                    } while (newHp == null);

                    memberFound.setMemberHp(newHp);
                    System.out.println(MemberConfig.SuccessfulMessage.MEMBER_HP_UPDATED);
                }
                case MEMBER_IC -> { // Edit IC
                    String newIc;
                    do {
                        System.out.print("ENTER NEW MEMBER IC: ");
                        newIc = MemberUtil.icValidation();
                        if (newIc == null) continue;

                        // check IC used by another member
                        if (memberService.icExists(newIc)){
                            System.out.println(MemberConfig.ErrorMessage.IC_ALREADY_EXISTS_OTHER);
                        } else {
                            break;
                        }
                    } while (true);

                    memberFound.setIc(newIc);
                    System.out.println(MemberConfig.SuccessfulMessage.MEMBER_IC_UPDATED);
                }
                case MEMBER_TYPE -> { // Edit member type
                    System.out.println("SELECT NEW MEMBER TYPE:");
                    memberView.printTierMenu();
                    System.out.print("YOUR CHOICE: ");

                    do {
                        typeOption = ValidationUtil.intValidation(1, 3);
                    } while (typeOption == -9999);

                    TierMenu userChoice = TierMenu.getByOption(typeOption);

                    if (userChoice == null) {
                        System.out.println(MemberConfig.ErrorMessage.INVALID_OPTION);
                        ConsoleUtil.systemPause();
                        continue;
                    }

                    switch (userChoice) {
                        case NORMAL_MEMBER -> {
                            NormalMember normalMember = new NormalMember();
                            normalMember.setMemberType(MemberConfig.MEMBER_TYPE_NORMAL);
                        }
                        case GOLD_MEMBER   -> {
                            GoldMember goldMember = new GoldMember();
                            goldMember.setMemberType(MemberConfig.MEMBER_TYPE_GOLD);
                        }
                        case PREMIUM_MEMBER -> {
                            PremiumMember premiumMember = new PremiumMember();
                            premiumMember.setMemberType(MemberConfig.MEMBER_TYPE_PREMIUM);
                        }
                    }
                }
                case BACK_TO_PREVIOUS_MENU -> done = true;  // Back
            }

            if (!done) {
                System.out.println("-------------------------------------------------------");
                System.out.println("UPDATED MEMBER DETAILS:");
                memberView.displayMemberDetails(memberFound);
                System.out.println("-------------------------------------------------------");

                char more = MemberUtil.confirmValidation("EDIT MORE FIELDS FOR THIS MEMBER? (Y = YES, N = NO):");
                if (more == 'N') {
                    done = true;
                }
            }
        }

        memberList.get(memberIndexNo).setName(memberFound.getName());
        memberList.get(memberIndexNo).setIc(memberFound.getIc());
        memberList.get(memberIndexNo).setMemberHp(memberFound.getMemberHp());
        memberList.get(memberIndexNo).setMemberType(memberFound.getMemberType());

        // Persist all changes via service
        memberService.saveMemberInfo(memberList);
        System.out.println(MemberConfig.SuccessfulMessage.MEMBER_SAVED);
        ConsoleUtil.systemPause();
        ConsoleUtil.clearScreen();
    }

}



package assignment.model;

import assignment.repo.MemberRepository;
import assignment.util.ConsoleUtil;
import assignment.util.ValidationUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * Membership management base class using ConsoleUtil for output.
 */
public abstract class Membership extends Person {

    protected final List<Membership> memberList = new ArrayList<>();
    private static final MemberRepository memberRepo = new MemberRepository();

    private String memberHp;
    private String memberType;
    private double discountRate = 0;
    private static int noOfMember = 0;

    public Membership() {
    }

    public Membership(String name, String ic, int id, String memberHp, String memberType, double discountRate) {
        super(name, ic, id);
        this.memberHp = memberHp;
        this.memberType = memberType;
        this.discountRate = discountRate;
        noOfMember++;
    }

    public String getMemberHp() {
        return memberHp;
    }

    public String getMemberType() {
        return memberType;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public static int getNoOfMember() {
        return noOfMember;
    }

    public void setMemberHp(String memberHp) {
        this.memberHp = memberHp;
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }

    public void setDiscountRate(double discountRate) {
        this.discountRate = discountRate;
    }

    public double calculateDiscountRate() {
        return discountRate;
    }

    public abstract double calDiscount();

    @Override
    public void add() {
        Scanner scanner = new Scanner(System.in);
        String memberName, memberHP, memberIC;

        Random random = new Random();
        setId((random.nextInt(898) + 100));

        System.out.println("MEMBER ID >> " + "M-" + getId());
        System.out.println("[THIS IS YOUR MEMBER ID]");

        do {
            System.out.println("Press enter key to continue...");
            scanner.nextLine(); // Consume the newline character

            // IC
            do {
                do {
                    System.out.print("ENTER MEMBER IC: ");
                    memberIC = ValidationUtil.icValidation();
                }while(memberIC == null);

                if (icExists("members.txt", memberIC)) {
                    System.out.println("<<<IC already exists in the file. Please reenter!>>>");
                } else {
                    break;
                }

            } while (true);

            // Name
            do {
                System.out.print("ENTER MEMBER NAME: ");
                memberName = scanner.nextLine();
                if (memberName.matches("[a-zA-Z ]+")) {
                    break;
                } else {
                    System.out.println("<<<Invalid. Please enter a valid name!>>>");
                }
            } while (true);

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

            setIc(memberIC);
            setName(memberName);
            setMemberHp(memberHP);

            System.out.println("---------------------------------------------------");
            System.out.println("ARE YOU CONFIRM THE MEMBER DETAILS ABOVE ARE CORRECT ?");
            System.out.print("ENTER YOUR OPTION (Y = YES, ANY KEY TO REPEAT): ");
            char yesNo = scanner.next().charAt(0);

            if (yesNo == 'Y' || yesNo == 'y') {
                // Persist via repository
                memberRepo.appendMember(this);

                System.out.println("NEW MEMBER ADDED TO THE SYSTEM...");
                System.out.println("---------------------------------------------------");
                System.out.println("MEMBER ID >> " + getId());
                System.out.println("MEMBER IC >> " + getIc());
                System.out.println("MEMBER NAME >> " + getName());
                System.out.println("MEMBER HP >> " + getMemberHp());
                System.out.println("MEMBER TYPE >> " + getMemberType());
                System.out.println("---------------------------------------------------");
                System.out.println();

                break;
            }
        } while (true);
    }

    @Override
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

            // Load members from repository
            loadMembersFromRepo();
            Membership target = null;
            for (Membership member : memberList) {
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
                System.out.print("CONFIRM DELETION? (Y = YES, ANY KEY TO CANCEL): ");
                char confirm = scanner.next().trim().toUpperCase().charAt(0);

                if (confirm == 'Y') {
                    boolean deleted = memberRepo.deleteById(memberIdToDelete);
                    if (deleted) {
                        System.out.println("MEMBER WITH ID M-" + memberIdToDelete + " HAS BEEN DELETED.");
                        loadMembersFromRepo();
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

    @Override
    public void view() {
        loadMembersFromRepo();
        ConsoleUtil.logo();
        System.out.println("[ VIEW ALL MEMBERS ]");
        System.out.println("-------------------------------------------------------");

        if (memberList.isEmpty()) {
            System.out.println("THERE IS NO MEMBER TO DISPLAY...");
        } else {
            boolean validChoice = false;
            while (!validChoice) {
                System.out.println("FILTER MEMBER BY MEMBERSHIP TYPE:");
                System.out.println("1. NORMAL");
                System.out.println("2. GOLD");
                System.out.println("3. PREMIUM");
                System.out.println("E. RETURN TO MENU");
                System.out.print("ENTER YOUR CHOICE (1, 2, 3, E) > ");
                Scanner scanner = new Scanner(System.in);
                String input = scanner.next().trim();

                switch (input.toUpperCase()) {
                    case "1" -> {
                        displayMembersByType("Normal");
                        validChoice = true;
                    }
                    case "2" -> {
                        displayMembersByType("Gold");
                        validChoice = true;
                    }
                    case "3" -> {
                        displayMembersByType("Premium");
                        validChoice = true;
                    }
                    case "E" -> {
                        ConsoleUtil.clearScreen();
                        return;
                    }
                    default -> System.out.println("<<<Invalid choice. Please try again.>>>");
                }
            }
        }

        ConsoleUtil.systemPause();
        ConsoleUtil.clearScreen();
    }

    @Override
    public void search() {
        loadMembersFromRepo();
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

            for (Membership member : memberList) {
                if (member.getId() == memberIdToSearch) {
                    found = true;
                    System.out.println("\tMEMBER FOUND !");
                    System.out.println(member.toString());
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

    private void displayMembersByType(String membershipType) {
        System.out.println("---------------------------------------------------------------------------------------");
        System.out.println("MEMBER ID |MEMBER NAME  |MEMBER HP   |MEMBER TYPE |MEMBER IC");
        System.out.println("---------------------------------------------------------------------------------------");

        boolean foundMembers = false;

        for (Membership member : memberList) {
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
        System.out.println("M-" + member.getId()
                + "     |" + member.getName()
                + "  |" + member.getMemberHp()
                + "  |" + member.getMemberType()
                + "      |" + member.getIc());
    }

    @Override
    public String toString() {
        return "\nMEMBER INFO\n-------------\nMEMBER ID >> M-" + getId()
                + "\nMEMBER NAME: " + getName()
                + "\nMEMBER IC: " + getIc()
                + "\nMEMBER HP: " + getMemberHp()
                + "\nTYPE OF MEMBERSHIP: " + getMemberType();
    }

    public void loadMembersFromRepo() {
        memberList.clear();
        memberList.addAll(memberRepo.loadAllMembers());
    }

    private boolean icExists(String filePath, String targetIC) {
        // filePath ignored; repository is the single source of truth
        return memberRepo.existsByIc(targetIC);
    }
}



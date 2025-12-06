package assignment.view;

import assignment.enums.MemberEditMenu;
import assignment.enums.MemberMenu;
import assignment.enums.TierMenu;
import assignment.model.GoldMember;
import assignment.model.Membership;
import assignment.model.NormalMember;
import assignment.model.PremiumMember;
import assignment.util.MemberConfig;

import java.util.List;

/**
 * View class for Member management.
 * Handles all print outputs and menu displays.
 */
public class MemberView {

    public void printMemberMenu() {
        System.out.println(MemberConfig.TITLE_MEMBER_SYSTEM);
        System.out.println("-------------------------------------------------------");
        for (MemberMenu menu : MemberMenu.values()) {
            System.out.printf("%d. %s%n", menu.getOption(), menu.getDescription());
        }
        System.out.println("-------------------------------------------------------");
    }

    public void printTierMenu() {
        System.out.println("-------------------------------------------------------");
        for (TierMenu menu : TierMenu.values()){
            System.out.printf("%d. %s%n", menu.getOption(), menu.getDescription());
        }
        System.out.println("-------------------------------------------------------");
    }

    public void printEditMenu() {
        System.out.println("-------------------------------------------------------");
        for (MemberEditMenu menu : MemberEditMenu.values()){
            System.out.printf("%d. %s%n", menu.getOption(), menu.getDescription());
        }
        System.out.println("-------------------------------------------------------");
    }

    public void displayMemberDetails(Membership memberFound) {
        System.out.println("MEMBER ID >> M-" + memberFound.getId());
        System.out.println("MEMBER IC >> " + memberFound.getIc());
        System.out.println("MEMBER NAME >> " + memberFound.getName());
        System.out.println("MEMBER HP >> " + memberFound.getMemberHp());
        System.out.println("MEMBER TYPE >> " + memberFound.getMemberType());
    }

    public void displayMembersByType(List<Membership> memberList, String membershipType) {
        System.out.println("---------------------------------------------------------------------------------------");
        System.out.printf("%-9s | %-22s | %-11s | %-11s | %-12s%n",
                "MEMBER ID", "MEMBER NAME", "MEMBER HP", "MEMBER TYPE", "MEMBER IC");
        System.out.println("---------------------------------------------------------------------------------------");

        boolean foundMembers = false;

        for (Membership member : memberList) {
            boolean match = false;
            if (membershipType.equals(MemberConfig.MEMBER_TYPE_NORMAL) && member instanceof NormalMember) {
                match = true;
            } else if (membershipType.equals(MemberConfig.MEMBER_TYPE_GOLD) && member instanceof GoldMember) {
                match = true;
            } else if (membershipType.equals(MemberConfig.MEMBER_TYPE_PREMIUM) && member instanceof PremiumMember) {
                match = true;
            }

            if (match) {
                printMemberDetails(member);
                foundMembers = true;
            }
        }

        if (!foundMembers) {
            System.out.println(String.format(MemberConfig.ErrorMessage.NO_MEMBERS_TYPE_FOUND, membershipType));
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

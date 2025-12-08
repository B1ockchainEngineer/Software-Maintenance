package assignment.service;

import assignment.model.Membership;
import assignment.repo.MemberRepository;
import assignment.util.SalesUtil;
import assignment.util.config.MemberConfig;

import java.util.List;

/**
 * Service layer for Member domain.
 * Encapsulates business rules on top of MemberRepository.
 */
public class MemberService {

    private final MemberRepository memberRepo;

    public MemberService(MemberRepository memberRepo) {
        this.memberRepo = memberRepo;
    }

    /**
     * Result class for discount rate calculation.
     * Contains the discount rate, member information, and any error messages.
     */
    public static class DiscountResult {
        private final double discountRate;
        private final Membership member;
        private final String errorMessage;

        private DiscountResult(double discountRate, Membership member, String errorMessage) {
            this.discountRate = discountRate;
            this.member = member;
            this.errorMessage = errorMessage;
        }

        public static DiscountResult success(double discountRate, Membership member) {
            return new DiscountResult(discountRate, member, null);
        }

        public static DiscountResult error(double discountRate, String errorMessage) {
            return new DiscountResult(discountRate, null, errorMessage);
        }

        public double getDiscountRate() {
            return discountRate;
        }

        public Membership getMember() {
            return member;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public boolean hasError() {
            return errorMessage != null;
        }
    }

    /**
     * Gets a list of all members from the file.
     * Returns the list of members.
     */
    public List<Membership> getAllMembers() {
        return memberRepo.loadAllMembers();
    }

    /**
     * Adds a new member if IC is unique.
     * Returns true if added, false if IC already exists.
     */
    public boolean addMember(Membership newMember) {
        if (memberRepo.existsByIc(newMember.getIc())) {
            return false;
        }
        memberRepo.appendMember(newMember);
        return true;
    }

    /**
     * Checks if a member ID is already taken.
     * Returns true if ID exists, false otherwise.
     */
    public boolean checkIdExists (int id) {
        for (Membership member : memberRepo.loadAllMembers()) {
            if (member.getId() == id) {
                return true;
            }
        }
        return false;
    }

    /**
     * Finds a member by their ID.
     * Returns the member if found, or null if not found.
     */
    public Membership findMemberById(int memberId) {
        for (Membership member : memberRepo.loadAllMembers()) {
            if (member.getId() == memberId) {
                return member;
            }
        }
        return null;
    }

    /**
     * Finds the index of a member in the list by ID.
     * Returns the index number or INVALID_INPUT if not found.
     */
    public int findMemberIndexById(List<Membership> memberList ,int memberId) {
        for (int i=0; i < memberList.size(); i++) {
            if (memberList.get(i).getId() == memberId) {
                return i;
            }
        }
        return SalesUtil.INVALID_INPUT;
    }

    /**
     * Saves the list of members to the text file.
     * This overwrites the existing file.
     */
    public void saveMemberInfo(List<Membership> updatedMemberList) {
        memberRepo.saveAllMembers(updatedMemberList);
    }

    /**
     * Deletes a member by their ID.
     * Returns true if successful, false otherwise.
     */
    public boolean deleteMemberById(int memberId) {
        return memberRepo.deleteById(memberId);
    }

    /**
     * Checks if an IC number is already in the system.
     * Returns true if IC exists, false otherwise.
     */
    public boolean icExists(String targetIC) {
        return memberRepo.existsByIc(targetIC);
    }

    /**
     * Calculates the discount rate based on member input.
     * Handles parsing, validation, and member lookup.
     * 
     * @param memberInput The member ID input string (can be "0", "X", or a numeric ID)
     * @return DiscountResult containing discount rate, member info, and any error messages
     */
    public DiscountResult getDiscountRate(String memberInput) {
        // Default: no discount
        if (memberInput == null || memberInput.trim().isEmpty() || memberInput.equals("0")) {
            return DiscountResult.success(0.0, null);
        }

        // Parse member ID
        try {
            int memberId = Integer.parseInt(memberInput.trim());
            Membership member = findMemberById(memberId);

            if (member == null) {
                return DiscountResult.error(0.0, MemberConfig.MSG_MEMBER_NOT_FOUND_PAYMENT);
            }

            // Calculate discount rate from member's type
            double discountRate = member.calDiscount();
            return DiscountResult.success(discountRate, member);

        } catch (NumberFormatException e) {
            return DiscountResult.error(0.0, MemberConfig.MSG_INVALID_MEMBER_ID_FORMAT_PAYMENT);
        }
    }
}



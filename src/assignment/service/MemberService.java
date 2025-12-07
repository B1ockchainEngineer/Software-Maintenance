package assignment.service;

import assignment.model.Membership;
import assignment.repo.MemberRepository;

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
     * Returns the index number or -9999 if not found.
     */
    public int findMemberIndexById(List<Membership> memberList ,int memberId) {
        for (int i=0; i < memberList.size(); i++) {
            if (memberList.get(i).getId() == memberId) {
                return i;
            }
        }
        return -9999;
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
}



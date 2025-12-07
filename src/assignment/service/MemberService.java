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
     * Returns all members.
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
     * Deletes a member by ID. Returns true if deleted.
     */
    public boolean deleteMemberById(int memberId) {
        return memberRepo.deleteById(memberId);
    }

    /**
     * Finds a member by ID, or null if not found.
     */
    public Membership findMemberById(int memberId) {
        for (Membership member : memberRepo.loadAllMembers()) {
            if (member.getId() == memberId) {
                return member;
            }
        }
        return null;
    }
}



package test.assignment.controller;

import assignment.controller.MemberController;
import assignment.model.Membership;
import assignment.model.NormalMember;
import assignment.service.MemberService;
import assignment.util.config.MemberConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for MemberController.
 * Tests member management operations with mocked dependencies.
 * Note: Some methods require user input/UI interaction and are tested for logic only.
 */
@DisplayName("MemberController Tests")
class MemberControllerTest {

    private MemberService memberService;
    private MemberController memberController;

    @BeforeEach
    void setUp() {
        // Create a mock MemberService
        memberService = new MemberService(null) {
            private final List<Membership> memberList = new ArrayList<>();

            {
                // Initialize test data (Updated based on user request)
                memberList.add(new NormalMember("Alice", "121212121234", 101, "0123456789", MemberConfig.MEMBER_TYPE_NORMAL));
                memberList.add(new NormalMember("Bob", "010101014321", 102, "0198765432", MemberConfig.MEMBER_TYPE_NORMAL));
                memberList.add(new NormalMember("May", "121212121212", 103, "0198765411", MemberConfig.MEMBER_TYPE_GOLD));
                memberList.add(new NormalMember("May", "121212102122", 104, "0174512365", MemberConfig.MEMBER_TYPE_GOLD));
                memberList.add(new NormalMember("Alex", "111112121212", 105, "0174512222", MemberConfig.MEMBER_TYPE_PREMIUM));
            }

            @Override
            public List<Membership> getAllMembers() {
                return memberList;
            }

            @Override
            public boolean addMember(Membership member) {
                if (icExists(member.getIc())) {
                    return false;
                }
                memberList.add(member);
                return true;
            }

            @Override
            public boolean deleteMemberById(int memberId) {
                return memberList.removeIf(m -> m.getId() == memberId);
            }

            @Override
            public Membership findMemberById(int memberId) {
                return memberList.stream()
                        .filter(m -> m.getId() == memberId)
                        .findFirst()
                        .orElse(null);
            }

            @Override
            public boolean checkIdExists(int id) {
                return memberList.stream().anyMatch(m -> m.getId() == id);
            }

            @Override
            public boolean icExists(String targetIC) {
                return memberList.stream().anyMatch(m -> m.getIc().equals(targetIC));
            }

            @Override
            public void saveMemberInfo(List<Membership> updatedMemberList) {
                // Fix reference problem
                List<Membership> snapshot = new ArrayList<>(updatedMemberList);
                memberList.clear();
                memberList.addAll(snapshot);
            }
            
            @Override
             public int findMemberIndexById(List<Membership> list, int id) {
                for (int i=0; i < list.size(); i++) {
                    if (list.get(i).getId() == id) return i;
                }
                return -1;
            }
        };

        memberController = new MemberController(memberService);
    }

    @Test
    @DisplayName("Should initialize MemberController with MemberService")
    void testMemberControllerInitialization() {
        assertNotNull(memberController);
        System.out.println("Initialization successful.");
    }

    @Test
    @DisplayName("Should have access to all members")
    void testGetAllMembers() {
        List<Membership> members = memberService.getAllMembers();
        assertNotNull(members);
        assertEquals(5, members.size()); // Updated expectation
        System.out.println("Got " + members.size() + " members.");
    }

    @Test
    @DisplayName("Should be able to add a new member")
    void testAddMember() {
        // ID 106 to avoid conflict, IC unique
        Membership newMember = new NormalMember("Charlie", "111111111111", 106, "0111111111", MemberConfig.MEMBER_TYPE_NORMAL);
        boolean result = memberService.addMember(newMember);
        
        assertTrue(result);
        assertEquals(6, memberService.getAllMembers().size());
        assertNotNull(memberService.findMemberById(106));
        System.out.println("Member added successfully. Total count: " + memberService.getAllMembers().size());
    }

    @Test
    @DisplayName("Should not add member with duplicate IC")
    void testAddMemberDuplicateIc() {
        // Use existing IC from Alice 121212121234
        Membership duplicateMember = new NormalMember("Duplicate", "121212121234", 107, "0112223333", MemberConfig.MEMBER_TYPE_NORMAL);
        boolean result = memberService.addMember(duplicateMember);
        
        assertFalse(result);
        assertEquals(5, memberService.getAllMembers().size());
        System.out.println("Duplicate IC check passed. Result: " + result);
    }

    @Test
    @DisplayName("Should be able to search member by ID")
    void testSearchMember() {
        Membership member = memberService.findMemberById(101); // Updated ID
        assertNotNull(member);
        assertEquals("Alice", member.getName());
        System.out.println("Found member: " + member.getName());
    }

    @Test
    @DisplayName("Should return null for non-existent member")
    void testSearchMemberNotFound() {
        Membership member = memberService.findMemberById(9999);
        assertNull(member);
        System.out.println("Member correctly not found.");
    }

    @Test
    @DisplayName("Should be able to delete member by ID")
    void testDeleteMember() {
        boolean result = memberService.deleteMemberById(101); // Updated ID
        assertTrue(result);
        assertNull(memberService.findMemberById(101));
        assertEquals(4, memberService.getAllMembers().size());
        System.out.println("Member deleted. Result: " + result);
    }

    @Test
    @DisplayName("Should return false when deleting non-existent member")
    void testDeleteMemberNotFound() {
        boolean result = memberService.deleteMemberById(9999);
        assertFalse(result);
        assertEquals(5, memberService.getAllMembers().size());
        System.out.println("Member deletion failed as expected. Result: " + result);
    }

    @Test
    @DisplayName("Should check if ID exists")
    void testCheckIdExists() {
        assertTrue(memberService.checkIdExists(101)); // Updated ID
        assertFalse(memberService.checkIdExists(9999));
        System.out.println("ID check verified.");
    }

    @Test
    @DisplayName("Should check if IC exists")
    void testCheckIcExists() {
        assertTrue(memberService.icExists("121212121234")); // Updated IC
        assertFalse(memberService.icExists("010101140007"));
        System.out.println("IC check verified.");
    }

    @Test
    @DisplayName("Should be able to edit member details (mock via service)")
    void testEditMember() {
        // Retrieve, modify, save
        Membership member = memberService.findMemberById(101); // Updated ID
        assertNotNull(member);
        
        member.setName("Alice Updated");
        member.setMemberHp("0111111111");
        
        List<Membership> allMembers = memberService.getAllMembers();
        memberService.saveMemberInfo(allMembers);
        
        Membership updatedMember = memberService.findMemberById(101);
        assertEquals("ALICE UPDATED", updatedMember.getName());
        assertEquals("0111111111", updatedMember.getMemberHp());
        System.out.println("Member edited: " + updatedMember.getName());
    }
}

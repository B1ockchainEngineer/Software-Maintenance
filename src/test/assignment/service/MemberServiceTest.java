package assignment.service;

import assignment.model.Membership;
import assignment.model.NormalMember;
import assignment.model.GoldMember;
import assignment.model.PremiumMember;
import assignment.repo.MemberRepository;
import assignment.service.MemberService;
import assignment.util.SalesUtil;
import assignment.util.config.MemberConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for MemberService.
 * Tests business rules: uniqueness checks, retrieving members, discount logic.
 */
@DisplayName("MemberService Tests")
class MemberServiceTest {

    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(MemberServiceTest.class.getName());
    private MemberRepository memberRepo;
    private MemberService memberService;

    @BeforeEach
    void setUp() {
        // Create mock repository with in-memory data
        memberRepo = new MemberRepository() {
            private final List<Membership> memberList = new ArrayList<>();
            private final String DUMMY_PATH = "dummy_path";
            
            @Override
            public List<Membership> loadAllMembers() {
                // Initialize with test data if empty
                if (memberList.isEmpty()) {
                    memberList.add(new NormalMember("Alice", "121212121234", 101, "0123456789", MemberConfig.MEMBER_TYPE_NORMAL));
                    memberList.add(new NormalMember("Bob", "010101014321", 102, "0198765432", MemberConfig.MEMBER_TYPE_NORMAL));
                }
                return memberList;
            }

            @Override
            public void appendMember(Membership member) {
                memberList.add(member);
            }

            @Override
            public void saveAllMembers(List<Membership> members) {
                // Mock overwrite
                List<Membership> snapshot = new ArrayList<>(members);
                memberList.clear();
                memberList.addAll(snapshot);
            }

            @Override
            public boolean deleteById(int memberIdToDelete) {
                if (memberList.isEmpty()) {
                    loadAllMembers();
                }
                return memberList.removeIf(m -> m.getId() == memberIdToDelete);
            }

            @Override
            public boolean existsByIc(String targetIC) {
                if (memberList.isEmpty()) {
                    loadAllMembers();
                }
                return memberList.stream().anyMatch(m -> m.getIc().equals(targetIC));
            }
        };

        memberService = new MemberService(memberRepo);
    }

    @Test
    @DisplayName("Should return all members")
    void testGetAllMembers() {
        List<Membership> members = memberService.getAllMembers();
        assertNotNull(members);
        assertEquals(2, members.size());
        LOGGER.info("Success. Members count: " + members.size());
    }

    @Test
    @DisplayName("Should successfully add a new member")
    void testAddMember_Success() {
        Membership newMember = new NormalMember("Charlie", "111111111111", 103, "0111111111", MemberConfig.MEMBER_TYPE_NORMAL);
        boolean result = memberService.addMember(newMember);
        LOGGER.info("Add result: " + result);

        assertTrue(result);
        assertEquals(3, memberService.getAllMembers().size());
        assertEquals("Charlie", memberService.findMemberById(103).getName());
        LOGGER.info("Member 103 verified.");
    }

    @Test
    @DisplayName("Should fail to add member when IC already exists")
    void testAddMember_DuplicateIc() {
        // Alice has IC 121212121234
        Membership duplicateMember = new NormalMember("Duplicate", "121212121234", 104, "0112223333", MemberConfig.MEMBER_TYPE_NORMAL);
        boolean result = memberService.addMember(duplicateMember);
        LOGGER.info("Add duplicate result: " + result);

        assertFalse(result);
        assertEquals(2, memberService.getAllMembers().size());
    }

    @Test
    @DisplayName("Should check if ID exists")
    void testCheckIdExists() {
        assertTrue(memberService.checkIdExists(101));
        assertFalse(memberService.checkIdExists(9999));
        LOGGER.info("ID check passed.");
    }

    @Test
    @DisplayName("Should find member by ID")
    void testFindMemberById() {
        Membership found = memberService.findMemberById(101);
        assertNotNull(found);
        assertEquals(101, found.getId());
        assertEquals("Alice", found.getName());
        LOGGER.info("Found: " + found.getName());
    }

    @Test
    @DisplayName("Should return null when member not found by ID")
    void testFindMemberById_NotFound() {
        Membership found = memberService.findMemberById(9999);
        assertNull(found);
        LOGGER.info("Correctly returned null.");
    }

    @Test
    @DisplayName("Should find member index by ID")
    void testFindMemberIndexById() {
        List<Membership> members = memberService.getAllMembers();
        
        int index = memberService.findMemberIndexById(members, 102);
        // Bob is 2nd in list (index 1) if assume load order
        // List is [Alice, Bob]
        assertEquals(1, index);
        LOGGER.info("Index for 102: " + index);
    }

    @Test
    @DisplayName("Should return INVALID_INPUT when index not found")
    void testFindMemberIndexById_NotFound() {
        List<Membership> members = memberService.getAllMembers();
        int index = memberService.findMemberIndexById(members, 9999);
        assertEquals(SalesUtil.INVALID_INPUT, index);
        LOGGER.info("Index: " + index);
    }

    @Test
    @DisplayName("Should save member info (overwrite validation)")
    void testSaveMemberInfo() {
        List<Membership> members = memberService.getAllMembers();
        Membership m = members.get(0);
        m.setName("Alice Updated");
        
        memberService.saveMemberInfo(members);
        
        // Verify via repo (mock internal state updated)
        Membership updated = memberService.findMemberById(101);
        assertEquals("ALICE UPDATED", updated.getName());
        LOGGER.info("Saved name: " + updated.getName());
    }

    @Test
    @DisplayName("Should successfully delete member by ID")
    void testDeleteMemberById_Success() {
        boolean result = memberService.deleteMemberById(101);
        assertTrue(result);
        
        assertEquals(1, memberService.getAllMembers().size());
        assertNull(memberService.findMemberById(101));
        LOGGER.info("Deleted 101. Result: " + result);
    }

    @Test
    @DisplayName("Should return false when deleting non-existent member")
    void testDeleteMemberById_NotFound() {
        boolean result = memberService.deleteMemberById(9999);
        assertFalse(result);
        assertEquals(2, memberService.getAllMembers().size());
        LOGGER.info("Deletion failed. Result: " + result);
    }

    @Test
    @DisplayName("Should check if IC exists via service")
    void testIcExists() {
        assertTrue(memberService.icExists("121212121234"));
        assertFalse(memberService.icExists("000000000000"));
        LOGGER.info("IC check verified.");
    }

    @Test
    @DisplayName("Should calculate discount for valid member ID")
    void testGetDiscountRate_Success() {
        // Add a Gold member
        Membership goldMember = new GoldMember("Goldie", "999999999999", 200, "0199999999", MemberConfig.MEMBER_TYPE_GOLD);
        memberService.addMember(goldMember);

        MemberService.DiscountResult result = memberService.getDiscountRate("200");
        
        assertFalse(result.hasError());
        assertEquals(MemberConfig.DISCOUNT_RATE_GOLD, result.getDiscountRate()); // 0.10
        assertEquals(goldMember, result.getMember());
        assertNull(result.getErrorMessage());
        LOGGER.info("Discount Rate: " + result.getDiscountRate());
    }

    @Test
    @DisplayName("Should return 0 discount for null/empty/0 input")
    void testGetDiscountRate_NoMember() {
        MemberService.DiscountResult r1 = memberService.getDiscountRate(null);
        assertEquals(0.0, r1.getDiscountRate());
        
        MemberService.DiscountResult r2 = memberService.getDiscountRate("");
        assertEquals(0.0, r2.getDiscountRate());

        MemberService.DiscountResult r3 = memberService.getDiscountRate("0");
        assertEquals(0.0, r3.getDiscountRate());
        LOGGER.info("Discount for invalid inputs checked (0.0).");
    }

    @Test
    @DisplayName("Should return error for non-existent member ID")
    void testGetDiscountRate_NotFound() {
        MemberService.DiscountResult result = memberService.getDiscountRate("9999");
        
        assertTrue(result.hasError());
        assertEquals(0.0, result.getDiscountRate());
        assertEquals(MemberConfig.MSG_MEMBER_NOT_FOUND_PAYMENT, result.getErrorMessage());
        LOGGER.info("Error msg: " + result.getErrorMessage());
    }

    @Test
    @DisplayName("Should return error for invalid format member ID")
    void testGetDiscountRate_InvalidFormat() {
        MemberService.DiscountResult result = memberService.getDiscountRate("ABC");
        
        assertTrue(result.hasError());
        assertEquals(0.0, result.getDiscountRate());
        assertEquals(MemberConfig.MSG_INVALID_MEMBER_ID_FORMAT_PAYMENT, result.getErrorMessage());
        LOGGER.info("Error msg: " + result.getErrorMessage());
    }
}

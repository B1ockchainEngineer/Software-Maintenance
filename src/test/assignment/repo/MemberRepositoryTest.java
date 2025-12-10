package test.assignment.repo;

import assignment.model.Membership;
import assignment.model.NormalMember;
import assignment.repo.MemberRepository;
import assignment.util.config.MemberConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for MemberRepository.
 * Tests file I/O operations for members.
 */
@DisplayName("MemberRepository Tests")
class MemberRepositoryTest {

    private MemberRepository memberRepository;
    private File tempFile;

    @BeforeEach
    void setUp(@TempDir Path tempDir) {
        // Create a temporary file for each test
        // This ensures we do not overwrite the actual members.txt file
        tempFile = tempDir.resolve("test_members.txt").toFile();
        memberRepository = new MemberRepository(tempFile.getAbsolutePath());
    }

    @AfterEach
    void tearDown() {
        if (tempFile.exists()) {
            tempFile.delete();
        }
    }

    @Test
    @DisplayName("Should append member to file")
    void testAppendMember() {
        // Use data format from MemberControllerTest (ID 101)
        Membership member = new NormalMember("Alice", "121212121234", 101, "0123456789", MemberConfig.MEMBER_TYPE_NORMAL);

        memberRepository.appendMember(member);

        List<Membership> members = memberRepository.loadAllMembers();
        assertEquals(1, members.size());
        assertEquals("Alice", members.get(0).getName());
        assertEquals("121212121234", members.get(0).getIc());
        assertEquals(101, members.get(0).getId());
    }

    @Test
    @DisplayName("Should load all members from file")
    void testLoadAllMembers() {
        // Append multiple members (IDs 101-103)
        memberRepository.appendMember(new NormalMember("Alice", "121212121234", 101, "0123456789", MemberConfig.MEMBER_TYPE_NORMAL));
        memberRepository.appendMember(new NormalMember("Bob", "010101014321", 102, "0198765432", MemberConfig.MEMBER_TYPE_NORMAL));
        memberRepository.appendMember(new NormalMember("May", "121212121212", 103, "0198765411", MemberConfig.MEMBER_TYPE_GOLD));

        List<Membership> members = memberRepository.loadAllMembers();

        assertNotNull(members);
        assertEquals(3, members.size());
        
        // Verify details
        assertEquals("Alice", members.get(0).getName());
        assertEquals(101, members.get(0).getId());
        
        assertEquals("Bob", members.get(1).getName());
        assertEquals(102, members.get(1).getId());
        
        assertEquals("May", members.get(2).getName());
        assertEquals(103, members.get(2).getId());
        assertEquals(MemberConfig.MEMBER_TYPE_GOLD, members.get(2).getMemberType());
    }

    @Test
    @DisplayName("Should save all members (overwrite file)")
    void testSaveAllMembers() {
        // Initial save
        List<Membership> initialMembers = List.of(
            new NormalMember("Alice", "121212121234", 101, "0123456789", MemberConfig.MEMBER_TYPE_NORMAL),
            new NormalMember("Bob", "010101014321", 102, "0198765432", MemberConfig.MEMBER_TYPE_NORMAL)
        );
        memberRepository.saveAllMembers(initialMembers);

        List<Membership> loaded = memberRepository.loadAllMembers();
        assertEquals(2, loaded.size());

        // Overwrite with new list ( Update Bob, remove Alice, add Charlie)
        List<Membership> updatedMembers = List.of(
            new NormalMember("Bob Updated", "010101014321", 102, "0112223333", MemberConfig.MEMBER_TYPE_NORMAL),
            new NormalMember("Charlie", "111111111111", 106, "0111111111", MemberConfig.MEMBER_TYPE_NORMAL)
        );
        memberRepository.saveAllMembers(updatedMembers);

        List<Membership> finalLoaded = memberRepository.loadAllMembers();
        assertEquals(2, finalLoaded.size());
        assertEquals("Bob Updated", finalLoaded.get(0).getName());
        assertEquals("Charlie", finalLoaded.get(1).getName());
    }

    @Test
    @DisplayName("Should delete member by ID")
    void testDeleteById() {
        // Setup initial data
        memberRepository.appendMember(new NormalMember("Alice", "121212121234", 101, "0123456789", MemberConfig.MEMBER_TYPE_NORMAL));
        memberRepository.appendMember(new NormalMember("Bob", "010101014321", 102, "0198765432", MemberConfig.MEMBER_TYPE_NORMAL));

        // Delete Alice (101)
        boolean result = memberRepository.deleteById(101);
        
        assertTrue(result);

        List<Membership> members = memberRepository.loadAllMembers();
        assertEquals(1, members.size());
        assertEquals("Bob", members.get(0).getName());
        assertEquals(102, members.get(0).getId());
    }

    @Test
    @DisplayName("Should return false when deleting non-existent member")
    void testDeleteByIdNotFound() {
        memberRepository.appendMember(new NormalMember("Alice", "121212121234", 101, "0123456789", MemberConfig.MEMBER_TYPE_NORMAL));

        boolean result = memberRepository.deleteById(999);
        
        assertFalse(result);
        assertEquals(1, memberRepository.loadAllMembers().size());
    }

    @Test
    @DisplayName("Should check if IC exists")
    void testExistsByIc() {
        memberRepository.appendMember(new NormalMember("Alice", "121212121234", 101, "0123456789", MemberConfig.MEMBER_TYPE_NORMAL));

        assertTrue(memberRepository.existsByIc("121212121234"));
        assertFalse(memberRepository.existsByIc("000000000000"));
    }

    @Test
    @DisplayName("Should return empty list for new/empty file")
    void testLoadAllMembersEmpty() {
        List<Membership> members = memberRepository.loadAllMembers();
        assertNotNull(members);
        assertTrue(members.isEmpty());
    }
}

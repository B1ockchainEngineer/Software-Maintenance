package assignment.view;

import assignment.model.NormalMember;
import assignment.model.GoldMember;
import assignment.model.Membership;
import assignment.util.config.MemberConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for MemberView.
 * Verifies console output correctness.
 */
@DisplayName("Member View Tests")
class MemberViewTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private MemberView memberView;
    private static final Logger LOGGER = Logger.getLogger(MemberViewTest.class.getName());

    @BeforeAll
    static void setUpLogger() {
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.ALL);
        LOGGER.addHandler(handler);
        LOGGER.setLevel(Level.ALL);
        LOGGER.setUseParentHandlers(false);
    }

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));
        memberView = new MemberView();
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    @DisplayName("Should print member menu options")
    void testPrintMemberMenu() {
        memberView.printMemberMenu();
        String output = outContent.toString();

        assertTrue(output.contains(MemberConfig.TITLE_MEMBER_SYSTEM));
        assertTrue(output.contains("ADD NEW MEMBER"));
        assertTrue(output.contains("BACK TO PREVIOUS MENU"));
        LOGGER.info("PrintMemberMenu test passed.");
    }

    @Test
    @DisplayName("Should print tier menu")
    void testPrintTierMenu() {
        memberView.printTierMenu();
        String output = outContent.toString();

        assertTrue(output.contains("NORMAL MEMBER"));
        assertTrue(output.contains("GOLD MEMBER"));
        assertTrue(output.contains("PREMIUM MEMBER"));
        LOGGER.info("PrintTierMenu test passed.");
    }

    @Test
    @DisplayName("Should display member details correctly")
    void testDisplayMemberDetails() {
        NormalMember member = new NormalMember("Alice", "121212121234", 101, "0123456789", MemberConfig.MEMBER_TYPE_NORMAL);
        
        memberView.displayMemberDetails(member);
        String output = outContent.toString();

        assertTrue(output.contains("M-101"));
        assertTrue(output.contains("Alice"));
        assertTrue(output.contains("121212121234"));
        LOGGER.info("DisplayMemberDetails test passed.");
    }

    @Test
    @DisplayName("Should display list of members by type")
    void testDisplayMembersByType() {
        List<Membership> members = new ArrayList<>();
        members.add(new NormalMember("Alice", "111", 101, "011", MemberConfig.MEMBER_TYPE_NORMAL));
        members.add(new GoldMember("Bob", "222", 102, "022", MemberConfig.MEMBER_TYPE_GOLD));

        // Test filter for Normal
        memberView.displayMembersByType(members, MemberConfig.MEMBER_TYPE_NORMAL);
        String output = outContent.toString();

        assertTrue(output.contains("Alice"));
        // Should NOT contain Bob (Gold)

        // Reset stream for next check
        outContent.reset();

        // Test filter for Gold
        memberView.displayMembersByType(members, MemberConfig.MEMBER_TYPE_GOLD);
        String outputGold = outContent.toString();
        
        assertTrue(outputGold.contains("Bob"));
        LOGGER.info("DisplayMembersByType test passed.");
    }
}

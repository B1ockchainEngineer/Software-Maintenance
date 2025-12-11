package assignment.model;

import assignment.util.config.MemberConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.BeforeAll;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Membership subclasses.
 * Verifies inheritance, data encapsulation, and discount calculations.
 */
@DisplayName("Member Model Tests")
class MemberModelTest {

    private static final Logger logger = Logger.getLogger(MemberModelTest.class.getName());

    @BeforeAll
    static void setUpLogger() {
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.ALL);
        logger.addHandler(handler);
        logger.setLevel(Level.ALL);
        logger.setUseParentHandlers(false);
    }

    @Test
    @DisplayName("NormalMember should initialize correctly and calculate discount")
    void testNormalMember() {
        NormalMember member = new NormalMember("Alice", "121212121234", 101, "0123456789", MemberConfig.MEMBER_TYPE_NORMAL);

        assertEquals("Alice", member.getName());
        assertEquals("121212121234", member.getIc());
        assertEquals(101, member.getId());
        assertEquals(MemberConfig.MEMBER_TYPE_NORMAL, member.getMemberType());
        
        // Test discount
        // Default base discount is 0 unless set.
        double expectedDiscount = member.calculateDiscountRate() + NormalMember.getNormalRate();
        assertEquals(expectedDiscount, member.calDiscount(), 0.001);

        // Test toString contains specific info
        String info = member.toString();
        assertTrue(info.contains("MEMBER ID >> M-101"));
        assertTrue(info.contains("NORMAL DISCOUNT RATE"));
        logger.info("NormalMember test passed. Discount: " + expectedDiscount);
    }

    @Test
    @DisplayName("GoldMember should initialize correctly and calculate discount")
    void testGoldMember() {
        GoldMember member = new GoldMember("Bob", "111111111111", 102, "0111111111", MemberConfig.MEMBER_TYPE_GOLD);

        assertEquals("Bob", member.getName());
        assertEquals(MemberConfig.MEMBER_TYPE_GOLD, member.getMemberType());
        
        double expectedDiscount = member.calculateDiscountRate() + GoldMember.getGoldRate();
        assertEquals(expectedDiscount, member.calDiscount(), 0.001);

        String info = member.toString();
        assertTrue(info.contains("GOLD DISCOUNT RATE"));
        logger.info("GoldMember test passed. Discount: " + expectedDiscount);
    }

    @Test
    @DisplayName("PremiumMember should initialize correctly and calculate discount")
    void testPremiumMember() {
        PremiumMember member = new PremiumMember("Charlie", "222222222222", 103, "0122222222", MemberConfig.MEMBER_TYPE_PREMIUM);

        assertEquals("Charlie", member.getName());
        assertEquals(MemberConfig.MEMBER_TYPE_PREMIUM, member.getMemberType());
        
        double expectedDiscount = member.calculateDiscountRate() + PremiumMember.getPremiumRate();
        assertEquals(expectedDiscount, member.calDiscount(), 0.001);

        String info = member.toString();
        assertTrue(info.contains("PREMIUM DISCOUNT RATE"));
        logger.info("PremiumMember test passed. Discount: " + expectedDiscount);
    }

    @Test
    @DisplayName("Should update static rates correctly")
    void testStaticRateUpdates() {
        // Save original rates
        double originalNormal = NormalMember.getNormalRate();

        // Update and verify
        NormalMember.setNormalRate(0.50);
        assertEquals(0.50, NormalMember.getNormalRate(), 0.001);

        // Restore
        NormalMember.setNormalRate(originalNormal);
        logger.info("Static rate update test passed.");
    }
}

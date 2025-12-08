package assignment.model;

import assignment.util.config.MemberConfig;

/**
 * Gold membership tier.
 */
public class GoldMember extends Membership {

    private static double goldRate = MemberConfig.DISCOUNT_RATE_GOLD;

    public GoldMember() {
    }

    public GoldMember(String name, String ic, int id, String memberHp, String memberType) {
        super(name, ic, id, memberHp, memberType);
    }

    public static void setGoldRate(double goldRate) {
        GoldMember.goldRate = goldRate;
    }

    public static double getGoldRate() {
        return goldRate;
    }

    @Override
    public double calDiscount() {
        return super.calculateDiscountRate() + goldRate;
    }

    @Override
    public String toString() {
        return super.toString()
                + "\nGOLD DISCOUNT RATE: " + goldRate;
    }
}



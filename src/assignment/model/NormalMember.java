package assignment.model;

/**
 * Normal membership tier.
 */
public class NormalMember extends Membership {

    private static double normalRate = 0.05;

    public NormalMember() {
    }

    public NormalMember(String name, String ic, int id, String memberHp, String memberType) {
        super(name, ic, id, memberHp, memberType);
    }

    public static void setNormalRate(double normalRate) {
        NormalMember.normalRate = normalRate;
    }

    public static double getNormalRate() {
        return normalRate;
    }

    @Override
    public double calDiscount() {
        return super.calculateDiscountRate() + normalRate;
    }

    @Override
    public String toString() {
        return super.toString()
                + "\nNORMAL DISCOUNT RATE: " + normalRate;
    }
}



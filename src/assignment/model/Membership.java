package assignment.model;

/**
 * Membership management base class using ConsoleUtil for output.
 */
public abstract class Membership extends Person {

    private String memberHp;
    private String memberType;
    private double discountRate = 0;
    private static int noOfMember = 0;

    public Membership() {
    }

    public Membership(String name, String ic, int id, String memberHp, String memberType) {
        super(name, ic, id);
        this.memberHp = memberHp;
        this.memberType = memberType;
        noOfMember++;
    }

    public String getMemberHp() {
        return memberHp;
    }

    public String getMemberType() {
        return memberType;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public static int getNoOfMember() {
        return noOfMember;
    }

    public void setMemberHp(String memberHp) {
        this.memberHp = memberHp;
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }

    public void setDiscountRate(double discountRate) {
        this.discountRate = discountRate;
    }

    public double calculateDiscountRate() {
        return discountRate;
    }

    public abstract double calDiscount();

    @Override
    public String toString() {
        return "\nMEMBER INFO\n-------------\nMEMBER ID >> M-" + getId()
                + "\nMEMBER NAME: " + getName()
                + "\nMEMBER IC: " + getIc()
                + "\nMEMBER HP: " + getMemberHp()
                + "\nTYPE OF MEMBERSHIP: " + getMemberType();
    }

}



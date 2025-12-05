package assignment.model;

import java.time.LocalDateTime;

/**
 * Staff domain model.
 * Note: UI logic has been moved to StaffController.
 * This class now only contains data and business logic.
 */
public class Staff extends Person {

    private int stfAge;
    private String stfPassword;
    private double stfSalary;
    private LocalDateTime clockIn, clockOut;

    public Staff() {
    }

    public Staff(String name, String ic, int stfAge, double stfSalary, String stfPassword) {
        super(name, ic, Integer.parseInt(ic.substring(6)));
        this.stfAge = stfAge;
        this.stfSalary = stfSalary;
        this.stfPassword = stfPassword;
    }

    public String getStfIC() {
        return getIc();
    }

    public int getStfAge() {
        return stfAge;
    }

    public double getStfSalary() {
        return stfSalary;
    }

    public String getStfPassword() {
        return stfPassword;
    }

    public LocalDateTime getClockIn() {
        return clockIn;
    }

    public LocalDateTime getClockOut() {
        return clockOut;
    }

    public void setStfName(String stfName) {
        setName(stfName.toUpperCase());
    }

    public void setStfIC(String stfIC) {
        setIc(stfIC);
    }

    public void setStfId(int stfId) {
        setId(stfId);
    }

    public void setStfAge(int stfAge) {
        this.stfAge = stfAge;
    }

    public void setStfSalary(double stfSalary) {
        this.stfSalary = stfSalary;
    }

    public void setStfPassword(String stfPassword) {
        this.stfPassword = stfPassword;
    }

    public void setClockIn(LocalDateTime clockIn) {
        this.clockIn = clockIn;
    }

    public void setClockOut(LocalDateTime clockOut) {
        this.clockOut = clockOut;
    }

    /**
     * Required by Person abstract class.
     * UI logic is handled by StaffController.
     */
    public void add() {
        throw new UnsupportedOperationException("Use StaffController.addStaff() instead");
    }

    /**
     * Required by Person abstract class.
     * UI logic is handled by StaffController.
     */
    public void delete() {
        throw new UnsupportedOperationException("Use StaffController.deleteStaff() instead");
    }

    /**
     * Required by Person abstract class.
     * UI logic is handled by StaffController.
     */
    public void view() {
        throw new UnsupportedOperationException("Use StaffController.viewStaffList() instead");
    }

    /**
     * Required by Person abstract class.
     * UI logic is handled by StaffController.
     */
    public void search() {
        throw new UnsupportedOperationException("Use StaffController.searchStaff() instead");
    }

    @Override
    public String toString() {
        return "\nSTAFF INFO\n-------------\nSTAFF ID >> S-" + getId()
                + "\nSTAFF NAME: " + getName()
                + "\nSTAFF IC: " + getIc()
                + "\nSTAFF AGE: " + getStfAge()
                + "\nSTAFF SALARY: RM " + String.format("%.2f", getStfSalary());
    }
}



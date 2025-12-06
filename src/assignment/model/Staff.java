package assignment.model;

import assignment.repo.StaffRepository;
import assignment.util.ConsoleUtil;
import assignment.util.ValidationUtil;
import java.time.LocalDateTime;

/**
 * Staff domain model.
 * Note: UI logic has been moved to StaffController.
 * This class now only contains data and business logic.
 */
public class Staff extends Person {

    private final List<Staff> staffList = new ArrayList<>(); // Store staff in a list
    private static final StaffRepository staffRepo = new StaffRepository();
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
    @Override
    public void add() {
        throw new UnsupportedOperationException("Use StaffController.addStaff() instead");
    }

    /**
     * Required by Person abstract class.
     * UI logic is handled by StaffController.
     */
    @Override
    public void delete() {
        throw new UnsupportedOperationException("Use StaffController.deleteStaff() instead");
    }

    /**
     * Required by Person abstract class.
     * UI logic is handled by StaffController.
     */
    @Override
    public void view() {
        throw new UnsupportedOperationException("Use StaffController.viewStaffList() instead");
    }

    /**
     * Required by Person abstract class.
     * UI logic is handled by StaffController.
     */
    @Override
    public void search() {
        throw new UnsupportedOperationException("Use StaffController.searchStaff() instead");}

    @Override
    public void add() {
        Scanner scanner = new Scanner(System.in);
        String stfName, stfIc, stfPW, confirmPassword;
        int stfAGE;
        double stfSlr;

        ConsoleUtil.logo();
        System.out.println("[ REGISTER STAFF ]");
        System.out.println("-------------------------------------------------------");

        do {
            do {
                System.out.print("ENTER NEW STAFF IC: ");
                stfIc = ValidationUtil.icValidation();
            }while(stfIc == null);

            if (staffRepo.existsByIc(stfIc)) {
                System.out.println("<<<IC already exists in the file. Please reenter!>>>");
            } else {
                break; // IC is valid and does not exist in the file
            }

        } while (true);

        do {
            System.out.print("ENTER NEW STAFF NAME: ");
            stfName = scanner.nextLine();
            if (stfName.matches("^[a-zA-Z ]+$")) {
                break; // Valid input, exit the loop
            } else {
                System.out.println("Invalid input. Please enter a name with alphabet characters only.");
            }
        } while (true);

        do {
            System.out.print("ENTER NEW PASSWORD (8-16 alphanumeric characters): ");
            stfPW = scanner.nextLine();
            if (stfPW.matches("^[a-zA-Z0-9]{8,16}$")) {
                System.out.print("CONFIRM PASSWORD: ");
                confirmPassword = scanner.nextLine();
                if (stfPW.equals(confirmPassword)) {
                    break; // Valid input and confirmed, exit the loop
                } else {
                    System.out.println("<<<Password does not match the confirmation. Please re-enter!>>>");
                }
            } else {
                System.out.println("<<<Invalid input. Please enter a password with 8-16 alphanumeric characters!>>>");
            }
        } while (true);

        do {
            System.out.print("ENTER NEW STAFF AGE (between 18 and 54): ");
            stfAGE = scanner.nextInt();
            if (stfAGE >= 18 && stfAGE <= 54) {
                break; // Valid input, exit the loop
            } else {
                System.out.println("<<<Invalid input. Please enter an age between 18 and 54!>>>");
            }
        } while (true);

        do {
            System.out.print("ENTER NEW STAFF SALARY (digits only): ");
            if (scanner.hasNextDouble()) {
                stfSlr = scanner.nextDouble();
                if (stfSlr >= 0) {
                    break;
                } else {
                    System.out.println("<<Invalid input. Please enter a salary that is more than 0>>");
                }
            } else {
                System.out.println("<<<Invalid input. Please enter a salary with digits only!>>>");
                scanner.next(); // Consume the invalid input
            }
        } while (true);

        // Read the current staff ID from the file
        int currentStaffId = Integer.parseInt(stfIc.substring(6));

        // Update the staff ID for the new staff member
        setId(currentStaffId);
        setIc(stfIc);
        setName(stfName);
        setStfPassword(stfPW);
        setStfAge(stfAGE);
        setStfSalary(stfSlr);

        // Persist new staff record via repository
        staffRepo.appendStaff(this);
        // Refresh in-memory list
        loadStaffFromRepo();
        ConsoleUtil.systemPause();
        ConsoleUtil.clearScreen();
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
        loadStaffFromRepo();
        Scanner scanner = new Scanner(System.in);

        ConsoleUtil.logo();
        System.out.println("[ SEARCH STAFF BY ID ]");
        System.out.println("-------------------------------------------------------");

        System.out.print("ENTER STAFF ID TO SEARCH (5/6 DIGIT ONLY) OR '0' TO CANCEL: S-");
        int staffIdToSearch = scanner.nextInt();

        if (staffIdToSearch == 0) {
            // User wants to cancel and return to the main menu
            ConsoleUtil.clearScreen();
            return;
        }

        boolean found = false;

        for (Staff staff : staffList) {
            if (staff.getId() == staffIdToSearch) {
                found = true;
                System.out.println("\tSTAFF FOUND !");
                System.out.println(staff.toString());
                break; // Exit the loop once a staff is found
            }
        }

        if (!found) {
            System.out.println("\tSTAFF NOT FOUND...");
        }
        ConsoleUtil.systemPause();
        System.out.println("\n");
        ConsoleUtil.clearScreen();
    }

    public boolean login(String stfIc, String stfPassword) {
        Staff found = staffRepo.findByCredentials(stfIc, stfPassword);
        if (found == null) {
            System.out.println("<<<No staff records found or invalid credentials>>>");
            return false;
        }

        // Copy details from repository result
        setId(found.getId());
        setStfIC(found.getStfIC());
        setStfName(found.getName());
        setStfPassword(found.getStfPassword());
        setStfAge(found.getStfAge());
        setStfSalary(found.getStfSalary());

        LocalDateTime loginTime = LocalDateTime.now();
        setClockIn(loginTime);
        System.out.println("LOGIN SUCCESSFUL FOR " + getName() + " AT " + loginTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        return true;
        throw new UnsupportedOperationException("Use StaffController.searchStaff() instead");
    }

    @Override
    public String toString() {
        return "\nSTAFF INFO\n-------------\nSTAFF ID >> S-" + getId()
                + "\nSTAFF NAME: " + getName()
                + "\nSTAFF IC: " + getIc()
                + "\nSTAFF AGE: " + getStfAge()
                + "\nSTAFF SALARY: RM " + String.format("%.2f", getStfSalary());
                + "\nSATFF AGE: " + getStfAge()
                + "\nSTAFF SALARY: " + getStfSalary();
    }

    private void loadStaffFromRepo() {
        staffList.clear();
        staffList.addAll(staffRepo.loadAllStaff());
                + "\nSTAFF AGE: " + getStfAge()
                + "\nSTAFF SALARY: RM " + String.format("%.2f", getStfSalary());
    }
}}



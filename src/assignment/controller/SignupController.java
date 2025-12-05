package assignment.controller;

import assignment.model.Staff;
import assignment.service.StaffService;
import assignment.util.ConsoleUtil;
import assignment.util.ValidationUtil;

/**
 * Controller for handling staff signup/registration functionality.
 * Separates signup UI logic from Main class.
 */
public class SignupController {
    
    private static final int REGISTRATION_CODE = 1234;
    private final StaffService staffService;
    
    public SignupController(StaffService staffService) {
        this.staffService = staffService;
    }
    
    /**
     * Displays the signup screen and handles staff registration.
     * @return true if registration was successful, false if cancelled
     */
    public boolean performSignup() {
        ConsoleUtil.clearScreen();
        ConsoleUtil.logo();
        displaySignupHeader();
        
        // Step 1: Verify registration code
        System.out.print("ENTER REGISTRATION CODE (OR 'E' TO RETURN): ");
        String input = ValidationUtil.scanner.nextLine().trim();
        
        if (input.equalsIgnoreCase("E")) {
            return false;
        }
        
        // Validate registration code
        int registrationCode;
        try {
            registrationCode = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("\n<<<INVALID INPUT! REGISTRATION CODE MUST BE A NUMBER!>>>");
            ConsoleUtil.systemPause();
            return false;
        }
        
        if (registrationCode != REGISTRATION_CODE) {
            System.out.println("\n<<<INCORRECT REGISTRATION CODE! REGISTRATION DENIED!>>>");
            ConsoleUtil.systemPause();
            return false;
        }
        
        // Step 2: Registration code verified, proceed to staff registration
        System.out.println("\n========================================");
        System.out.println("  REGISTRATION CODE VERIFIED!");
        System.out.println("  PROCEEDING TO STAFF REGISTRATION...");
        System.out.println("========================================\n");
        ConsoleUtil.systemPause();
        
        // Step 3: Collect staff information
        Staff newStaff = collectStaffInformation();
        
        if (newStaff == null) {
            // User cancelled during information collection
            return false;
        }
        
        // Step 4: Attempt to add staff
        boolean success = staffService.addStaff(newStaff);
        
        if (success) {
            System.out.println("\n========================================");
            System.out.println("  REGISTRATION SUCCESSFUL!");
            System.out.println("========================================");
            System.out.println("STAFF ID: S-" + newStaff.getId());
            System.out.println("NAME: " + newStaff.getName());
            System.out.println("IC: " + newStaff.getStfIC());
            System.out.println("\nYou can now log in with your IC and password.");
            System.out.println("========================================\n");
        } else {
            System.out.println("\n<<<REGISTRATION FAILED! IC ALREADY EXISTS IN THE SYSTEM!>>>");
        }
        
        ConsoleUtil.systemPause();
        return success;
    }
    
    /**
     * Displays the signup header.
     */
    private void displaySignupHeader() {
        System.out.println("[ STAFF REGISTRATION ]");
        System.out.println("-------------------------------------------------------");
        System.out.println("Please enter the registration code to create a new staff account.");
        System.out.println("-------------------------------------------------------\n");
    }
    
    /**
     * Collects staff information from the user.
     * @return Staff object with collected information, or null if user cancels
     */
    private Staff collectStaffInformation() {
        ConsoleUtil.clearScreen();
        ConsoleUtil.logo();
        System.out.println("[ REGISTER STAFF ]");
        System.out.println("-------------------------------------------------------");
        System.out.println("Please fill in the following information:");
        System.out.println("-------------------------------------------------------\n");
        
        String ic = collectIC();
        if (ic == null) return null;
        
        String name = collectName();
        if (name == null) return null;
        
        String password = collectPassword();
        if (password == null) return null;
        
        Integer age = collectAge();
        if (age == null) return null;
        
        Double salary = collectSalary();
        if (salary == null) return null;
        
        // Create staff object
        int staffId = Integer.parseInt(ic.substring(6));
        Staff staff = new Staff(name, ic, age, salary, password);
        staff.setId(staffId);
        
        return staff;
    }
    
    /**
     * Collects and validates IC number.
     */
    private String collectIC() {
        while (true) {
            System.out.print("ENTER NEW STAFF IC (12 digits): ");
            String ic = ValidationUtil.scanner.nextLine().trim();
            
            if (ic.equalsIgnoreCase("E")) {
                return null;
            }
            
            if (!ic.matches("\\d{12}")) {
                System.out.println("<<<INVALID INPUT! Please enter a 12-digit IC number!>>>\n");
                continue;
            }
            
            if (staffService.getAllStaff().stream()
                    .anyMatch(s -> s.getStfIC().equals(ic))) {
                System.out.println("<<<IC ALREADY EXISTS! Please use a different IC!>>>\n");
                continue;
            }
            
            return ic;
        }
    }
    
    /**
     * Collects and validates staff name.
     */
    private String collectName() {
        while (true) {
            System.out.print("ENTER NEW STAFF NAME (alphabets only): ");
            String name = ValidationUtil.scanner.nextLine().trim();
            
            if (name.equalsIgnoreCase("E")) {
                return null;
            }
            
            if (!name.matches("^[a-zA-Z ]+$")) {
                System.out.println("<<<INVALID INPUT! Please enter a name with alphabet characters only!>>>\n");
                continue;
            }
            
            if (name.trim().isEmpty()) {
                System.out.println("<<<NAME CANNOT BE EMPTY!>>>\n");
                continue;
            }
            
            return name.toUpperCase();
        }
    }
    
    /**
     * Collects and validates password.
     */
    private String collectPassword() {
        while (true) {
            System.out.print("ENTER NEW PASSWORD (8-16 alphanumeric characters): ");
            String password = ValidationUtil.scanner.nextLine();
            
            if (password.equalsIgnoreCase("E")) {
                return null;
            }
            
            if (!password.matches("^[a-zA-Z0-9]{8,16}$")) {
                System.out.println("<<<INVALID INPUT! Password must be 8-16 alphanumeric characters!>>>\n");
                continue;
            }
            
            System.out.print("CONFIRM PASSWORD: ");
            String confirmPassword = ValidationUtil.scanner.nextLine();
            
            if (!password.equals(confirmPassword)) {
                System.out.println("<<<PASSWORDS DO NOT MATCH! Please try again!>>>\n");
                continue;
            }
            
            return password;
        }
    }
    
    /**
     * Collects and validates age.
     */
    private Integer collectAge() {
        while (true) {
            System.out.print("ENTER NEW STAFF AGE (18-54): ");
            String input = ValidationUtil.scanner.nextLine().trim();
            
            if (input.equalsIgnoreCase("E")) {
                return null;
            }
            
            try {
                int age = Integer.parseInt(input);
                if (age >= 18 && age <= 54) {
                    return age;
                } else {
                    System.out.println("<<<INVALID INPUT! Age must be between 18 and 54!>>>\n");
                }
            } catch (NumberFormatException e) {
                System.out.println("<<<INVALID INPUT! Please enter a valid number!>>>\n");
            }
        }
    }
    
    /**
     * Collects and validates salary.
     */
    private Double collectSalary() {
        while (true) {
            System.out.print("ENTER NEW STAFF SALARY (must be > 0): ");
            String input = ValidationUtil.scanner.nextLine().trim();
            
            if (input.equalsIgnoreCase("E")) {
                return null;
            }
            
            try {
                double salary = Double.parseDouble(input);
                if (salary > 0) {
                    return salary;
                } else {
                    System.out.println("<<<INVALID INPUT! Salary must be greater than 0!>>>\n");
                }
            } catch (NumberFormatException e) {
                System.out.println("<<<INVALID INPUT! Please enter a valid number!>>>\n");
            }
        }
    }
}



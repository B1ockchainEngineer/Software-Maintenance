package assignment.controller;

import assignment.model.Staff;
import assignment.service.StaffService;
import assignment.util.ConsoleUtil;
import assignment.util.ValidationUtil;
import assignment.util.MemberUtil;
import assignment.util.PasswordStrengthUtil;
import assignment.util.PasswordStrengthUtil.PasswordStrength;
import assignment.util.PasswordUtil;

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
        System.out.println("STEP 1/6: REGISTRATION CODE VERIFICATION");
        System.out.println("-------------------------------------------------------");
        System.out.print("ENTER REGISTRATION CODE (OR 'E' TO RETURN): ");
        String input = ValidationUtil.scanner.nextLine().trim();
        
        if (input.equalsIgnoreCase("E")) {
            if (confirmCancellation("registration")) {
                return false;
            }
            return performSignup(); // Restart if not confirmed
        }
        
        // Validate registration code
        int registrationCode;
        try {
            registrationCode = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("\n<<<INVALID INPUT! REGISTRATION CODE MUST BE A NUMBER!>>>");
            ConsoleUtil.systemPause();
            return performSignup(); // Retry
        }
        
        if (registrationCode != REGISTRATION_CODE) {
            System.out.println("\n<<<INCORRECT REGISTRATION CODE! REGISTRATION DENIED!>>>");
            ConsoleUtil.systemPause();
            return performSignup(); // Retry
        }
        
        // Step 2: Registration code verified, proceed to staff registration
        System.out.println("\n✓ REGISTRATION CODE VERIFIED!");
        System.out.println("PROCEEDING TO STAFF REGISTRATION...\n");
        ConsoleUtil.systemPause();
        
        // Step 3-6: Collect staff information
        Staff newStaff = collectStaffInformation();
        
        if (newStaff == null) {
            // User cancelled during information collection
            return false;
        }
        
        // Step 7: Show summary and confirm
        if (!confirmRegistrationDetails(newStaff)) {
            System.out.println("\nREGISTRATION CANCELLED BY USER.\n");
            ConsoleUtil.systemPause();
            return false;
        }
        
        // Step 8: Attempt to add staff
        System.out.println("\nSTEP 8/8: SAVING REGISTRATION...");
        System.out.println("-------------------------------------------------------");
        boolean success = staffService.addStaff(newStaff);
        
        if (success) {
            System.out.println("\n========================================");
            System.out.println("  ✓ REGISTRATION SUCCESSFUL!");
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
        System.out.println("(You can press 'E' at any time to cancel)\n");
        System.out.println("-------------------------------------------------------\n");
        
        // Step 2: Collect IC
        System.out.println("STEP 2/6: STAFF IC");
        System.out.println("-------------------------------------------------------");
        String ic = collectIC();
        if (ic == null) return null;
        System.out.println("✓ IC collected: " + ic + "\n");
        ConsoleUtil.systemPause();
        
        // Step 3: Collect Name
        ConsoleUtil.clearScreen();
        ConsoleUtil.logo();
        System.out.println("[ REGISTER STAFF ]");
        System.out.println("-------------------------------------------------------");
        System.out.println("STEP 3/6: STAFF NAME");
        System.out.println("-------------------------------------------------------");
        String name = collectName();
        if (name == null) return null;
        System.out.println("✓ Name collected: " + name + "\n");
        ConsoleUtil.systemPause();
        
        // Step 4: Collect Password (with strength indicator)
        ConsoleUtil.clearScreen();
        ConsoleUtil.logo();
        System.out.println("[ REGISTER STAFF ]");
        System.out.println("-------------------------------------------------------");
        System.out.println("STEP 4/6: PASSWORD");
        System.out.println("-------------------------------------------------------");
        String password = collectPassword();
        if (password == null) return null;
        System.out.println("✓ Password collected\n");
        ConsoleUtil.systemPause();
        
        // Step 5: Collect Age
        ConsoleUtil.clearScreen();
        ConsoleUtil.logo();
        System.out.println("[ REGISTER STAFF ]");
        System.out.println("-------------------------------------------------------");
        System.out.println("STEP 5/6: ADDITIONAL INFORMATION");
        System.out.println("-------------------------------------------------------");
        Integer age = collectAge();
        if (age == null) return null;
        
        // Step 6: Collect Salary
        Double salary = collectSalary();
        if (salary == null) return null;
        
        // Create staff object
        int staffId = Integer.parseInt(ic.substring(6));
        Staff staff = new Staff(name, ic, age, salary, password);
        staff.setId(staffId);
        
        return staff;
    }
    
    /**
     * Collects and validates IC number using MemberUtil.icValidation().
     */
    private String collectIC() {
        while (true) {
            System.out.print("ENTER NEW STAFF IC (12 digits, or 'E' to cancel): ");
            String input = ValidationUtil.scanner.nextLine().trim();
            
            if (input.equalsIgnoreCase("E")) {
                if (confirmCancellation("IC entry")) {
                    return null;
                }
                continue; // Continue if cancellation not confirmed
            }
            
            // Validate IC format using proper validation
            // Check basic format first
            if (!input.matches("\\d{12}")) {
                System.out.println("<<<INVALID INPUT! Please enter a 12-digit IC number!>>>\n");
                continue;
            }
            
            // Use MemberUtil validation for comprehensive check (date, place of birth, etc.)
            // Since MemberUtil.icValidation() reads from scanner, we'll validate manually
            try {
                int yy = Integer.parseInt(input.substring(0, 2));
                int mm = Integer.parseInt(input.substring(2, 4));
                int dd = Integer.parseInt(input.substring(4, 6));
                int pb = Integer.parseInt(input.substring(6, 8));
                
                // Place of birth: only 01-16 allowed
                if (pb < 1 || pb > 16) {
                    System.out.println("<<<INVALID PLACE OF BIRTH CODE (7th-8th digits): MUST BE 01-16 >>>\n");
                    continue;
                }
                
                // Validate date
                java.time.LocalDate.of(2000 + yy, mm, dd); // Try 20xx first
            } catch (Exception e) {
                System.out.println("<<<INVALID IC FORMAT! Please check date and place of birth codes!>>>\n");
                continue;
            }
            
            // Check if IC already exists
            if (staffService.getAllStaff().stream()
                    .anyMatch(s -> s.getStfIC().equals(input))) {
                System.out.println("<<<IC ALREADY EXISTS! Please use a different IC!>>>\n");
                continue;
            }
            
            return input;
        }
    }
    
    /**
     * Collects and validates staff name using MemberUtil.nameValidation().
     */
    private String collectName() {
        while (true) {
            System.out.print("ENTER NEW STAFF NAME (alphabets only, or 'E' to cancel): ");
            String name = ValidationUtil.scanner.nextLine().trim();
            
            if (name.equalsIgnoreCase("E")) {
                if (confirmCancellation("name entry")) {
                    return null;
                }
                continue; // Continue if cancellation not confirmed
            }
            
            if (name.trim().isEmpty()) {
                System.out.println("<<<NAME CANNOT BE EMPTY!>>>\n");
                continue;
            }
            
            // Use MemberUtil.nameValidation() for consistent validation
            if (MemberUtil.nameValidation(name)) {
                return name.toUpperCase();
            }
            // Error message already printed by nameValidation()
        }
    }
    
    /**
     * Collects and validates password with strength indicator.
     */
    private String collectPassword() {
        System.out.println("\nPASSWORD REQUIREMENTS:");
        System.out.println("• 8-16 characters");
        System.out.println("• Alphanumeric characters (a-z, A-Z, 0-9)");
        System.out.println("• For stronger passwords, include uppercase, lowercase, numbers, and special characters");
        System.out.println("-------------------------------------------------------\n");
        
        while (true) {
            System.out.print("ENTER NEW PASSWORD (or 'E' to cancel): ");
            String password = ValidationUtil.scanner.nextLine();
            
            if (password.equalsIgnoreCase("E")) {
                if (confirmCancellation("password entry")) {
                    return null;
                }
                continue; // Continue if cancellation not confirmed
            }
            
            // Show password strength immediately
            PasswordStrength strength = PasswordStrengthUtil.checkPasswordStrength(password);
            String strengthBar = PasswordStrengthUtil.getStrengthBar(strength);
            
            System.out.println("\nPASSWORD STRENGTH: " + strengthBar + " " + strength.getDisplayName().toUpperCase());
            
            // Show feedback for weak passwords
            if (strength.getLevel() <= 2) {
                System.out.println("\n⚠ WEAK PASSWORD DETECTED!");
                System.out.println("RECOMMENDATIONS:");
                System.out.print(PasswordStrengthUtil.getPasswordFeedback(password));
            }
            
            // Basic validation (still required)
            if (!password.matches("^[a-zA-Z0-9]{8,16}$")) {
                System.out.println("\n<<<INVALID INPUT! Password must be 8-16 alphanumeric characters!>>>");
                System.out.println("CURRENT ISSUES:");
                System.out.print(PasswordStrengthUtil.getPasswordFeedback(password));
                System.out.println();
                continue;
            }
            
            // Warn about weak passwords but allow them
            if (strength.getLevel() <= 2) {
                System.out.print("\n⚠ Your password is weak. Continue anyway? (Y/N): ");
                String choice = ValidationUtil.scanner.nextLine().trim();
                if (!choice.equalsIgnoreCase("Y") && !choice.equalsIgnoreCase("YES")) {
                    System.out.println("Please enter a stronger password.\n");
                    continue;
                }
            }
            
            System.out.print("\nCONFIRM PASSWORD: ");
            String confirmPassword = ValidationUtil.scanner.nextLine();
            
            if (!password.equals(confirmPassword)) {
                System.out.println("<<<PASSWORDS DO NOT MATCH! Please try again!>>>\n");
                continue;
            }
            
            // Show final strength after confirmation
            System.out.println("\n✓ Password confirmed!");
            System.out.println("FINAL PASSWORD STRENGTH: " + strengthBar + " " + strength.getDisplayName().toUpperCase());
            
            return password;
        }
    }
    
    /**
     * Collects and validates age.
     */
    private Integer collectAge() {
        while (true) {
            System.out.print("ENTER NEW STAFF AGE (18-54, or 'E' to cancel): ");
            String input = ValidationUtil.scanner.nextLine().trim();
            
            if (input.equalsIgnoreCase("E")) {
                if (confirmCancellation("age entry")) {
                    return null;
                }
                continue; // Continue if cancellation not confirmed
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
            System.out.print("ENTER NEW STAFF SALARY (must be > 0, or 'E' to cancel): ");
            String input = ValidationUtil.scanner.nextLine().trim();
            
            if (input.equalsIgnoreCase("E")) {
                if (confirmCancellation("salary entry")) {
                    return null;
                }
                continue; // Continue if cancellation not confirmed
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
    
    /**
     * Confirms cancellation with the user.
     * @param step The current step being cancelled
     * @return true if user confirms cancellation, false otherwise
     */
    private boolean confirmCancellation(String step) {
        System.out.print("\n⚠ Are you sure you want to cancel " + step + "? (Y/N): ");
        String choice = ValidationUtil.scanner.nextLine().trim();
        return choice.equalsIgnoreCase("Y") || choice.equalsIgnoreCase("YES");
    }
    
    /**
     * Displays registration summary and asks for final confirmation.
     * @param staff The staff object with collected information
     * @return true if user confirms, false if cancelled
     */
    private boolean confirmRegistrationDetails(Staff staff) {
        ConsoleUtil.clearScreen();
        ConsoleUtil.logo();
        System.out.println("[ REGISTRATION SUMMARY ]");
        System.out.println("-------------------------------------------------------");
        System.out.println("Please review your registration details:");
        System.out.println("-------------------------------------------------------\n");
        
        System.out.println("STAFF ID: S-" + staff.getId());
        System.out.println("NAME: " + staff.getName());
        System.out.println("IC: " + staff.getStfIC());
        System.out.println("AGE: " + staff.getStfAge());
        System.out.println("SALARY: RM " + String.format("%.2f", staff.getStfSalary()));
        System.out.println("\n-------------------------------------------------------");
        System.out.print("CONFIRM REGISTRATION? (Y/N): ");
        
        String choice = ValidationUtil.scanner.nextLine().trim();
        return choice.equalsIgnoreCase("Y") || choice.equalsIgnoreCase("YES");
    }
}



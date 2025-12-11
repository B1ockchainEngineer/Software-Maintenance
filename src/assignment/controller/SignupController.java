package assignment.controller;

import assignment.model.Staff;
import assignment.service.StaffService;
import assignment.util.ConsoleUtil;
import assignment.util.ValidationUtil;
import assignment.util.MemberUtil;
import assignment.util.PasswordUtil;
import assignment.util.config.SignupConfig;
import assignment.view.SignupView;

/**
 * Controller for handling staff signup/registration functionality.
 * Handles menu presentation and delegates work to StaffService for business logic.
 */
public class SignupController {

    private final StaffService staffService;
    private final SignupView signupView;

    public SignupController(StaffService staffService) {
        this.staffService = staffService;
        this.signupView = new SignupView();
    }

    /**
     * Displays the signup screen and handles staff registration.
     * @return true if registration was successful, false if cancelled
     */
    public boolean performSignup() {
        ConsoleUtil.clearScreen();
        ConsoleUtil.logo();
        signupView.printSignupHeader();

        // Step 1: Verify registration code
        signupView.printRegistrationCodeStep();
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
            signupView.printInvalidRegistrationCodeFormat();
            ConsoleUtil.systemPause();
            return performSignup(); // Retry
        }

        if (registrationCode != SignupConfig.REGISTRATION_CODE) {
            signupView.printIncorrectRegistrationCode();
            ConsoleUtil.systemPause();
            return performSignup(); // Retry
        }

        // Step 2: Registration code verified, proceed to staff registration
        signupView.printRegistrationCodeVerified();
        ConsoleUtil.systemPause();

        // Step 3-6: Collect staff information
        Staff newStaff = collectStaffInformation();

        if (newStaff == null) {
            // User cancelled during information collection
            return false;
        }

        // Step 7: Show summary and confirm
        if (!confirmRegistrationDetails(newStaff)) {
            signupView.printRegistrationCancelled();
            ConsoleUtil.systemPause();
            return false;
        }

        // Step 8: Attempt to add staff
        signupView.printSavingRegistration();
        boolean success = staffService.addStaff(newStaff);

        if (success) {
            signupView.printRegistrationSuccess(newStaff);
        } else {
            signupView.printRegistrationFailed();
        }

        ConsoleUtil.systemPause();
        return success;
    }

    /**
     * Collects staff information from the user.
     * @return Staff object with collected information, or null if user cancels
     */
    private Staff collectStaffInformation() {
        ConsoleUtil.clearScreen();
        ConsoleUtil.logo();
        signupView.printRegisterStaffHeader();

        // Step 2: Collect IC
        String ic = collectIC();
        if (ic == null) return null;
        signupView.printIcCollected(ic);
        ConsoleUtil.systemPause();

        // Step 3: Collect Name
        ConsoleUtil.clearScreen();
        ConsoleUtil.logo();
        signupView.printRegisterStaffHeader();
        signupView.printNameStep();
        String name = collectName();
        if (name == null) return null;
        signupView.printNameCollected(name);
        ConsoleUtil.systemPause();

        // Step 4: Collect Password (with strength indicator)
        ConsoleUtil.clearScreen();
        ConsoleUtil.logo();
        signupView.printRegisterStaffHeader();
        String password = collectPassword();
        if (password == null) return null;
        signupView.printPasswordCollected();
        ConsoleUtil.systemPause();

        // Step 5: Collect Age
        ConsoleUtil.clearScreen();
        ConsoleUtil.logo();
        signupView.printRegisterStaffHeader();
        Integer age = collectAge();
        if (age == null) return null;

        // Step 6: Collect Salary
        ConsoleUtil.clearScreen();
        ConsoleUtil.logo();
        signupView.printRegisterStaffHeader();
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
            signupView.printIcStep();
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
                signupView.printInvalidIcFormat();
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
                    signupView.printInvalidPlaceOfBirth();
                    continue;
                }

                // Validate date
                java.time.LocalDate.of(2000 + yy, mm, dd); // Try 20xx first
            } catch (Exception e) {
                signupView.printInvalidIcDate();
                continue;
            }

            // Check if IC already exists
            if (staffService.getAllStaff().stream()
                    .anyMatch(s -> s.getStfIC().equals(input))) {
                signupView.printIcAlreadyExists();
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
            signupView.printNameStep();
            String name = ValidationUtil.scanner.nextLine().trim();

            if (name.equalsIgnoreCase("E")) {
                if (confirmCancellation("name entry")) {
                    return null;
                }
                continue; // Continue if cancellation not confirmed
            }

            if (name.trim().isEmpty()) {
                signupView.printEmptyNameError();
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
     * Collects and validates password (minimum 8 characters only).
     */
    private String collectPassword() {
        signupView.printPasswordStep();

        while (true) {
            signupView.printPasswordStepPrompt();
            String password = PasswordUtil.readPassword("");

            if (password.equalsIgnoreCase("E")) {
                if (confirmCancellation("password entry")) {
                    return null;
                }
                continue; // Continue if cancellation not confirmed
            }

            // Check for empty password first
            if (password == null || password.trim().isEmpty()) {
                signupView.printEmptyPasswordError();
                continue; // Ask user to enter again
            }

            // Only check minimum length (8 characters)
            if (password.length() < 8) {
                signupView.printInvalidPasswordFormat();
                continue;
            }

            signupView.printPasswordConfirmPrompt();
            String confirmPassword = PasswordUtil.readPassword("");

            if (!password.equals(confirmPassword)) {
                signupView.printPasswordMismatch();
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
            signupView.printAgeStep();
            String input = ValidationUtil.scanner.nextLine().trim();

            if (input.equalsIgnoreCase("E")) {
                if (confirmCancellation("age entry")) {
                    return null;
                }
                continue; // Continue if cancellation not confirmed
            }

            try {
                int age = Integer.parseInt(input);
                if (age < 0) {
                    System.out.println(SignupConfig.ErrorMessage.AGE_CANNOT_BE_NEGATIVE);
                    System.out.println();
                    continue;
                }
                if (age >= 18 && age <= 54) {
                    return age;
                } else {
                    System.out.println(SignupConfig.ErrorMessage.INVALID_AGE);
                    System.out.println();
                }
            } catch (NumberFormatException e) {
                System.out.println(SignupConfig.ErrorMessage.INVALID_NUMBER);
                System.out.println();
            }
        }
    }

    /**
     * Collects and validates salary.
     */
    private Double collectSalary() {
        while (true) {
            signupView.printSalaryStep();
            String input = ValidationUtil.scanner.nextLine().trim();

            if (input.equalsIgnoreCase("E")) {
                if (confirmCancellation("salary entry")) {
                    return null;
                }
                continue; // Continue if cancellation not confirmed
            }

            try {
                double salary = Double.parseDouble(input);
                if (salary < 0) {
                    System.out.println(SignupConfig.ErrorMessage.SALARY_CANNOT_BE_NEGATIVE);
                    System.out.println();
                    continue;
                }
                if (salary > 0) {
                    return salary;
                } else {
                    System.out.println(SignupConfig.ErrorMessage.INVALID_SALARY);
                    System.out.println();
                }
            } catch (NumberFormatException e) {
                System.out.println(SignupConfig.ErrorMessage.INVALID_NUMBER);
                System.out.println();
            }
        }
    }

    /**
     * Confirms cancellation with the user.
     * @param step The current step being cancelled
     * @return true if user confirms cancellation, false otherwise
     */
    private boolean confirmCancellation(String step) {
        signupView.printCancelConfirmationPrompt(step);
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
        signupView.printRegistrationSummary(staff);

        String choice = ValidationUtil.scanner.nextLine().trim();
        return choice.equalsIgnoreCase("Y") || choice.equalsIgnoreCase("YES");
    }
}



package assignment.controller;

import assignment.enums.StaffMenu;
import assignment.model.Staff;
import assignment.service.StaffService;
import assignment.util.ConsoleUtil;
import assignment.util.ValidationUtil;
import assignment.view.StaffView;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for staff-related flows.
 * Handles staff menu and delegates to StaffService for business logic.
 */
public class StaffController {

    private final StaffService staffService;
    private final StaffView staffView;

    public StaffController(StaffService staffService) {
        this.staffService = staffService;
        this.staffView = new StaffView();
    }

    public void manageStaff() {
        while (true) {
            ConsoleUtil.clearScreen();
            ConsoleUtil.logo();
            staffView.printStaffMenu(staffService.getAllStaff().size());
            System.out.print("ENTER YOUR SELECTION: ");

            int staffOpt = ValidationUtil.intValidation(0, 5);

            if (staffOpt == -9999) {
                ConsoleUtil.systemPause();
                continue;
            }

            StaffMenu selection = StaffMenu.getByOption(staffOpt);
            if (selection == null) {
                System.out.println("<<<INVALID OPTION!>>>");
                ConsoleUtil.systemPause();
                continue;
            }

            switch (selection) {
                case ADD_STAFF -> {
                    addStaff();
                }
                case UPDATE_STAFF -> {
                    updateStaff();
                }
                case DELETE_STAFF -> {
                    deleteStaff();
                }
                case SEARCH_STAFF -> {
                    searchStaff();
                }
                case VIEW_STAFF_LIST -> {
                    viewStaffList();
                }
                case BACK_TO_MAIN -> {
                    System.out.println("\nRETURNING TO MAIN MENU...");
                    ConsoleUtil.systemPause();
                    return;
                }
            }
        }
    }

    /**
     * Handles adding a new staff member.
     */
    private void addStaff() {
        ConsoleUtil.clearScreen();
        ConsoleUtil.logo();
        staffView.printAddStaffHeader(staffService.getAllStaff().size());

        String ic = collectIC();
        if (ic == null) return;

        String name = collectName();
        if (name == null) return;

        String password = collectPassword();
        if (password == null) return;

        Integer age = collectAge();
        if (age == null) return;

        Double salary = collectSalary();
        if (salary == null) return;

        // Create staff object
        int staffId = Integer.parseInt(ic.substring(6));
        Staff newStaff = new Staff(name, ic, age, salary, password);
        newStaff.setId(staffId);

        // Show summary before adding
        staffView.printNewStaffSummary(newStaff);
        System.out.print("CONFIRM ADD STAFF? (Y/N): ");
        String confirm = ValidationUtil.scanner.nextLine().trim();

        if (!confirm.equalsIgnoreCase("Y")) {
            System.out.println("\n<<<STAFF ADDITION CANCELLED!>>>\n");
            return;
        }

        // Attempt to add staff
        boolean success = staffService.addStaff(newStaff);

        if (success) {
            staffView.printStaffAddedSuccess(newStaff, staffService.getAllStaff().size());
            ConsoleUtil.systemPause();
        } else {
            System.out.println();
            System.out.println("<<<FAILED TO ADD STAFF!>>>");
            System.out.println("Reason: IC already exists in the system.");
            System.out.println("Please use a different IC number.");
            System.out.println();
            ConsoleUtil.systemPause();
        }
    }

    /**
     * Handles updating staff information.
     */
    private void updateStaff() {
        ConsoleUtil.clearScreen();
        ConsoleUtil.logo();
        staffView.printUpdateStaffMenu();
        System.out.print("ENTER YOUR CHOICE (OR 'E' TO CANCEL): ");

        String choice = ValidationUtil.scanner.nextLine().trim();

        if (choice.equalsIgnoreCase("E")) {
            return;
        }

        Staff staffToUpdate = null;

        if (choice.equals("1")) {
            System.out.print("ENTER STAFF ID TO UPDATE (OR '0' TO CANCEL): S-");
            int staffId = ValidationUtil.intValidation(0, Integer.MAX_VALUE);

            if (staffId == -9999 || staffId == 0) {
                return;
            }

            staffToUpdate = staffService.findById(staffId);

        } else if (choice.equals("2")) {
            System.out.print("ENTER STAFF IC TO UPDATE (OR 'E' TO CANCEL): ");
            String ic = ValidationUtil.scanner.nextLine().trim();

            if (ic.equalsIgnoreCase("E")) {
                return;
            }

            if (!ic.matches("\\d{12}")) {
                System.out.println("\n<<<INVALID IC FORMAT! IC must be 12 digits!>>>\n");
                return;
            }

            staffToUpdate = staffService.findByIc(ic);

        } else {
            System.out.println("\n<<<INVALID CHOICE! Please enter 1 or 2!>>>\n");
            return;
        }

        if (staffToUpdate == null) {
            System.out.println();
            System.out.println("<<<STAFF NOT FOUND!>>>");
            System.out.println();
            System.out.println("TIP: You can:");
            System.out.println("  - View the staff list to verify the ID/IC");
            System.out.println("  - Use the search function to find the staff");
            System.out.println("  - Check for typos in the ID/IC");
            System.out.println();
            return;
        }

        // Display current information
        ConsoleUtil.clearScreen();
        ConsoleUtil.logo();
        System.out.println("[ UPDATE STAFF INFORMATION ]");
        System.out.println("-------------------------------------------------------");
        System.out.println("CURRENT STAFF INFORMATION:");
        System.out.println("-------------------------------------------------------");
        staffView.displayStaffDetails(staffToUpdate);
        System.out.println("-------------------------------------------------------");
        System.out.println();
        System.out.println("INSTRUCTIONS:");
        System.out.println("  - Press Enter to keep current value");
        System.out.println("  - Enter new value to update");
        System.out.println("  - Press 'E' to cancel");
        System.out.println();
        System.out.println("-------------------------------------------------------");
        System.out.println();

        // Update Name
        System.out.print("ENTER NEW NAME [" + staffToUpdate.getName() + "]: ");
        String newName = ValidationUtil.scanner.nextLine().trim();
        if (newName.equalsIgnoreCase("E")) {
            System.out.println("\n<<<UPDATE CANCELLED!>>>\n");
            return;
        }
        if (!newName.isEmpty()) {
            if (newName.matches("^[a-zA-Z ]+$") && newName.length() >= 2 && newName.length() <= 50) {
                staffToUpdate.setStfName(newName);
                System.out.println("  -> Name updated successfully!");
            } else {
                System.out.println("  -> Invalid name format! Keeping current value.");
                System.out.println("     (Name must be 2-50 characters, letters and spaces only)");
            }
        } else {
            System.out.println("  -> Keeping current name.");
        }
        System.out.println();

        // Update Password
        System.out.print("ENTER NEW PASSWORD (Press Enter to skip): ");
        String newPassword = ValidationUtil.scanner.nextLine();
        if (newPassword.equalsIgnoreCase("E")) {
            System.out.println("\n<<<UPDATE CANCELLED!>>>\n");
            return;
        }
        if (!newPassword.isEmpty()) {
            if (newPassword.matches("^[a-zA-Z0-9]{8,16}$")) {
                System.out.print("CONFIRM PASSWORD: ");
                String confirmPassword = ValidationUtil.scanner.nextLine();
                if (newPassword.equals(confirmPassword)) {
                    staffToUpdate.setStfPassword(newPassword);
                    System.out.println("  -> Password updated successfully!");
                } else {
                    System.out.println("  -> Passwords do not match! Keeping current password.");
                }
            } else {
                System.out.println("  -> Invalid password format! Keeping current password.");
                System.out.println("     (Password must be 8-16 alphanumeric characters)");
            }
        } else {
            System.out.println("  -> Keeping current password.");
        }
        System.out.println();

        // Update Age
        System.out.print("ENTER NEW AGE [" + staffToUpdate.getStfAge() + "]: ");
        String ageInput = ValidationUtil.scanner.nextLine().trim();
        if (ageInput.equalsIgnoreCase("E")) {
            System.out.println("\n<<<UPDATE CANCELLED!>>>\n");
            return;
        }
        if (!ageInput.isEmpty()) {
            try {
                int newAge = Integer.parseInt(ageInput);
                if (newAge >= 18 && newAge <= 54) {
                    staffToUpdate.setStfAge(newAge);
                    System.out.println("  -> Age updated successfully!");
                } else {
                    System.out.println("  -> Invalid age! Age must be between 18-54. Keeping current value.");
                }
            } catch (NumberFormatException e) {
                System.out.println("  -> Invalid input! Please enter a number. Keeping current value.");
            }
        } else {
            System.out.println("  -> Keeping current age.");
        }
        System.out.println();

        // Update Salary
        System.out.print("ENTER NEW SALARY [RM " + String.format("%.2f", staffToUpdate.getStfSalary()) + "]: RM ");
        String salaryInput = ValidationUtil.scanner.nextLine().trim();
        if (salaryInput.equalsIgnoreCase("E")) {
            System.out.println("\n<<<UPDATE CANCELLED!>>>\n");
            return;
        }
        if (!salaryInput.isEmpty()) {
            try {
                double newSalary = Double.parseDouble(salaryInput);
                if (newSalary > 0) {
                    if (newSalary > 1000000) {
                        System.out.print("  -> Warning: Salary seems unusually high. Continue? (Y/N): ");
                        String confirm = ValidationUtil.scanner.nextLine().trim();
                        if (!confirm.equalsIgnoreCase("Y")) {
                            System.out.println("  -> Keeping current salary.");
                        } else {
                            staffToUpdate.setStfSalary(newSalary);
                            System.out.println("  -> Salary updated successfully!");
                        }
                    } else {
                        staffToUpdate.setStfSalary(newSalary);
                        System.out.println("  -> Salary updated successfully!");
                    }
                } else {
                    System.out.println("  -> Invalid salary! Salary must be greater than 0. Keeping current value.");
                }
            } catch (NumberFormatException e) {
                System.out.println("  -> Invalid input! Please enter a valid number. Keeping current value.");
            }
        } else {
            System.out.println("  -> Keeping current salary.");
        }
        System.out.println();

        // Confirm update
        System.out.println();
        System.out.println("-------------------------------------------------------");
        System.out.println("UPDATED STAFF INFORMATION:");
        System.out.println("-------------------------------------------------------");
        staffView.displayStaffDetails(staffToUpdate);
        System.out.println("-------------------------------------------------------");
        System.out.print("CONFIRM UPDATE? (Y/N): ");
        String confirm = ValidationUtil.scanner.nextLine().trim();

        if (confirm.equalsIgnoreCase("Y")) {
            boolean success = staffService.updateStaff(staffToUpdate);
            if (success) {
                System.out.println();
                System.out.println("========================================");
                System.out.println("  STAFF UPDATED SUCCESSFULLY!");
                System.out.println("========================================");
                System.out.println();
            } else {
                System.out.println();
                System.out.println("<<<FAILED TO UPDATE STAFF!>>>");
                System.out.println("Possible reasons:");
                System.out.println("  - Staff record not found");
                System.out.println("  - New IC already exists (if IC was changed)");
                System.out.println("Please try again.");
                System.out.println();
            }
        } else {
            System.out.println();
            System.out.println("<<<UPDATE CANCELLED!>>>");
            System.out.println("No changes were made.");
            System.out.println();
        }

        ConsoleUtil.systemPause();
    }

    /**
     * Handles deleting a staff member.
     * Allows deletion by ID or IC with confirmation.
     */
    private void deleteStaff() {
        ConsoleUtil.clearScreen();
        ConsoleUtil.logo();

        List<Staff> staffList = staffService.getAllStaff();

        if (staffList.isEmpty()) {
            System.out.println("[ DELETE STAFF ]");
            System.out.println("-------------------------------------------------------");
            System.out.println("WARNING: This action cannot be undone!");
            System.out.println("-------------------------------------------------------\n");
            System.out.println("THERE IS NO STAFF TO DELETE...");
            System.out.println();
            System.out.println("TIP: Use 'Add New Staff' option to register staff members.");
            System.out.println();
            ConsoleUtil.systemPause();
            return;
        }

        staffView.printDeleteStaffMenu(staffList);
        System.out.print("ENTER YOUR CHOICE (OR 'E' TO CANCEL): ");

        String choice = ValidationUtil.scanner.nextLine().trim();

        if (choice.equalsIgnoreCase("E")) {
            return;
        }

        Staff staffToDelete = null;
        String identifier = "";

        if (choice.equals("1")) {
            // Delete by ID
            System.out.print("ENTER STAFF ID TO DELETE (OR '0' TO CANCEL): S-");
            int staffId = ValidationUtil.intValidation(0, Integer.MAX_VALUE);

            if (staffId == -9999) {
                System.out.println("\n<<<INVALID INPUT!>>>\n");
                return;
            }

            if (staffId == 0) {
                return;
            }

            staffToDelete = staffService.findById(staffId);
            identifier = "ID S-" + staffId;

        } else if (choice.equals("2")) {
            // Delete by IC
            System.out.print("ENTER STAFF IC TO DELETE (OR 'E' TO CANCEL): ");
            String icToDelete = ValidationUtil.scanner.nextLine().trim();

            if (icToDelete.equalsIgnoreCase("E")) {
                return;
            }

            if (!icToDelete.matches("\\d{12}")) {
                System.out.println("\n<<<INVALID IC FORMAT! IC must be 12 digits!>>>\n");
                return;
            }

            staffToDelete = staffService.findByIc(icToDelete);
            identifier = "IC " + icToDelete;

        } else {
            System.out.println("\n<<<INVALID CHOICE! Please enter 1 or 2!>>>\n");
            return;
        }

        if (staffToDelete == null) {
            System.out.println();
            System.out.println("<<<STAFF WITH " + identifier + " NOT FOUND!>>>");
            System.out.println();
            System.out.println("TIP: You can:");
            System.out.println("  - View the staff list to verify the ID/IC");
            System.out.println("  - Use the search function to find the staff");
            System.out.println("  - Check for typos in the ID/IC");
            System.out.println();
            return;
        }

        // Display staff information before deletion
        ConsoleUtil.clearScreen();
        ConsoleUtil.logo();
        staffView.printDeleteConfirmation(staffToDelete);
        System.out.print("ARE YOU SURE YOU WANT TO DELETE THIS STAFF? (Y/N): ");
        String confirm = ValidationUtil.scanner.nextLine().trim();

        if (!confirm.equalsIgnoreCase("Y")) {
            System.out.println();
            System.out.println("<<<DELETION CANCELLED!>>>");
            System.out.println("Staff record is safe.");
            System.out.println();
            return;
        }

        // Perform deletion
        boolean deleted = false;
        if (identifier.startsWith("ID")) {
            deleted = staffService.deleteById(staffToDelete.getId());
        } else {
            deleted = staffService.deleteByIc(staffToDelete.getIc());
        }

        if (deleted) {
            System.out.println();
            System.out.println("========================================");
            System.out.println("  STAFF DELETED SUCCESSFULLY!");
            System.out.println("========================================");
            System.out.println("Staff with " + identifier + " has been removed from the system.");
            System.out.println("Remaining staff count: " + staffService.getAllStaff().size());
            System.out.println("========================================");
            System.out.println();
        } else {
            System.out.println();
            System.out.println("<<<FAILED TO DELETE STAFF!>>>");
            System.out.println("Please try again or contact system administrator.");
            System.out.println();
        }

        ConsoleUtil.systemPause();
    }

    /**
     * Handles searching for staff members.
     * Allows searching by ID, IC, or Name.
     */
    private void searchStaff() {
        ConsoleUtil.clearScreen();
        ConsoleUtil.logo();
        staffView.printSearchStaffMenu(staffService.getAllStaff());
        System.out.print("ENTER YOUR CHOICE (OR 'E' TO CANCEL): ");

        String choice = ValidationUtil.scanner.nextLine().trim();

        if (choice.equalsIgnoreCase("E")) {
            return;
        }

        List<Staff> results = new ArrayList<>();
        String searchType = "";

        if (choice.equals("1")) {
            // Search by ID
            System.out.print("ENTER STAFF ID TO SEARCH (OR '0' TO CANCEL): S-");
            int staffId = ValidationUtil.intValidation(0, Integer.MAX_VALUE);

            if (staffId == -9999) {
                System.out.println("\n<<<INVALID INPUT!>>>\n");
                return;
            }

            if (staffId == 0) {
                return;
            }

            Staff staff = staffService.findById(staffId);
            if (staff != null) {
                results.add(staff);
            }
            searchType = "ID S-" + staffId;

        } else if (choice.equals("2")) {
            // Search by IC with retry mechanism
            String ic = "";
            boolean validIc = false;

            while (!validIc) {
                System.out.print("ENTER STAFF IC TO SEARCH (OR 'E' TO CANCEL): ");
                ic = ValidationUtil.scanner.nextLine().trim();

                if (ic.equalsIgnoreCase("E")) {
                    return;
                }

                if (!ic.matches("\\d{12}")) {
                    System.out.println("\n<<<INVALID IC FORMAT! IC must be 12 digits!>>>\n");
                    System.out.print("DO YOU WANT TO SEARCH AGAIN? (Y/N): ");
                    String retry = ValidationUtil.scanner.nextLine().trim();

                    if (!retry.equalsIgnoreCase("Y")) {
                        return; // Return to menu if user doesn't want to retry
                    }
                    System.out.println(); // Add spacing before retry
                } else {
                    validIc = true;
                }
            }

            Staff staff = staffService.findByIc(ic);
            if (staff != null) {
                results.add(staff);
            }
            searchType = "IC " + ic;

        } else if (choice.equals("3")) {
            // Search by Name
            System.out.print("ENTER STAFF NAME TO SEARCH (OR 'E' TO CANCEL): ");
            String name = ValidationUtil.scanner.nextLine().trim();

            if (name.equalsIgnoreCase("E")) {
                return;
            }

            if (name.isEmpty()) {
                System.out.println("\n<<<NAME CANNOT BE EMPTY!>>>\n");
                return;
            }

            results = staffService.findByName(name);
            searchType = "NAME '" + name + "'";

        } else {
            System.out.println("\n<<<INVALID CHOICE! Please enter 1, 2, or 3!>>>\n");
            return;
        }

        // Display results
        ConsoleUtil.clearScreen();
        ConsoleUtil.logo();
        staffView.displaySearchResults(results, searchType);

        ConsoleUtil.systemPause();
    }

    /**
     * Displays all staff members with statistics.
     */
    private void viewStaffList() {
        ConsoleUtil.clearScreen();
        ConsoleUtil.logo();
        staffView.displayStaffList(staffService.getAllStaff());
        ConsoleUtil.systemPause();
    }

    /**
     * Collects and validates IC number.
     */
    private String collectIC() {
        while (true) {
            System.out.print("ENTER STAFF IC (12 digits, e.g., 123456789012): ");
            String ic = ValidationUtil.scanner.nextLine().trim();

            if (ic.equalsIgnoreCase("E")) {
                System.out.println("  -> Operation cancelled.");
                return null;
            }

            if (ic.isEmpty()) {
                System.out.println("  -> ERROR: IC cannot be empty. Please enter a 12-digit IC number.");
                System.out.println();
                continue;
            }

            if (!ic.matches("\\d{12}")) {
                System.out.println("  -> ERROR: Invalid format! IC must be exactly 12 digits.");
                System.out.println("     Example: 123456789012");
                System.out.println();
                continue;
            }

            if (staffService.getAllStaff().stream()
                    .anyMatch(s -> s.getStfIC().equals(ic))) {
                System.out.println("  -> ERROR: This IC already exists in the system!");
                System.out.println("     Please use a different IC number.");
                System.out.println();
                continue;
            }

            System.out.println("  -> IC validated successfully!");
            System.out.println();
            return ic;
        }
    }

    /**
     * Collects and validates staff name.
     */
    private String collectName() {
        while (true) {
            System.out.print("ENTER STAFF NAME (alphabets and spaces only, e.g., John Doe): ");
            String name = ValidationUtil.scanner.nextLine().trim();

            if (name.equalsIgnoreCase("E")) {
                System.out.println("  -> Operation cancelled.");
                return null;
            }

            if (name.isEmpty()) {
                System.out.println("  -> ERROR: Name cannot be empty. Please enter a valid name.");
                System.out.println();
                continue;
            }

            if (!name.matches("^[a-zA-Z ]+$")) {
                System.out.println("  -> ERROR: Invalid characters! Name should contain only:");
                System.out.println("     - Letters (A-Z, a-z)");
                System.out.println("     - Spaces");
                System.out.println("     Example: John Smith, Ahmad Bin Ali");
                System.out.println();
                continue;
            }

            if (name.length() < 2) {
                System.out.println("  -> ERROR: Name is too short. Please enter at least 2 characters.");
                System.out.println();
                continue;
            }

            if (name.length() > 50) {
                System.out.println("  -> ERROR: Name is too long. Maximum 50 characters allowed.");
                System.out.println();
                continue;
            }

            System.out.println("  -> Name validated successfully!");
            System.out.println();
            return name.toUpperCase();
        }
    }

    /**
     * Collects and validates password.
     */
    private String collectPassword() {
        while (true) {
            System.out.print("ENTER PASSWORD (8-16 alphanumeric characters, e.g., Staff123): ");
            String password = ValidationUtil.scanner.nextLine();

            if (password.equalsIgnoreCase("E")) {
                System.out.println("  -> Operation cancelled.");
                return null;
            }

            if (password.isEmpty()) {
                System.out.println("  -> ERROR: Password cannot be empty.");
                System.out.println();
                continue;
            }

            if (password.length() < 8) {
                System.out.println("  -> ERROR: Password too short! Minimum 8 characters required.");
                System.out.println("     Current length: " + password.length() + " characters");
                System.out.println();
                continue;
            }

            if (password.length() > 16) {
                System.out.println("  -> ERROR: Password too long! Maximum 16 characters allowed.");
                System.out.println("     Current length: " + password.length() + " characters");
                System.out.println();
                continue;
            }

            if (!password.matches("^[a-zA-Z0-9]+$")) {
                System.out.println("  -> ERROR: Password can only contain letters and numbers.");
                System.out.println("     Special characters and spaces are not allowed.");
                System.out.println();
                continue;
            }

            // Password strength indicator
            boolean hasLetter = password.matches(".*[a-zA-Z].*");
            boolean hasNumber = password.matches(".*[0-9].*");
            if (hasLetter && hasNumber) {
                System.out.println("  -> Password strength: Strong (contains letters and numbers)");
            } else {
                System.out.println("  -> Password strength: Weak (recommended: use letters + numbers)");
            }

            System.out.print("CONFIRM PASSWORD: ");
            String confirmPassword = ValidationUtil.scanner.nextLine();

            if (!password.equals(confirmPassword)) {
                System.out.println("  -> ERROR: Passwords do not match! Please try again.");
                System.out.println();
                continue;
            }

            System.out.println("  -> Password confirmed successfully!");
            System.out.println();
            return password;
        }
    }

    /**
     * Collects and validates age.
     */
    private Integer collectAge() {
        while (true) {
            System.out.print("ENTER STAFF AGE (18-54 years, e.g., 25): ");
            String input = ValidationUtil.scanner.nextLine().trim();

            if (input.equalsIgnoreCase("E")) {
                System.out.println("  -> Operation cancelled.");
                return null;
            }

            if (input.isEmpty()) {
                System.out.println("  -> ERROR: Age cannot be empty. Please enter a number.");
                System.out.println();
                continue;
            }

            try {
                int age = Integer.parseInt(input);
                if (age < 18) {
                    System.out.println("  -> ERROR: Age too young! Minimum age is 18 years.");
                    System.out.println();
                    continue;
                }
                if (age > 54) {
                    System.out.println("  -> ERROR: Age too old! Maximum age is 54 years.");
                    System.out.println();
                    continue;
                }
                System.out.println("  -> Age validated successfully!");
                System.out.println();
                return age;
            } catch (NumberFormatException e) {
                System.out.println("  -> ERROR: Invalid input! Please enter a valid number (e.g., 25).");
                System.out.println();
            }
        }
    }

    /**
     * Collects and validates salary.
     */
    private Double collectSalary() {
        while (true) {
            System.out.print("ENTER STAFF SALARY (must be > 0, e.g., 2500.00): RM ");
            String input = ValidationUtil.scanner.nextLine().trim();

            if (input.equalsIgnoreCase("E")) {
                System.out.println("  -> Operation cancelled.");
                return null;
            }

            if (input.isEmpty()) {
                System.out.println("  -> ERROR: Salary cannot be empty. Please enter a number.");
                System.out.println();
                continue;
            }

            try {
                double salary = Double.parseDouble(input);
                if (salary <= 0) {
                    System.out.println("  -> ERROR: Salary must be greater than 0!");
                    System.out.println("     Please enter a positive amount (e.g., 2500.00)");
                    System.out.println();
                    continue;
                }
                if (salary > 1000000) {
                    System.out.print("  -> WARNING: Salary seems unusually high (>RM 1,000,000). Are you sure? (Y/N): ");
                    String confirm = ValidationUtil.scanner.nextLine().trim();
                    if (!confirm.equalsIgnoreCase("Y")) {
                        System.out.println("  -> Please re-enter the salary.");
                        System.out.println();
                        continue;
                    }
                }
                System.out.println("  -> Salary validated successfully!");
                System.out.println();
                return salary;
            } catch (NumberFormatException e) {
                System.out.println("  -> ERROR: Invalid input! Please enter a valid number (e.g., 2500.00).");
                System.out.println();
            }
        }
    }

}

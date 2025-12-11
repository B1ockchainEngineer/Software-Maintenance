package assignment.controller;

import assignment.enums.StaffMenu;
import assignment.model.Staff;
import assignment.service.StaffService;
import assignment.util.ConsoleUtil;
import assignment.util.ValidationUtil;
import assignment.util.PasswordUtil;
import assignment.util.config.StaffConfig;
import assignment.view.StaffView;

import assignment.util.SalesUtil;
import assignment.util.config.AppConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Controller for staff-related flows.
 * Handles staff menu and delegates to StaffService for business logic.
 */
public class StaffController {
    private static final Logger LOGGER = Logger.getLogger(StaffController.class.getName());
    
    private final StaffService staffService;
    private final StaffView staffView;

    public StaffController(StaffService staffService) {
        this.staffService = staffService;
        this.staffView = new StaffView();
    }

    public void manageStaff(Staff currentStaff) {
        while (true) {
            ConsoleUtil.clearScreen();
            ConsoleUtil.logo();
            staffView.printStaffMenu(staffService.getAllStaff().size());
            System.out.print(StaffConfig.PROMPT_ENTER_SELECTION);

            int staffOpt = ValidationUtil.intValidation(0, 5);

            if (staffOpt == SalesUtil.INVALID_INPUT) {
                ConsoleUtil.systemPause();
                continue;
            }

            StaffMenu selection = StaffMenu.getByOption(staffOpt);
            if (selection == null) {
                LOGGER.warning(StaffConfig.ErrorMessage.INVALID_OPTION);
                System.out.println(StaffConfig.ErrorMessage.INVALID_OPTION);
                ConsoleUtil.systemPause();
                continue;
            }

            switch (selection) {
                case ADD_STAFF:
                    addStaff();
                    break;
                case UPDATE_STAFF:
                    updateStaff();
                    break;
                case DELETE_STAFF:
                    deleteStaff(currentStaff);
                    break;
                case SEARCH_STAFF:
                    searchStaff();
                    break;
                case VIEW_STAFF_LIST:
                    viewStaffList();
                    break;
                case BACK_TO_MAIN:
                    System.out.println();
                    System.out.println(StaffConfig.MSG_RETURNING_TO_MAIN);
                    ConsoleUtil.systemPause();
                    return;
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
        System.out.print(StaffConfig.PROMPT_CONFIRM_ADD_STAFF);
        String confirm = ValidationUtil.scanner.nextLine().trim();

        if (!confirm.equalsIgnoreCase("Y")) {
            System.out.println();
            System.out.println(StaffConfig.ErrorMessage.STAFF_ADDITION_CANCELLED);
            System.out.println();
            return;
        }

        // Attempt to add staff
        boolean success = staffService.addStaff(newStaff);

        if (success) {
            staffView.printStaffAddedSuccess(newStaff, staffService.getAllStaff().size());
            ConsoleUtil.systemPause();
        } else {
            System.out.println();
            LOGGER.warning(StaffConfig.ErrorMessage.FAILED_TO_ADD_STAFF + " - " + StaffConfig.ErrorMessage.REASON_IC_EXISTS);
            System.out.println(StaffConfig.ErrorMessage.FAILED_TO_ADD_STAFF);
            System.out.println(StaffConfig.ErrorMessage.REASON_IC_EXISTS);
            System.out.println(StaffConfig.ErrorMessage.USE_DIFFERENT_IC);
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
        
        String choice;
        while (true) {
            staffView.printUpdateStaffMenu();
            System.out.print("ENTER YOUR CHOICE (OR 'E' TO CANCEL): ");

            choice = ValidationUtil.scanner.nextLine().trim();

            if (choice.equalsIgnoreCase("E")) {
                return;
            }

            if (!choice.equals("1") && !choice.equals("2")) {
                LOGGER.warning("INVALID CHOICE! Please enter 1 or 2!");
                System.out.println("\n<<<INVALID CHOICE! Please enter 1 or 2!>>>\n");
                System.out.print("PRESS 'E' TO EXIT OR ENTER TO TRY AGAIN: ");
                String retry = ValidationUtil.scanner.nextLine().trim();
                if (retry.equalsIgnoreCase("E")) {
                    return;
                }
                ConsoleUtil.clearScreen();
                ConsoleUtil.logo();
                continue; // Try again
            }
            break; // Valid choice, exit loop
        }

        Staff staffToUpdate = null;

        if (choice.equals("1")) {
            while (true) {
                System.out.print("ENTER STAFF NAME TO UPDATE (OR 'E' TO CANCEL): ");
                String name = ValidationUtil.scanner.nextLine().trim();

                if (name.equalsIgnoreCase("E")) {
                    return;
                }

                if (name.trim().isEmpty()) {
                    System.out.println(StaffConfig.ErrorMessage.NAME_CANNOT_BE_EMPTY);
                    System.out.println();
                    continue; // Ask again
                }

                List<Staff> results = staffService.findByName(name);
                if (results.isEmpty()) {
                    System.out.println();
                    LOGGER.warning("STAFF NOT FOUND!");
                    System.out.println("<<<STAFF NOT FOUND!>>>");
                    System.out.println();
                    System.out.println("TIP: You can:");
                    System.out.println("  - View the staff list to verify the name");
                    System.out.println("  - Use the search function to find the staff");
                    System.out.println("  - Check for typos in the name");
                    System.out.println();
                    System.out.print("PRESS 'E' TO EXIT OR ENTER TO TRY AGAIN: ");
                    String retry = ValidationUtil.scanner.nextLine().trim();
                    if (retry.equalsIgnoreCase("E")) {
                        return;
                    }
                    continue; // Ask again
                } else if (results.size() == 1) {
                    staffToUpdate = results.get(0);
                    break; // Exit loop, staff found
                } else {
                    // Multiple staff found with same name
                    System.out.println();
                    System.out.println("Multiple staff found with name containing \"" + name + "\":");
                    System.out.println(AppConfig.SEPARATOR_LINE);
                    int index = 1;
                    for (Staff s : results) {
                        System.out.println("[" + index + "]");
                        System.out.println(s.toString());
                        if (index < results.size()) {
                            System.out.println(AppConfig.SEPARATOR_LINE);
                        }
                        index++;
                    }
                    System.out.println();
                    System.out.print("ENTER THE NUMBER OF THE STAFF TO UPDATE (OR '0' TO CANCEL): ");
                    int selection = ValidationUtil.intValidation(0, results.size());
                    if (selection == SalesUtil.INVALID_INPUT || selection == 0 || selection < 1 || selection > results.size()) {
                        return;
                    }
                    staffToUpdate = results.get(selection - 1);
                    break; // Exit loop, staff selected
                }
            }

        } else if (choice.equals("2")) {
            while (true) {
                System.out.print("ENTER STAFF IC TO UPDATE (OR 'E' TO CANCEL): ");
                String ic = ValidationUtil.scanner.nextLine().trim();

                if (ic.equalsIgnoreCase("E")) {
                    return;
                }

                if (ic.trim().isEmpty()) {
                    LOGGER.warning(StaffConfig.ErrorMessage.IC_CANNOT_BE_EMPTY);
                    System.out.println(StaffConfig.ErrorMessage.IC_CANNOT_BE_EMPTY);
                    System.out.println();
                    continue; // Ask again
                }

                if (!ic.matches("\\d{12}")) {
                    LOGGER.warning("INVALID IC FORMAT! IC must be 12 digits! - IC: " + ic);
                    LOGGER.warning("INVALID IC FORMAT! IC must be 12 digits!");
                    System.out.println("\n<<<INVALID IC FORMAT! IC must be 12 digits!>>>\n");
                    continue; // Ask again
                }

                staffToUpdate = staffService.findByIc(ic);
                if (staffToUpdate == null) {
                    System.out.println();
                    LOGGER.warning("STAFF NOT FOUND! - IC: " + ic);
                    LOGGER.warning("STAFF NOT FOUND!");
                    System.out.println("<<<STAFF NOT FOUND!>>>");
                    System.out.println();
                    System.out.println("TIP: You can:");
                    System.out.println("  - View the staff list to verify the IC");
                    System.out.println("  - Use the search function to find the staff");
                    System.out.println("  - Check for typos in the IC");
                    System.out.println();
                    System.out.print("PRESS 'E' TO EXIT OR ENTER TO TRY AGAIN: ");
                    String retry = ValidationUtil.scanner.nextLine().trim();
                    if (retry.equalsIgnoreCase("E")) {
                        return;
                    }
                    continue; // Ask again
                }
                break; // Exit loop, staff found
            }
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
                LOGGER.warning("Invalid name format! Keeping current value. - Name: " + newName);
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
                String confirmPassword = PasswordUtil.readPassword("");
                if (newPassword.equals(confirmPassword)) {
                    staffToUpdate.setStfPassword(newPassword);
                    System.out.println("  -> Password updated successfully!");
                } else {
                    LOGGER.warning("Passwords do not match! Keeping current password.");
                    System.out.println("  -> Passwords do not match! Keeping current password.");
                }
            } else {
                LOGGER.warning("Invalid password format! Keeping current password.");
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
                if (newAge < 0) {
                    LOGGER.warning(StaffConfig.ErrorMessage.AGE_CANNOT_BE_NEGATIVE + " Keeping current value. - Age: " + newAge);
                    System.out.println("  -> " + StaffConfig.ErrorMessage.AGE_CANNOT_BE_NEGATIVE + " Keeping current value.");
                } else if (newAge >= 18 && newAge <= 54) {
                    staffToUpdate.setStfAge(newAge);
                    System.out.println("  -> " + StaffConfig.ErrorMessage.AGE_UPDATED);
                } else {
                    LOGGER.warning(StaffConfig.ErrorMessage.AGE_INVALID + " - Age: " + newAge);
                    System.out.println("  -> " + StaffConfig.ErrorMessage.AGE_INVALID);
                }
            } catch (NumberFormatException e) {
                LOGGER.warning(StaffConfig.ErrorMessage.INVALID_NUMBER + " Keeping current value. - Input: " + ageInput);
                System.out.println("  -> " + StaffConfig.ErrorMessage.INVALID_NUMBER + " Keeping current value.");
            }
        } else {
            System.out.println("  -> " + StaffConfig.ErrorMessage.KEEPING_CURRENT_AGE);
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
                if (newSalary < 0) {
                    LOGGER.warning(StaffConfig.ErrorMessage.SALARY_CANNOT_BE_NEGATIVE + " Keeping current value. - Salary: " + newSalary);
                    System.out.println("  -> " + StaffConfig.ErrorMessage.SALARY_CANNOT_BE_NEGATIVE + " Keeping current value.");
                } else if (newSalary > 0) {
                    if (newSalary > 1000000) {
                        System.out.print("  -> " + StaffConfig.WARNING_SALARY_HIGH);
                        String confirm = ValidationUtil.scanner.nextLine().trim();
                        if (!confirm.equalsIgnoreCase("Y")) {
                            System.out.println("  -> " + StaffConfig.ErrorMessage.KEEPING_CURRENT_SALARY);
                        } else {
                            staffToUpdate.setStfSalary(newSalary);
                            System.out.println("  -> " + StaffConfig.ErrorMessage.SALARY_UPDATED);
                        }
                    } else {
                        staffToUpdate.setStfSalary(newSalary);
                        System.out.println("  -> " + StaffConfig.ErrorMessage.SALARY_UPDATED);
                    }
                } else {
                    LOGGER.warning(StaffConfig.ErrorMessage.SALARY_INVALID + " - Salary: " + newSalary);
                    System.out.println("  -> " + StaffConfig.ErrorMessage.SALARY_INVALID);
                }
            } catch (NumberFormatException e) {
                LOGGER.warning(StaffConfig.ErrorMessage.INVALID_NUMBER + " Keeping current value. - Input: " + salaryInput);
                System.out.println("  -> " + StaffConfig.ErrorMessage.INVALID_NUMBER + " Keeping current value.");
            }
        } else {
            System.out.println("  -> " + StaffConfig.ErrorMessage.KEEPING_CURRENT_SALARY);
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
                LOGGER.severe("FAILED TO UPDATE STAFF!");
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
     * Allows deletion by Name or IC with confirmation.
     * Prevents staff from deleting themselves.
     */
    private void deleteStaff(Staff currentStaff) {
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

        String choice;
        while (true) {
            staffView.printDeleteStaffMenu(staffList);
            System.out.print("ENTER YOUR CHOICE (OR 'E' TO CANCEL): ");

            choice = ValidationUtil.scanner.nextLine().trim();

            if (choice.equalsIgnoreCase("E")) {
                return;
            }

            if (!choice.equals("1") && !choice.equals("2")) {
                LOGGER.warning("INVALID CHOICE! Please enter 1 or 2!");
                System.out.println("\n<<<INVALID CHOICE! Please enter 1 or 2!>>>\n");
                System.out.print("PRESS 'E' TO EXIT OR ENTER TO TRY AGAIN: ");
                String retry = ValidationUtil.scanner.nextLine().trim();
                if (retry.equalsIgnoreCase("E")) {
                    return;
                }
                ConsoleUtil.clearScreen();
                ConsoleUtil.logo();
                continue; // Try again
            }
            break; // Valid choice, exit loop
        }

        Staff staffToDelete = null;
        String identifier = "";

        if (choice.equals("1")) {
            // Delete by Name
            while (true) {
                System.out.print("ENTER STAFF NAME TO DELETE (OR 'E' TO CANCEL): ");
                String name = ValidationUtil.scanner.nextLine().trim();

                if (name.equalsIgnoreCase("E")) {
                    return;
                }

                if (name.trim().isEmpty()) {
                    System.out.println(StaffConfig.ErrorMessage.NAME_CANNOT_BE_EMPTY);
                    System.out.println();
                    continue; // Ask again
                }

                List<Staff> results = staffService.findByName(name);
                if (results.isEmpty()) {
                    System.out.println();
                    LOGGER.warning("STAFF NOT FOUND!");
                    System.out.println("<<<STAFF NOT FOUND!>>>");
                    System.out.println();
                    System.out.println("TIP: You can:");
                    System.out.println("  - View the staff list to verify the name");
                    System.out.println("  - Use the search function to find the staff");
                    System.out.println("  - Check for typos in the name");
                    System.out.println();
                    System.out.print("PRESS 'E' TO EXIT OR ENTER TO TRY AGAIN: ");
                    String retry = ValidationUtil.scanner.nextLine().trim();
                    if (retry.equalsIgnoreCase("E")) {
                        return;
                    }
                    continue; // Ask again
                } else if (results.size() == 1) {
                    staffToDelete = results.get(0);
                    identifier = "NAME '" + name + "'";
                    break; // Exit loop, staff found
                } else {
                    // Multiple staff found with same name
                    System.out.println();
                    System.out.println("Multiple staff found with name containing \"" + name + "\":");
                    System.out.println(AppConfig.SEPARATOR_LINE);
                    int index = 1;
                    for (Staff s : results) {
                        System.out.println("[" + index + "]");
                        System.out.println(s.toString());
                        if (index < results.size()) {
                            System.out.println(AppConfig.SEPARATOR_LINE);
                        }
                        index++;
                    }
                    System.out.println();
                    System.out.print("ENTER THE NUMBER OF THE STAFF TO DELETE (OR '0' TO CANCEL): ");
                    int selection = ValidationUtil.intValidation(0, results.size());
                    if (selection == SalesUtil.INVALID_INPUT || selection == 0 || selection < 1 || selection > results.size()) {
                        return;
                    }
                    staffToDelete = results.get(selection - 1);
                    identifier = "NAME '" + name + "'";
                    break; // Exit loop, staff selected
                }
            }

        } else if (choice.equals("2")) {
            // Delete by IC
            while (true) {
                System.out.print("ENTER STAFF IC TO DELETE (OR 'E' TO CANCEL): ");
                String icToDelete = ValidationUtil.scanner.nextLine().trim();

                if (icToDelete.equalsIgnoreCase("E")) {
                    return;
                }

                if (icToDelete.trim().isEmpty()) {
                    LOGGER.warning(StaffConfig.ErrorMessage.IC_CANNOT_BE_EMPTY);
                    System.out.println(StaffConfig.ErrorMessage.IC_CANNOT_BE_EMPTY);
                    System.out.println();
                    continue; // Ask again
                }

                if (!icToDelete.matches("\\d{12}")) {
                    LOGGER.warning("INVALID IC FORMAT! IC must be 12 digits!");
                    System.out.println("\n<<<INVALID IC FORMAT! IC must be 12 digits!>>>\n");
                    continue; // Ask again
                }

                staffToDelete = staffService.findByIc(icToDelete);
                if (staffToDelete == null) {
                    System.out.println();
                    LOGGER.warning("STAFF NOT FOUND!");
                    System.out.println("<<<STAFF NOT FOUND!>>>");
                    System.out.println();
                    System.out.println("TIP: You can:");
                    System.out.println("  - View the staff list to verify the IC");
                    System.out.println("  - Use the search function to find the staff");
                    System.out.println("  - Check for typos in the IC");
                    System.out.println();
                    System.out.print("PRESS 'E' TO EXIT OR ENTER TO TRY AGAIN: ");
                    String retry = ValidationUtil.scanner.nextLine().trim();
                    if (retry.equalsIgnoreCase("E")) {
                        return;
                    }
                    continue; // Ask again
                }
                identifier = "IC " + icToDelete;
                break; // Exit loop, staff found
            }
        }

        // Check if staff is trying to delete themselves
        if (currentStaff != null && staffToDelete.getIc().equals(currentStaff.getIc())) {
            System.out.println();
            System.out.println(StaffConfig.ErrorMessage.CANNOT_DELETE_YOURSELF);
            System.out.println();
            System.out.println(StaffConfig.ErrorMessage.CANNOT_DELETE_YOURSELF_REASON);
            System.out.println(StaffConfig.ErrorMessage.CANNOT_DELETE_YOURSELF_TIP);
            System.out.println();
            System.out.print("PRESS 'E' TO EXIT OR ENTER TO TRY AGAIN: ");
            String retry = ValidationUtil.scanner.nextLine().trim();
            if (retry.equalsIgnoreCase("E")) {
                return;
            }
            // Retry by restarting the delete process
            deleteStaff(currentStaff);
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
            System.out.print("PRESS 'E' TO EXIT OR ENTER TO CONTINUE: ");
            String retry = ValidationUtil.scanner.nextLine().trim();
            if (retry.equalsIgnoreCase("E")) {
                return;
            }
            // Continue by restarting the delete process
            deleteStaff(currentStaff);
            return;
        }

        // Perform deletion
        boolean deleted = staffService.deleteByIc(staffToDelete.getIc());

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
            LOGGER.severe("FAILED TO DELETE STAFF!");
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
            while (true) {
                System.out.print("ENTER STAFF ID TO SEARCH (OR '0' TO CANCEL): S-");
                String idInput = ValidationUtil.scanner.nextLine().trim();

                if (idInput.equalsIgnoreCase("E") || idInput.equals("0")) {
                    return;
                }

                if (idInput.isEmpty()) {
                    LOGGER.warning("ID CANNOT BE EMPTY!");
                    System.out.println("\n<<<ID CANNOT BE EMPTY!>>>\n");
                    System.out.print("PRESS 'E' TO EXIT OR ENTER TO TRY AGAIN: ");
                    String retry = ValidationUtil.scanner.nextLine().trim();
                    if (retry.equalsIgnoreCase("E")) {
                        return;
                    }
                    ConsoleUtil.clearScreen();
                    ConsoleUtil.logo();
                    staffView.printSearchStaffMenu(staffService.getAllStaff());
                    System.out.println("1. Staff ID");
                    continue;
                }

                try {
                    int staffId = Integer.parseInt(idInput);
                    Staff staff = staffService.findById(staffId);
                    if (staff != null) {
                        results.add(staff);
                        searchType = "ID S-" + staffId;
                        break;
                    } else {
                        System.out.println();
                        LOGGER.warning("STAFF NOT FOUND!");
                    System.out.println("<<<STAFF NOT FOUND!>>>");
                        System.out.println();
                        System.out.println("TIP: You can:");
                        System.out.println("  - View the staff list to verify the ID");
                        System.out.println("  - Use the search function to find the staff");
                        System.out.println("  - Check for typos in the ID");
                        System.out.println();
                        System.out.print("PRESS 'E' TO EXIT OR ENTER TO TRY AGAIN: ");
                        String retry = ValidationUtil.scanner.nextLine().trim();
                        if (retry.equalsIgnoreCase("E")) {
                            return;
                        }
                        ConsoleUtil.clearScreen();
                        ConsoleUtil.logo();
                        staffView.printSearchStaffMenu(staffService.getAllStaff());
                        System.out.println("1. Staff ID");
                        continue;
                    }
                } catch (NumberFormatException e) {
                    LOGGER.warning("INVALID INPUT! Please enter a valid number!");
                    System.out.println("\n<<<INVALID INPUT! Please enter a valid number!>>>\n");
                    System.out.print("PRESS 'E' TO EXIT OR ENTER TO TRY AGAIN: ");
                    String retry = ValidationUtil.scanner.nextLine().trim();
                    if (retry.equalsIgnoreCase("E")) {
                        return;
                    }
                    ConsoleUtil.clearScreen();
                    ConsoleUtil.logo();
                    staffView.printSearchStaffMenu(staffService.getAllStaff());
                    System.out.println("1. Staff ID");
                    continue;
                }
            }

        } else if (choice.equals("2")) {
            // Search by IC
            while (true) {
                System.out.print("ENTER STAFF IC TO SEARCH (OR 'E' TO CANCEL): ");
                String ic = ValidationUtil.scanner.nextLine().trim();

                if (ic.equalsIgnoreCase("E")) {
                    return;
                }

                if (ic.isEmpty()) {
                    LOGGER.warning(StaffConfig.ErrorMessage.IC_CANNOT_BE_EMPTY);
                    System.out.println(StaffConfig.ErrorMessage.IC_CANNOT_BE_EMPTY);
                    System.out.println();
                    System.out.print("PRESS 'E' TO EXIT OR ENTER TO TRY AGAIN: ");
                    String retry = ValidationUtil.scanner.nextLine().trim();
                    if (retry.equalsIgnoreCase("E")) {
                        return;
                    }
                    ConsoleUtil.clearScreen();
                    ConsoleUtil.logo();
                    staffView.printSearchStaffMenu(staffService.getAllStaff());
                    System.out.println("2. Staff IC");
                    continue;
                }

                if (!ic.matches("\\d{12}")) {
                    LOGGER.warning("INVALID IC FORMAT! IC must be 12 digits!");
                    System.out.println("\n<<<INVALID IC FORMAT! IC must be 12 digits!>>>\n");
                    System.out.print("PRESS 'E' TO EXIT OR ENTER TO TRY AGAIN: ");
                    String retry = ValidationUtil.scanner.nextLine().trim();
                    if (retry.equalsIgnoreCase("E")) {
                        return;
                    }
                    ConsoleUtil.clearScreen();
                    ConsoleUtil.logo();
                    staffView.printSearchStaffMenu(staffService.getAllStaff());
                    System.out.println("2. Staff IC");
                    continue;
                }

                Staff staff = staffService.findByIc(ic);
                if (staff != null) {
                    results.add(staff);
                    searchType = "IC " + ic;
                    break;
                } else {
                    System.out.println();
                    LOGGER.warning("STAFF NOT FOUND!");
                    System.out.println("<<<STAFF NOT FOUND!>>>");
                    System.out.println();
                    System.out.println("TIP: You can:");
                    System.out.println("  - View the staff list to verify the IC");
                    System.out.println("  - Use the search function to find the staff");
                    System.out.println("  - Check for typos in the IC");
                    System.out.println();
                    System.out.print("PRESS 'E' TO EXIT OR ENTER TO TRY AGAIN: ");
                    String retry = ValidationUtil.scanner.nextLine().trim();
                    if (retry.equalsIgnoreCase("E")) {
                        return;
                    }
                    ConsoleUtil.clearScreen();
                    ConsoleUtil.logo();
                    staffView.printSearchStaffMenu(staffService.getAllStaff());
                    System.out.println("2. Staff IC");
                    continue;
                }
            }

        } else if (choice.equals("3")) {
            // Search by Name
            while (true) {
                System.out.print("ENTER STAFF NAME TO SEARCH (OR 'E' TO CANCEL): ");
                String name = ValidationUtil.scanner.nextLine().trim();

                if (name.equalsIgnoreCase("E")) {
                    return;
                }

                if (name.isEmpty()) {
                    System.out.println(StaffConfig.ErrorMessage.NAME_CANNOT_BE_EMPTY);
                    System.out.println();
                    System.out.print("PRESS 'E' TO EXIT OR ENTER TO TRY AGAIN: ");
                    String retry = ValidationUtil.scanner.nextLine().trim();
                    if (retry.equalsIgnoreCase("E")) {
                        return;
                    }
                    ConsoleUtil.clearScreen();
                    ConsoleUtil.logo();
                    staffView.printSearchStaffMenu(staffService.getAllStaff());
                    System.out.println("3. Staff Name");
                    continue;
                }

                results = staffService.findByName(name);
                if (!results.isEmpty()) {
                    searchType = "NAME '" + name + "'";
                    break;
                } else {
                    System.out.println();
                    LOGGER.warning("STAFF NOT FOUND!");
                    System.out.println("<<<STAFF NOT FOUND!>>>");
                    System.out.println();
                    System.out.println("TIP: You can:");
                    System.out.println("  - View the staff list to verify the name");
                    System.out.println("  - Use the search function to find the staff");
                    System.out.println("  - Check for typos in the name");
                    System.out.println("  - Try a partial name match");
                    System.out.println();
                    System.out.print("PRESS 'E' TO EXIT OR ENTER TO TRY AGAIN: ");
                    String retry = ValidationUtil.scanner.nextLine().trim();
                    if (retry.equalsIgnoreCase("E")) {
                        return;
                    }
                    ConsoleUtil.clearScreen();
                    ConsoleUtil.logo();
                    staffView.printSearchStaffMenu(staffService.getAllStaff());
                    System.out.println("3. Staff Name");
                    continue;
                }
            }

        } else {
            LOGGER.warning("INVALID CHOICE! Please enter 1, 2, or 3!");
            System.out.println("\n<<<INVALID CHOICE! Please enter 1, 2, or 3!>>>\n");
            System.out.print("PRESS 'E' TO EXIT OR ENTER TO TRY AGAIN: ");
            String retry = ValidationUtil.scanner.nextLine().trim();
            if (retry.equalsIgnoreCase("E")) {
                return;
            }
            // Retry by restarting the search process
            searchStaff();
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
                LOGGER.warning("IC cannot be empty. Please enter a 12-digit IC number.");
                System.out.println("  -> ERROR: IC cannot be empty. Please enter a 12-digit IC number.");
                System.out.println();
                continue;
            }

            if (!ic.matches("\\d{12}")) {
                LOGGER.warning("Invalid format! IC must be exactly 12 digits. - IC: " + ic);
                System.out.println("  -> ERROR: Invalid format! IC must be exactly 12 digits.");
                System.out.println("     Example: 123456789012");
                System.out.println();
                continue;
            }

            if (staffService.getAllStaff().stream()
                    .anyMatch(s -> s.getStfIC().equals(ic))) {
                LOGGER.warning("This IC already exists in the system! - IC: " + ic);
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
                LOGGER.warning("Name cannot be empty. Please enter a valid name.");
                System.out.println("  -> ERROR: Name cannot be empty. Please enter a valid name.");
                System.out.println();
                continue;
            }

            if (!name.matches("^[a-zA-Z ]+$")) {
                LOGGER.warning("Invalid characters! Name should contain only letters and spaces. - Name: " + name);
                System.out.println("  -> ERROR: Invalid characters! Name should contain only:");
                System.out.println("     - Letters (A-Z, a-z)");
                System.out.println("     - Spaces");
                System.out.println("     Example: John Smith, Ahmad Bin Ali");
                System.out.println();
                continue;
            }

            if (name.length() < 2) {
                LOGGER.warning("Name is too short. Please enter at least 2 characters. - Name: " + name);
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
            String password = PasswordUtil.readPassword("");

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
            String confirmPassword = PasswordUtil.readPassword("");

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
                LOGGER.warning("Age cannot be empty. Please enter a number.");
                System.out.println("  -> ERROR: Age cannot be empty. Please enter a number.");
                System.out.println();
                continue;
            }

            try {
                int age = Integer.parseInt(input);
                if (age < 0) {
                    LOGGER.warning(StaffConfig.ErrorMessage.AGE_CANNOT_BE_NEGATIVE);
                    System.out.println("  -> ERROR: " + StaffConfig.ErrorMessage.AGE_CANNOT_BE_NEGATIVE);
                    System.out.println();
                    continue;
                }
                if (age < 18) {
                    LOGGER.warning(StaffConfig.ErrorMessage.AGE_TOO_YOUNG);
                    System.out.println("  -> ERROR: " + StaffConfig.ErrorMessage.AGE_TOO_YOUNG);
                    System.out.println();
                    continue;
                }
                if (age > 54) {
                    LOGGER.warning(StaffConfig.ErrorMessage.AGE_TOO_OLD);
                    System.out.println("  -> ERROR: " + StaffConfig.ErrorMessage.AGE_TOO_OLD);
                    System.out.println();
                    continue;
                }
                System.out.println("  -> " + StaffConfig.SuccessfulMessage.AGE_VALIDATED);
                System.out.println();
                return age;
            } catch (NumberFormatException e) {
                LOGGER.warning(StaffConfig.ErrorMessage.INVALID_NUMBER + " - Age input: " + input);
                System.out.println("  -> ERROR: " + StaffConfig.ErrorMessage.INVALID_NUMBER);
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
                LOGGER.warning("Salary cannot be empty. Please enter a number.");
                System.out.println("  -> ERROR: Salary cannot be empty. Please enter a number.");
                System.out.println();
                continue;
            }

            try {
                double salary = Double.parseDouble(input);
                if (salary < 0) {
                    LOGGER.warning(StaffConfig.ErrorMessage.SALARY_CANNOT_BE_NEGATIVE);
                    System.out.println("  -> ERROR: " + StaffConfig.ErrorMessage.SALARY_CANNOT_BE_NEGATIVE);
                    System.out.println();
                    continue;
                }
                if (salary <= 0) {
                    LOGGER.warning(StaffConfig.ErrorMessage.SALARY_MUST_BE_POSITIVE);
                    System.out.println("  -> ERROR: " + StaffConfig.ErrorMessage.SALARY_MUST_BE_POSITIVE);
                    System.out.println("     Please enter a positive amount (e.g., 2500.00)");
                    System.out.println();
                    continue;
                }
                if (salary > 1000000) {
                    System.out.print("  -> " + StaffConfig.ErrorMessage.SALARY_UNUSUALLY_HIGH);
                    String confirm = ValidationUtil.scanner.nextLine().trim();
                    if (!confirm.equalsIgnoreCase("Y")) {
                        System.out.println("  -> Please re-enter the salary.");
                        System.out.println();
                        continue;
                    }
                }
                System.out.println("  -> " + StaffConfig.SuccessfulMessage.SALARY_VALIDATED);
                System.out.println();
                return salary;
            } catch (NumberFormatException e) {
                LOGGER.warning(StaffConfig.ErrorMessage.INVALID_NUMBER + " - Salary input: " + input);
                System.out.println("  -> ERROR: " + StaffConfig.ErrorMessage.INVALID_NUMBER);
                System.out.println();
            }
        }
    }

}

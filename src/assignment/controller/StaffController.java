package assignment.controller;

import assignment.enums.StaffMenu;
import assignment.model.Staff;
import assignment.util.ConsoleUtil;
import assignment.util.ValidationUtil;

/**
 * Controller for staff-related flows.
 * Handles staff menu and delegates to Staff model for file operations.
 */
public class StaffController {

    private final Staff staff;

    public StaffController() {
        this.staff = new Staff();
    }

    public void manageStaff() {
        while (true) {
            ConsoleUtil.clearScreen();
            ConsoleUtil.logo();
            printStaffMenu();
            System.out.print("ENTER YOUR SELECTION: ");

            int staffOpt = ValidationUtil.intValidation(0, 4);

            if (staffOpt == -9999) {
                ConsoleUtil.systemPause();
                continue;
            }

            StaffMenu selection = StaffMenu.getByOption(staffOpt);
            if (selection == null) {
                System.out.println("<<<INVALID OPTION>>>");
                ConsoleUtil.systemPause();
                continue;
            }

            switch (selection) {
                case ADD_STAFF -> {
                    ConsoleUtil.clearScreen();
                    staff.add();
                }
                case DELETE_STAFF -> {
                    ConsoleUtil.clearScreen();
                    staff.delete();
                }
                case SEARCH_STAFF -> {
                    ConsoleUtil.clearScreen();
                    staff.search();
                }
                case VIEW_STAFF_LIST -> {
                    ConsoleUtil.clearScreen();
                    staff.view();
                }
                case BACK_TO_MAIN -> {
                    System.out.println("BACK TO MAIN MENU...");
                    ConsoleUtil.systemPause();
                    return;
                }
            }
        }
    }

    private void printStaffMenu() {
        System.out.println("[ STAFF MANAGEMENT SYSTEM ]");
        System.out.println("-------------------------------------------------------");
        for (StaffMenu menu : StaffMenu.values()) {
            System.out.printf("%d. %s%n", menu.getOption(), menu.getDescription());
        }
        System.out.println("-------------------------------------------------------");
    }
}



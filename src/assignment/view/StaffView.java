package assignment.view;

import assignment.enums.StaffMenu;
import assignment.model.Staff;
import assignment.util.config.AppConfig;
import assignment.util.config.StaffConfig;
import java.util.List;

/**
 * View class for Staff management.
 * Handles all print outputs and menu displays.
 */
public class StaffView {

    public void printStaffMenu(int totalStaff) {
        System.out.println(StaffConfig.TITLE_STAFF_SYSTEM);
        System.out.println(AppConfig.SEPARATOR_LINE);
        System.out.println(String.format(StaffConfig.MSG_TOTAL_STAFF, totalStaff));
        System.out.println(AppConfig.SEPARATOR_LINE);
        System.out.println(StaffConfig.MSG_PLEASE_SELECT_OPTION);
        System.out.println(AppConfig.SEPARATOR_LINE);

        for (StaffMenu menu : StaffMenu.values()) {
            System.out.printf("%d. %s%n", menu.getOption(), menu.getDescription());
        }
        System.out.println(AppConfig.SEPARATOR_LINE);
    }

    public void printAddStaffHeader(int currentCount) {
        System.out.println(StaffConfig.TITLE_ADD_STAFF);
        System.out.println(AppConfig.SEPARATOR_LINE);
        System.out.println(StaffConfig.MSG_FILL_INFORMATION);
        System.out.println(StaffConfig.MSG_PRESS_E_TO_CANCEL);
        System.out.println(AppConfig.SEPARATOR_LINE);
        System.out.println(String.format(StaffConfig.MSG_CURRENT_STAFF_COUNT, currentCount));
        System.out.println(AppConfig.SEPARATOR_LINE);
        System.out.println();
    }

    public void printNewStaffSummary(Staff newStaff) {
        System.out.println();
        System.out.println(AppConfig.SEPARATOR_LINE);
        System.out.println(StaffConfig.MSG_SUMMARY_REVIEW);
        System.out.println(AppConfig.SEPARATOR_LINE);
        System.out.printf("STAFF ID:     S-%d%n", newStaff.getId());
        System.out.printf("NAME:         %s%n", newStaff.getName());
        System.out.printf("IC:           %s%n", newStaff.getStfIC());
        System.out.printf("AGE:          %d years%n", newStaff.getStfAge());
        System.out.printf("SALARY:       RM %,.2f%n", newStaff.getStfSalary());
        System.out.println(AppConfig.SEPARATOR_LINE);
    }

    public void printStaffAddedSuccess(Staff newStaff, int newCount) {
        System.out.println();
        System.out.println(AppConfig.SEPARATOR_LONG);
        System.out.println(StaffConfig.SuccessfulMessage.STAFF_ADDED);
        System.out.println(AppConfig.SEPARATOR_LONG);
        System.out.printf("STAFF ID:     S-%d%n", newStaff.getId());
        System.out.printf("NAME:         %s%n", newStaff.getName());
        System.out.printf("IC:           %s%n", newStaff.getStfIC());
        System.out.printf("AGE:          %d years%n", newStaff.getStfAge());
        System.out.printf("SALARY:       RM %,.2f%n", newStaff.getStfSalary());
        System.out.println(AppConfig.SEPARATOR_LONG);
        System.out.println(String.format(StaffConfig.MSG_NEW_STAFF_COUNT, newCount));
        System.out.println(AppConfig.SEPARATOR_LONG);
        System.out.println();
    }

    public void printUpdateStaffMenu() {
        System.out.println(StaffConfig.TITLE_UPDATE_STAFF);
        System.out.println(AppConfig.SEPARATOR_LINE);
        System.out.println(StaffConfig.MSG_FIND_STAFF_BY);
        System.out.println(StaffConfig.MSG_FIND_BY_NAME);
        System.out.println(StaffConfig.MSG_FIND_BY_IC);
        System.out.println(AppConfig.SEPARATOR_LINE);
    }

    public void displayStaffDetails(Staff staff) {
        System.out.println(staff.toString());
    }

    public void printDeleteStaffMenu(List<Staff> staffList) {
        System.out.println(StaffConfig.TITLE_DELETE_STAFF);
        System.out.println(AppConfig.SEPARATOR_LINE);
        System.out.println(StaffConfig.WARNING_DELETE_IRREVERSIBLE);
        System.out.println(AppConfig.SEPARATOR_LINE);
        System.out.println();

        System.out.println(StaffConfig.MSG_CURRENT_STAFF_LIST);
        System.out.println(AppConfig.SEPARATOR_LONG);
        System.out.printf("%-10s %-25s %-15s %-10s%n",
                StaffConfig.HEADER_STAFF_ID, StaffConfig.HEADER_STAFF_NAME, StaffConfig.HEADER_STAFF_IC, StaffConfig.HEADER_AGE);
        System.out.println(AppConfig.SEPARATOR_LONG);

        for (Staff staff : staffList) {
            System.out.printf("%-10s %-25s %-15s %-10d%n",
                    "S-" + staff.getId(),
                    staff.getName(),
                    staff.getIc(),
                    staff.getStfAge());
        }

        System.out.println(AppConfig.SEPARATOR_LONG);
        System.out.println(String.format(StaffConfig.MSG_TOTAL_STAFF_COUNT, staffList.size()));
        System.out.println(AppConfig.SEPARATOR_LINE);
        System.out.println();

        System.out.println(StaffConfig.MSG_DELETE_BY);
        System.out.println(StaffConfig.MSG_FIND_BY_NAME);
        System.out.println(StaffConfig.MSG_FIND_BY_IC);
        System.out.println(AppConfig.SEPARATOR_LINE);
    }

    public void printDeleteConfirmation(Staff staffToDelete) {
        System.out.println(StaffConfig.TITLE_DELETE_STAFF_CONFIRMATION);
        System.out.println(AppConfig.SEPARATOR_LINE);
        System.out.println(StaffConfig.WARNING_DELETE_IRREVERSIBLE);
        System.out.println(AppConfig.SEPARATOR_LINE);
        System.out.println(StaffConfig.MSG_STAFF_TO_BE_DELETED);
        System.out.println(AppConfig.SEPARATOR_LINE);
        System.out.println(staffToDelete.toString());
        System.out.println(AppConfig.SEPARATOR_LINE);
    }

    public void printSearchStaffMenu(List<Staff> quickList) {
        System.out.println(StaffConfig.TITLE_SEARCH_STAFF);
        System.out.println(AppConfig.SEPARATOR_LINE);

        if (!quickList.isEmpty() && quickList.size() <= 10) {
            System.out.println(StaffConfig.MSG_QUICK_REFERENCE);
            System.out.println(AppConfig.SEPARATOR_LINE);
            System.out.printf("%-10s %-20s %-15s%n", StaffConfig.HEADER_STAFF_ID, StaffConfig.HEADER_STAFF_NAME, StaffConfig.HEADER_STAFF_IC);
            System.out.println(AppConfig.SEPARATOR_LINE);
            for (Staff s : quickList) {
                System.out.printf("%-10s %-20s %-15s%n",
                        "S-" + s.getId(), s.getName(), s.getIc());
            }
            System.out.println(AppConfig.SEPARATOR_LINE);
            System.out.println();
        }

        System.out.println(StaffConfig.MSG_SEARCH_BY);
        System.out.println(StaffConfig.MSG_SEARCH_BY_ID);
        System.out.println(StaffConfig.MSG_SEARCH_BY_IC);
        System.out.println(StaffConfig.MSG_SEARCH_BY_NAME);
        System.out.println(AppConfig.SEPARATOR_LINE);
    }

    public void displaySearchResults(List<Staff> results, String searchType) {
        if (results.isEmpty()) {
            System.out.println(StaffConfig.TITLE_SEARCH_RESULTS);
            System.out.println(AppConfig.SEPARATOR_LINE);
            System.out.println(String.format(StaffConfig.ErrorMessage.NO_STAFF_FOUND, searchType));
            System.out.println();
            System.out.println(StaffConfig.ErrorMessage.TIP_SEARCH_DIFFERENT);
            System.out.println(StaffConfig.ErrorMessage.TIP_DIFFERENT_SPELLING);
            System.out.println(StaffConfig.ErrorMessage.TIP_PARTIAL_NAME);
            System.out.println(StaffConfig.ErrorMessage.TIP_CHECK_LIST);
            System.out.println();
        } else {
            System.out.println(StaffConfig.TITLE_SEARCH_RESULTS);
            System.out.println(AppConfig.SEPARATOR_LINE);
            System.out.println(String.format(StaffConfig.MSG_SEARCH_CRITERIA, searchType));
            System.out.println(String.format(StaffConfig.MSG_FOUND, results.size()));
            System.out.println(AppConfig.SEPARATOR_LINE);
            System.out.println();
            int index = 1;
            for (Staff staff : results) {
                System.out.println("[" + index + "]");
                System.out.println(staff.toString());
                if (index < results.size()) {
                    System.out.println(AppConfig.SEPARATOR_LINE);
                }
                index++;
            }
            System.out.println();
            System.out.println(AppConfig.SEPARATOR_LINE);
        }
    }

    public void displayStaffList(List<Staff> staffList) {
        System.out.println(StaffConfig.TITLE_VIEW_STAFF);
        System.out.println(AppConfig.SEPARATOR_LINE);
        System.out.println();

        if (staffList.isEmpty()) {
            System.out.println(StaffConfig.ErrorMessage.NO_STAFF_TO_DISPLAY);
            System.out.println();
            System.out.println(StaffConfig.ErrorMessage.TIP_ADD_STAFF);
            System.out.println();
        } else {
            // Calculate statistics
            double totalSalary = 0;
            int totalAge = 0;
            double avgAge = 0;
            double minSalary = Double.MAX_VALUE;
            double maxSalary = 0;

            for (Staff staff : staffList) {
                totalSalary += staff.getStfSalary();
                totalAge += staff.getStfAge();
                if (staff.getStfSalary() < minSalary) minSalary = staff.getStfSalary();
                if (staff.getStfSalary() > maxSalary) maxSalary = staff.getStfSalary();
            }

            if (!staffList.isEmpty()) {
                avgAge = (double) totalAge / staffList.size();
            }

            // Display statistics
            System.out.println(StaffConfig.MSG_STATISTICS);
            System.out.println(AppConfig.SEPARATOR_LINE);
            System.out.printf("Total Staff Members: %d%n", staffList.size());
            System.out.printf("Average Age: %.1f years%n", avgAge);
            System.out.printf("Highest Salary: RM %.2f%n", maxSalary);
            System.out.printf("Lowest Salary: RM %.2f%n", minSalary);
            System.out.printf("Total Payroll: RM %.2f%n", totalSalary);
            System.out.println(AppConfig.SEPARATOR_LINE);
            System.out.println();

            // Display staff list
            System.out.println(StaffConfig.MSG_STAFF_LIST);
            System.out.println(AppConfig.SEPARATOR_LONG);
            System.out.printf("%-10s %-20s %-15s %-10s %-15s%n",
                    StaffConfig.HEADER_STAFF_ID, StaffConfig.HEADER_STAFF_NAME, StaffConfig.HEADER_STAFF_IC, StaffConfig.HEADER_AGE, StaffConfig.HEADER_SALARY);
            System.out.println(AppConfig.SEPARATOR_LONG);

            for (Staff staff : staffList) {
                System.out.printf("%-10s %-20s %-15s %-10d RM%-14.2f%n",
                        "S-" + staff.getId(),
                        staff.getName(),
                        staff.getIc(),
                        staff.getStfAge(),
                        staff.getStfSalary());
            }

            System.out.println(AppConfig.SEPARATOR_LONG);
            System.out.println(String.format(StaffConfig.MSG_TOTAL_STAFF_COUNT, staffList.size()));
            System.out.println();
        }
    }
}

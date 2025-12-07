package assignment.view;

import assignment.enums.StaffMenu;
import assignment.model.Staff;
import java.util.List;

/**
 * View class for Staff management.
 * Handles all print outputs and menu displays.
 */
public class StaffView {

    public void printStaffMenu(int totalStaff) {
        System.out.println("[ STAFF MANAGEMENT SYSTEM ]");
        System.out.println("-------------------------------------------------------");
        System.out.println("Total Staff: " + totalStaff);
        System.out.println("-------------------------------------------------------");
        System.out.println("Please select an option:");
        System.out.println("-------------------------------------------------------");
        
        for (StaffMenu menu : StaffMenu.values()) {
            System.out.printf("%d. %s%n", menu.getOption(), menu.getDescription());
        }
        System.out.println("-------------------------------------------------------");
    }

    public void printAddStaffHeader(int currentCount) {
        System.out.println("[ ADD NEW STAFF ]");
        System.out.println("-------------------------------------------------------");
        System.out.println("Please fill in the following information:");
        System.out.println("(Press 'E' at any time to cancel)");
        System.out.println("-------------------------------------------------------");
        System.out.println("Current Staff Count: " + currentCount);
        System.out.println("-------------------------------------------------------");
        System.out.println();
    }

    public void printNewStaffSummary(Staff newStaff) {
        System.out.println();
        System.out.println("-------------------------------------------------------");
        System.out.println("SUMMARY - Please review the information:");
        System.out.println("-------------------------------------------------------");
        System.out.printf("STAFF ID:     S-%d%n", newStaff.getId());
        System.out.printf("NAME:         %s%n", newStaff.getName());
        System.out.printf("IC:           %s%n", newStaff.getStfIC());
        System.out.printf("AGE:          %d years%n", newStaff.getStfAge());
        System.out.printf("SALARY:       RM %,.2f%n", newStaff.getStfSalary());
        System.out.println("-------------------------------------------------------");
    }

    public void printStaffAddedSuccess(Staff newStaff, int newCount) {
        System.out.println();
        System.out.println("========================================");
        System.out.println("  STAFF ADDED SUCCESSFULLY!");
        System.out.println("========================================");
        System.out.printf("STAFF ID:     S-%d%n", newStaff.getId());
        System.out.printf("NAME:         %s%n", newStaff.getName());
        System.out.printf("IC:           %s%n", newStaff.getStfIC());
        System.out.printf("AGE:          %d years%n", newStaff.getStfAge());
        System.out.printf("SALARY:       RM %,.2f%n", newStaff.getStfSalary());
        System.out.println("========================================");
        System.out.println("New Staff Count: " + newCount);
        System.out.println("========================================");
        System.out.println();
    }

    public void printUpdateStaffMenu() {
        System.out.println("[ UPDATE STAFF INFORMATION ]");
        System.out.println("-------------------------------------------------------");
        System.out.println("Find staff by:");
        System.out.println("1. Staff ID (e.g., S-123456)");
        System.out.println("2. Staff IC (e.g., 123456789012)");
        System.out.println("-------------------------------------------------------");
    }

    public void displayStaffDetails(Staff staff) {
        System.out.println(staff.toString());
    }

    public void printDeleteStaffMenu(List<Staff> staffList) {
        System.out.println("[ DELETE STAFF ]");
        System.out.println("-------------------------------------------------------");
        System.out.println("WARNING: This action cannot be undone!");
        System.out.println("-------------------------------------------------------\n");
        
        System.out.println("CURRENT STAFF LIST:");
        System.out.println("=================================================================================");
        System.out.printf("%-10s %-25s %-15s %-10s%n", 
                "STAFF ID", "STAFF NAME", "STAFF IC", "AGE");
        System.out.println("=================================================================================");
        
        for (Staff staff : staffList) {
            System.out.printf("%-10s %-25s %-15s %-10d%n",
                    "S-" + staff.getId(),
                    staff.getName(),
                    staff.getIc(),
                    staff.getStfAge());
        }
        
        System.out.println("=================================================================================");
        System.out.println("TOTAL STAFF: " + staffList.size());
        System.out.println("-------------------------------------------------------\n");
        
        System.out.println("Delete by:");
        System.out.println("1. Staff ID (e.g., S-123456)");
        System.out.println("2. Staff IC (e.g., 123456789012)");
        System.out.println("-------------------------------------------------------");
    }

    public void printDeleteConfirmation(Staff staffToDelete) {
        System.out.println("[ DELETE STAFF - CONFIRMATION ]");
        System.out.println("-------------------------------------------------------");
        System.out.println("WARNING: This action cannot be undone!");
        System.out.println("-------------------------------------------------------");
        System.out.println("STAFF TO BE DELETED:");
        System.out.println("-------------------------------------------------------");
        System.out.println(staffToDelete.toString());
        System.out.println("-------------------------------------------------------");
    }

    public void printSearchStaffMenu(List<Staff> quickList) {
        System.out.println("[ SEARCH STAFF ]");
        System.out.println("-------------------------------------------------------");
        
        if (!quickList.isEmpty() && quickList.size() <= 10) {
            System.out.println("QUICK REFERENCE - Current Staff List:");
            System.out.println("-------------------------------------------------------");
            System.out.printf("%-10s %-20s %-15s%n", "STAFF ID", "STAFF NAME", "STAFF IC");
            System.out.println("-------------------------------------------------------");
            for (Staff s : quickList) {
                System.out.printf("%-10s %-20s %-15s%n", 
                        "S-" + s.getId(), s.getName(), s.getIc());
            }
            System.out.println("-------------------------------------------------------\n");
        }
        
        System.out.println("Search by:");
        System.out.println("1. Staff ID");
        System.out.println("2. Staff IC");
        System.out.println("3. Staff Name");
        System.out.println("-------------------------------------------------------");
    }

    public void displaySearchResults(List<Staff> results, String searchType) {
        if (results.isEmpty()) {
            System.out.println("[ SEARCH RESULTS ]");
            System.out.println("-------------------------------------------------------");
            System.out.println("<<<NO STAFF FOUND WITH " + searchType + "!>>>");
            System.out.println();
            System.out.println("TIP: Try searching with:");
            System.out.println("  - Different spelling");
            System.out.println("  - Partial name (for name search)");
            System.out.println("  - Check the staff list to verify the ID/IC");
            System.out.println();
        } else {
            System.out.println("[ SEARCH RESULTS ]");
            System.out.println("-------------------------------------------------------");
            System.out.println("Search criteria: " + searchType);
            System.out.println("Found: " + results.size() + " staff member(s)");
            System.out.println("-------------------------------------------------------");
            System.out.println();
            int index = 1;
            for (Staff staff : results) {
                System.out.println("[" + index + "]");
                System.out.println(staff.toString());
                if (index < results.size()) {
                    System.out.println("-------------------------------------------------------");
                }
                index++;
            }
            System.out.println();
            System.out.println("-------------------------------------------------------");
        }
    }

    public void displayStaffList(List<Staff> staffList) {
        System.out.println("[ VIEW ALL STAFF ]");
        System.out.println("-------------------------------------------------------\n");

        if (staffList.isEmpty()) {
            System.out.println("THERE IS NO STAFF TO DISPLAY...");
            System.out.println();
            System.out.println("TIP: Use 'Add New Staff' option to register staff members.");
            System.out.println();
        } else {
            // Calculate statistics
            double totalSalary = 0;
            double avgSalary = 0;
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
                avgSalary = totalSalary / staffList.size();
                avgAge = (double) totalAge / staffList.size();
            }

            // Display statistics
            System.out.println("STATISTICS:");
            System.out.println("-------------------------------------------------------");
            System.out.printf("Total Staff Members: %d%n", staffList.size());
            System.out.printf("Average Age: %.1f years%n", avgAge);
            System.out.printf("Highest Salary: RM %.2f%n", maxSalary);
            System.out.printf("Lowest Salary: RM %.2f%n", minSalary);
            System.out.printf("Total Payroll: RM %.2f%n", totalSalary);
            System.out.println("-------------------------------------------------------\n");

            // Display staff list
            System.out.println("STAFF LIST:");
            System.out.println("=================================================================================");
            System.out.printf("%-10s %-20s %-15s %-10s %-15s%n", 
                    "STAFF ID", "STAFF NAME", "STAFF IC", "AGE", "SALARY");
            System.out.println("=================================================================================");

            for (Staff staff : staffList) {
                System.out.printf("%-10s %-20s %-15s %-10d RM%-14.2f%n",
                        "S-" + staff.getId(),
                        staff.getName(),
                        staff.getIc(),
                        staff.getStfAge(),
                        staff.getStfSalary());
            }

            System.out.println("=================================================================================");
            System.out.println("TOTAL STAFF: " + staffList.size() + "\n");
        }
    }
}

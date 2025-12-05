package assignment.controller;

import assignment.model.Staff;
import assignment.service.StaffService;
import assignment.util.ConsoleUtil;
import assignment.util.ValidationUtil;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Controller for handling login functionality.
 * Separates login UI logic from Main class.
 */
public class LoginController {
    
    private final StaffService staffService;
    private Staff currentStaff;
    
    public LoginController(StaffService staffService) {
        this.staffService = staffService;
    }
    
    /**
     * Displays the login screen and handles user authentication.
     * @return The logged-in Staff object if successful, null otherwise
     */
    public Staff performLogin() {
        while (true) {
            ConsoleUtil.clearScreen();
            ConsoleUtil.logo();
            displayLoginHeader();
            
            // Get IC
            System.out.print("ENTER IC: ");
            String ic = ValidationUtil.scanner.nextLine().trim();
            
            if (ic.isEmpty()) {
                System.out.println("<<<IC CANNOT BE EMPTY!>>>");
                ConsoleUtil.systemPause();
                continue;
            }
            
            // Get Password (with masking option)
            System.out.print("ENTER PASSWORD: ");
            String password = ValidationUtil.scanner.nextLine();
            
            if (password.isEmpty()) {
                System.out.println("<<<PASSWORD CANNOT BE EMPTY!>>>");
                ConsoleUtil.systemPause();
                continue;
            }
            
            // Attempt login
            Staff staff = staffService.login(ic, password);
            
            if (staff != null) {
                // Login successful
                LocalDateTime loginTime = LocalDateTime.now();
                System.out.println("\n========================================");
                System.out.println("  LOGIN SUCCESSFUL!");
                System.out.println("========================================");
                System.out.println("WELCOME, " + staff.getName().toUpperCase());
                System.out.println("LOGIN TIME: " + loginTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                System.out.println("========================================\n");
                
                // Set clock-in time
                staff.setClockIn(loginTime);
                this.currentStaff = staff;
                
                ConsoleUtil.systemPause();
                return staff;
            } else {
                // Login failed
                System.out.println("\n<<<LOGIN FAILED! INVALID IC OR PASSWORD!>>>\n");
                System.out.print("PRESS 'E' TO RETURN TO MENU OR ANY OTHER KEY TO RETRY: ");
                String choice = ValidationUtil.scanner.nextLine();
                
                if (choice.equalsIgnoreCase("E")) {
                    return null;
                }
            }
        }
    }
    
    /**
     * Displays the login header.
     */
    private void displayLoginHeader() {
        System.out.println("[ LOG IN ]");
        System.out.println("-------------------------------------------------------");
        System.out.println("Please enter your credentials to access the system.");
        System.out.println("-------------------------------------------------------\n");
    }
    
    /**
     * Returns the currently logged-in staff member.
     */
    public Staff getCurrentStaff() {
        return currentStaff;
    }
    
    /**
     * Logs out the current staff member.
     */
    public void logout() {
        if (currentStaff != null) {
            LocalDateTime logoutTime = LocalDateTime.now();
            currentStaff.setClockOut(logoutTime);
            System.out.println("\n========================================");
            System.out.println("  LOGOUT SUCCESSFUL");
            System.out.println("========================================");
            System.out.println("GOODBYE, " + currentStaff.getName().toUpperCase());
            System.out.println("LOGOUT TIME: " + logoutTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            System.out.println("========================================\n");
            currentStaff = null;
        }
    }
}



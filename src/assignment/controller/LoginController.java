package assignment.controller;

import assignment.model.Staff;
import assignment.service.StaffService;
import assignment.util.ConsoleUtil;
import assignment.util.ValidationUtil;
import assignment.util.PasswordUtil;
import assignment.util.config.LoginConfig;
import assignment.view.LoginView;
import java.time.LocalDateTime;

/**
 * Controller for handling login functionality.
 * Handles menu presentation and delegates work to StaffService for business logic.
 */
public class LoginController {

    private final StaffService staffService;
    private final LoginView loginView;
    private Staff currentStaff;

    public LoginController(StaffService staffService) {
        this.staffService = staffService;
        this.loginView = new LoginView();
    }

    /**
     * Displays the login screen and handles user authentication.
     * @return The logged-in Staff object if successful, null otherwise
     */
    public Staff performLogin() {
        while (true) {
            ConsoleUtil.clearScreen();
            ConsoleUtil.logo();
            loginView.printLoginHeader();

            // Get IC
            System.out.print(LoginConfig.PROMPT_ENTER_IC);
            String ic = ValidationUtil.scanner.nextLine().trim();

            if (ic.isEmpty()) {
                loginView.printEmptyIcError();
                ConsoleUtil.systemPause();
                continue;
            }

            // Get Password (with masking)
            String password = PasswordUtil.readPassword(LoginConfig.PROMPT_ENTER_PASSWORD);

            if (password.isEmpty()) {
                loginView.printEmptyPasswordError();
                ConsoleUtil.systemPause();
                continue;
            }

            // Check if IC exists first for better error messages
            Staff foundByIc = staffService.findByIc(ic);

            // Attempt login
            Staff staff = staffService.login(ic, password);

            if (staff != null) {
                // Login successful
                LocalDateTime loginTime = LocalDateTime.now();
                loginView.printLoginSuccess(staff, loginTime);

                // Set clock-in time
                staff.setClockIn(loginTime);
                this.currentStaff = staff;

                ConsoleUtil.systemPause();
                return staff;
            } else {
                // Login failed - provide specific error message
                if (foundByIc == null) {
                    loginView.printLoginFailedIcNotFound();
                } else {
                    loginView.printLoginFailedIncorrectPassword();
                }

                loginView.printRetryOrExitPrompt();
                String choice = ValidationUtil.scanner.nextLine();

                if (choice.equalsIgnoreCase("E")) {
                    return null;
                }
            }
        }
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
            loginView.printLogoutSuccess(currentStaff, logoutTime);
            currentStaff = null;
        }
    }
}



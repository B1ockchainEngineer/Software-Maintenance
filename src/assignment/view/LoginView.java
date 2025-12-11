package assignment.view;

import assignment.model.Staff;
import assignment.util.config.AppConfig;
import assignment.util.config.LoginConfig;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * View class for Login functionality.
 * Handles all print outputs and display logic for login.
 */
public class LoginView {

    public void printLoginHeader() {
        System.out.println(LoginConfig.TITLE_LOGIN);
        System.out.println(AppConfig.SEPARATOR_LINE);
        System.out.println(LoginConfig.MSG_PLEASE_ENTER_CREDENTIALS);
        System.out.println(AppConfig.SEPARATOR_LINE);
        System.out.println();
    }

    public void printLoginSuccess(Staff staff, LocalDateTime loginTime) {
        System.out.println();
        System.out.println(AppConfig.SEPARATOR_LONG);
        System.out.println(LoginConfig.SuccessfulMessage.LOGIN_SUCCESSFUL);
        System.out.println(AppConfig.SEPARATOR_LONG);
        System.out.println(String.format(LoginConfig.MSG_WELCOME, staff.getName().toUpperCase()));
        System.out.println(String.format(LoginConfig.MSG_LOGIN_TIME, loginTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
        System.out.println(AppConfig.SEPARATOR_LONG);
        System.out.println();
    }

    public void printLoginFailedIcNotFound() {
        System.out.println();
        System.out.println(LoginConfig.ErrorMessage.LOGIN_FAILED);
        System.out.println(LoginConfig.ErrorMessage.CHECK_IC_NUMBER);
        System.out.println();
    }

    public void printLoginFailedIncorrectPassword() {
        System.out.println();
        System.out.println(LoginConfig.ErrorMessage.LOGIN_FAILED);
        System.out.println(LoginConfig.ErrorMessage.IC_EXISTS_PASSWORD_WRONG);
        System.out.println();
    }

    public void printRetryOrExitPrompt() {
        System.out.print(LoginConfig.PROMPT_RETRY_OR_EXIT);
    }

    public void printEmptyIcError() {
        System.out.println(LoginConfig.ErrorMessage.IC_CANNOT_BE_EMPTY);
    }

    public void printEmptyPasswordError() {
        System.out.println();
        System.out.println(LoginConfig.ErrorMessage.PASSWORD_CANNOT_BE_EMPTY);
    }

    public void printLogoutSuccess(Staff staff, LocalDateTime logoutTime) {
        System.out.println();
        System.out.println(AppConfig.SEPARATOR_LONG);
        System.out.println(LoginConfig.SuccessfulMessage.LOGOUT_SUCCESSFUL);
        System.out.println(AppConfig.SEPARATOR_LONG);
        System.out.println(String.format(LoginConfig.MSG_GOODBYE, staff.getName().toUpperCase()));
        System.out.println(String.format(LoginConfig.MSG_LOGOUT_TIME, logoutTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
        System.out.println(AppConfig.SEPARATOR_LONG);
        System.out.println();
    }
}


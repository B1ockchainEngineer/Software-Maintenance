package assignment.util;

import java.io.IOException;

public class ConsoleUtil {

    public static void logo() {
        System.out.println("  _____  _    ____     ____    _    _____ _____ ");
        System.out.println(" |_   _|/ \\  |  _ \\   / ___|  / \\  |  ___| ____|");
        System.out.println("   | | / _ \\ | |_) | | |     / _ \\ | |_  |  _|  ");
        System.out.println("   | |/ ___ \\|  _ <  | |___ / ___ \\|  _| | |___ ");
        System.out.println("   |_/_/   \\_\\_| \\_\\  \\____/_/   \\_\\_|   |_____|");
        System.out.println("                   .-=========-.");
        System.out.println("                   |  Welcome  |");
        System.out.println("                   |    to     |");
        System.out.println("                   |  TAR CAFE |");
        System.out.println("                   .-=========-.");
        System.out.println("-------------------------------------------------------");
    }

    public static void clearScreen() {
        try {
            // ANSI escape sequence: moves cursor home (\033[H) and clears screen (\033[2J)
            System.out.print("\033[H\033[2J");
            System.out.flush();
        } catch (Exception e) {
            // Fallback for non-ANSI terminals
            for (int i = 0; i < 50; ++i) System.out.println();
        }
    }

    public static void systemPause() {
        System.out.println("\n\t\tPress Enter to continue...");
        // Use Scanner to avoid buffering conflicts with other Scanner usage
        ValidationUtil.scanner.nextLine();
    }
}
package BLMain;//package BLMain;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private static final String LOG_FILE = "login_activity.txt";

/**This method upon succession either creates or appends a .txt file that will track username logins, success/fail and time
 * */
    public static void log(String username, boolean success) throws IOException {
        File logFile = new File(LOG_FILE);
        logFile.createNewFile();

        try (FileWriter fw = new FileWriter(LOG_FILE, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter pw = new PrintWriter(bw)) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            String timestamp = dtf.format(now);
            String status = success ? "successful" : "failed";
            String message = String.format("%s login attempt by user %s at %s", status, username, timestamp);
            pw.println(message);
            pw.flush();
        } catch (IOException e) {
            System.err.println("Error writing login log: " + e.getMessage());
        }
    }
}

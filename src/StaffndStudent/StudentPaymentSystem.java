package StaffndStudent;

import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;

public class StudentPaymentSystem {
    public static void main(String[] args) throws SQLException {
        Scanner users = new Scanner(System.in);
        System.out.println("Welcome to Office Payment System");
        String[] method = { "Cash on Hand", "Online Payment" };
        for (int i = 0; i <= 1; i++) {
            System.out.println(method[i]);
        }
        System.out.println();
        
        while (true) {
            System.out.print("Enter your Payment Method: ");
            String obj = users.nextLine();
            if (obj.equalsIgnoreCase("Online Payment")) {
                
                // Display Payments table to user
                String QUERY = "SELECT * FROM Payments";
                try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "postgresql");
                     Statement str = conn.createStatement();
                     ResultSet rs = str.executeQuery(QUERY)) {

                    while (rs.next()) {
                        System.out.println("User_id: " + rs.getInt("User_id"));
                        System.out.println("Account: " + rs.getString("Account"));
                    }

                    System.out.println("Enter the number 1 or 2 ");
                    int accountChoice = users.nextInt();
                    users.nextLine(); 

                    String account = "";
                    String accountQuery = "SELECT Account FROM Payments WHERE User_id = ?";
                    try (PreparedStatement pstmt = conn.prepareStatement(accountQuery)) {
                        pstmt.setInt(1, accountChoice);
                        try (ResultSet accountRs = pstmt.executeQuery()) {
                            if (accountRs.next()) {
                                account = accountRs.getString("Account");
                                System.out.println("Account: " + account);
                            } else {
                                System.out.println("Invalid account selection.");
                                continue;
                            }
                        }
                    }

                    System.out.println("Payment Method was Process");
                    System.out.print("Enter your Id: ");
                    int user_id = users.nextInt();
                    users.nextLine(); // Consume newline

                    System.out.print("Enter your User_name: ");
                    String user_name = users.nextLine();

                    System.out.print("Enter the date (YYYYMMDD): ");
                    String date = users.nextLine();
                   // users.nextLine(); 

                    System.out.print("Enter your Amount: ");
                    int amount = users.nextInt();
                    users.nextLine(); 

                    System.out.println("Generated Query:");
                    String insertQuery = "INSERT INTO student_Payments (user_id, user_name, staff_account, today_date, amount) VALUES (?, ?, ?, ?, ?)";
                    try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                        insertStmt.setInt(1, user_id);
                        insertStmt.setString(2, user_name);
                        insertStmt.setString(3, account);
                        insertStmt.setString(4, date);
                        insertStmt.setInt(5, amount);
                        
                        int rowsInserted = insertStmt.executeUpdate();
                        if (rowsInserted > 0) {
                            System.out.println("Payment was successfully processed.");
                        }
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }

            } else {
                System.out.println("Payment not found or details not available");
                System.out.println("If you want to exit, type \"exit\"");
                String exit = users.nextLine();
                if (exit.equalsIgnoreCase("exit")) {
                    break;
                }
            }
        }

        users.close();
    }
}

// // import java.util.*;

// // public class demo {

// // public static void main(String[] args) {
// // try {
// // Class.forName("com.mysql.cj.jdbc.Driver");
// // System.out.println("Driver loaded successfully ✅");
// // } catch (ClassNotFoundException e) {
// // System.out.println("Driver not found ❌: " + e.getMessage());
// // }
// // }

// // }

// import java.sql.*;
// import java.util.Scanner;
// import java.io.FileOutputStream;
// import com.itextpdf.text.*;
// import com.itextpdf.text.pdf.*;

// public class AdminPanel {
// private static Connection conn;
// static Scanner sc = new Scanner(System.in);

// public AdminPanel() {
// conn = DBConnection.getConnection();
// }

// // ================== EXISTING METHODS ==================
// static void viewAllUsers() {
// System.out.println("\n--- All Registered Users ---");
// String sql = "SELECT userID, user_name, email, role, status FROM user JOIN
// register USING(userID)";
// try (PreparedStatement ps = conn.prepareStatement(sql);
// ResultSet rs = ps.executeQuery()) {

// System.out.printf("%-10s %-20s %-25s %-12s %-10s%n",
// "UserID", "User Name", "Email", "Role", "Status");
// System.out.println("---------------------------------------------------------------------");

// while (rs.next()) {
// System.out.printf("%-10d %-20s %-25s %-12s %-10s%n",
// rs.getInt("userID"),
// rs.getString("user_name"),
// rs.getString("email"),
// rs.getString("role"),
// rs.getString("status"));
// }

// } catch (SQLException e) {
// System.out.println("SQL Error: " + e.getMessage());
// }
// }

// static void viewOverallRevenue() {
// String sql = "SELECT SUM(total_amt) AS totalRevenue FROM tickets WHERE status
// IN ('Confirmed','Used')";
// try (PreparedStatement ps = conn.prepareStatement(sql);
// ResultSet rs = ps.executeQuery()) {

// if (rs.next()) {
// double revenue = rs.getDouble("totalRevenue");
// System.out.println("💰 Total Platform Revenue: ₹" + revenue);
// } else {
// System.out.println("No revenue data available.");
// }

// } catch (SQLException e) {
// System.out.println("SQL Error: " + e.getMessage());
// }
// }

// static void blockUnblockOrganizer() {
// System.out.print("Enter Organizer UserID to block/unblock: ");
// int userId = sc.nextInt();

// try {
// String check = "SELECT status, role FROM user WHERE userID=?";
// PreparedStatement ps = conn.prepareStatement(check);
// ps.setInt(1, userId);
// ResultSet rs = ps.executeQuery();

// if (!rs.next()) {
// System.out.println("No user found with this ID!");
// return;
// }

// String role = rs.getString("role");
// if (!role.equalsIgnoreCase("organizer")) {
// System.out.println("This user is not an Organizer!");
// return;
// }

// String currentStatus = rs.getString("status");
// String newStatus = currentStatus.equalsIgnoreCase("Active") ? "Blocked" :
// "Active";

// String update = "UPDATE user SET status=? WHERE userID=?";
// PreparedStatement ps2 = conn.prepareStatement(update);
// ps2.setString(1, newStatus);
// ps2.setInt(2, userId);
// ps2.executeUpdate();

// System.out.println("Organizer status updated to: " + newStatus);

// } catch (SQLException e) {
// System.out.println("SQL Error: " + e.getMessage());
// }
// }

// static void viewTicketSalesSummary() {
// System.out.println("\n--- Ticket Sales Summary ---");
// String sql = "SELECT e.event_name, COUNT(t.ticket_id) AS totalTickets,
// SUM(t.total_amt) AS revenue " +
// "FROM events e LEFT JOIN tickets t ON e.event_id = t.event_id " +
// "GROUP BY e.event_id";

// try (PreparedStatement ps = conn.prepareStatement(sql);
// ResultSet rs = ps.executeQuery()) {

// System.out.printf("%-25s %-15s %-15s%n", "Event Name", "Tickets Sold",
// "Revenue (₹)");
// System.out.println("---------------------------------------------------------------");

// while (rs.next()) {
// System.out.printf("%-25s %-15d %-15.2f%n",
// rs.getString("event_name"),
// rs.getInt("totalTickets"),
// rs.getDouble("revenue"));
// }

// } catch (SQLException e) {
// System.out.println("SQL Error: " + e.getMessage());
// }
// }

// static void analyzeEventPopularity() {
// System.out.println("\n--- Event Popularity Report ---");
// String sql = """
// SELECT e.event_name,
// IFNULL(AVG(r.rating_value),0) AS avgRating,
// IFNULL(SUM(a.attended_count),0) AS totalAttended
// FROM events e
// LEFT JOIN rating r ON e.event_id = r.event_id
// LEFT JOIN attendance a ON e.event_id = a.event_id
// GROUP BY e.event_id
// ORDER BY avgRating DESC, totalAttended DESC
// LIMIT 10
// """;

// try (PreparedStatement ps = conn.prepareStatement(sql);
// ResultSet rs = ps.executeQuery()) {

// System.out.printf("%-25s %-15s %-15s%n", "Event Name", "Avg Rating", "Total
// Attendees");
// System.out.println("------------------------------------------------------------");

// while (rs.next()) {
// System.out.printf("%-25s %-15.2f %-15d%n",
// rs.getString("event_name"),
// rs.getDouble("avgRating"),
// rs.getInt("totalAttended"));
// }

// } catch (SQLException e) {
// System.out.println("SQL Error: " + e.getMessage());
// }
// }

// static void monitorSuspiciousActivity() {
// System.out.println("\n--- Suspicious Activity ---");
// String sql = """
// SELECT u.user_name, t.ticket_id, t.status, a.attended_count, a.absent_count
// FROM attendance a
// JOIN tickets t ON a.ticket_id = t.ticket_id
// JOIN user u ON a.userID = u.userID
// WHERE a.attended_count = 0 AND t.status = 'Used'
// """;

// try (PreparedStatement ps = conn.prepareStatement(sql);
// ResultSet rs = ps.executeQuery()) {

// boolean found = false;
// while (rs.next()) {
// found = true;
// System.out.println("⚠️ Suspicious: User " + rs.getString("user_name") +
// " | Ticket ID: " + rs.getInt("ticket_id") +
// " | Status: " + rs.getString("status") +
// " | Attended: " + rs.getInt("attended_count") +
// " | Absent: " + rs.getInt("absent_count"));
// }

// if (!found)
// System.out.println("✅ No suspicious activities found!");

// } catch (SQLException e) {
// System.out.println("SQL Error: " + e.getMessage());
// }
// }

// // ================== NEW FEATURE: PDF REPORT ==================
// static void generatePDFReport() {
// String pdfFile = "AdminEventReport.pdf";
// System.out.println("\nGenerating PDF Report...");

// try {
// Document document = new Document();
// PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
// document.open();

// document.add(new Paragraph("EVENT MANAGEMENT REPORT"));
// document.add(new Paragraph("Generated on: " + new java.util.Date()));
// document.add(new Paragraph(" "));

// // TOTAL REVENUE
// PreparedStatement ps1 = conn.prepareStatement(
// "SELECT SUM(t.total_amt) - IFNULL(SUM(r.refund_amt),0) AS total_revenue " +
// "FROM tickets t LEFT JOIN refunds r ON t.ticket_id = r.ticket_id " +
// "WHERE t.status='Confirmed'");
// ResultSet rs1 = ps1.executeQuery();
// if (rs1.next()) {
// document.add(new Paragraph("Total Revenue: ₹" +
// rs1.getDouble("total_revenue")));
// }

// // TOTAL TICKETS SOLD
// PreparedStatement ps2 = conn.prepareStatement(
// "SELECT SUM(quantity) AS total_tickets_sold FROM tickets WHERE
// status='Confirmed'");
// ResultSet rs2 = ps2.executeQuery();
// if (rs2.next()) {
// document.add(new Paragraph("Total Tickets Sold: " +
// rs2.getInt("total_tickets_sold")));
// }

// document.add(new Paragraph(" "));
// document.add(new Paragraph("Event Popularity (By Tickets Sold)"));
// document.add(new Paragraph("-------------------------------------"));

// // EVENT POPULARITY TABLE
// PreparedStatement ps3 = conn.prepareStatement(
// "SELECT e.event_name, SUM(t.quantity) AS tickets_sold, SUM(t.total_amt) AS
// revenue " +
// "FROM events e JOIN tickets t ON e.event_id = t.event_id " +
// "GROUP BY e.event_id ORDER BY tickets_sold DESC");
// ResultSet rs3 = ps3.executeQuery();

// PdfPTable table = new PdfPTable(3);
// table.addCell("Event Name");
// table.addCell("Tickets Sold");
// table.addCell("Revenue (₹)");

// while (rs3.next()) {
// table.addCell(rs3.getString("event_name"));
// table.addCell(String.valueOf(rs3.getInt("tickets_sold")));
// table.addCell(String.valueOf(rs3.getDouble("revenue")));
// }

// document.add(table);
// document.add(new Paragraph(" "));
// document.add(new Paragraph("End of Report."));

// document.close();
// System.out.println("✅ PDF Report generated successfully: " + pdfFile);

// } catch (Exception e) {
// System.out.println("❌ Error generating PDF: " + e.getMessage());
// e.printStackTrace();
// }
// }
// }

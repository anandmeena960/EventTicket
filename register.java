// import java.io.*;
// import java.sql.*;
// import java.time.LocalDate;
// import java.time.temporal.ChronoUnit;
// import java.util.Scanner;
// import java.util.regex.Pattern;

// class DBConnection {
// private static final String URL =
// "jdbc:mysql://127.0.0.1:3306/event_management?autoReconnect=true&useSSL=false&allowPublicKeyRetrieval=true";
// private static final String USER = "root";
// private static final String PASSWORD = "Anand";

// public static Connection getConnection() {
// Connection conn = null;
// try {
// Class.forName("com.mysql.cj.jdbc.Driver");
// conn = DriverManager.getConnection(URL, USER, PASSWORD);
// System.out.println("Database connected successfully!");

// } catch (ClassNotFoundException e) {
// System.out.println("JDBC Driver not found: " + e.getMessage());
// } catch (SQLException e) {
// System.out.println("SQL Error: " + e.getMessage());
// }
// return conn;
// }
// }

// class User {
// private static Connection conn = DBConnection.getConnection();
// private static final Scanner sc = new Scanner(System.in);
// private static final Pattern GMAIL_PATTERN =
// Pattern.compile("^[A-Za-z0-9._%+-]+@gmail\\.com$");
// public static int currentUserId = -1;

// public static void register() {
// try {
// if (conn == null || conn.isClosed()) {
// conn = DBConnection.getConnection();
// }
// } catch (SQLException e) {
// conn = DBConnection.getConnection();
// }
// String user_name;
// String email;
// String password;
// int age;
// String role;

// System.out.println("=== User Registration ===");

// System.out.print("1. Enter user name: ");
// user_name = sc.next().trim();
// System.out.print("2. Enter your age : ");
// age = sc.nextInt();
// if (age < 18) {
// System.out.println("You are not eligibale for attender OR organizer");
// register();
// }
// System.out.print("3. Ente your role ('attendee','organizer'): ");
// role = sc.next();

// // Email validation loop
// while (true) {
// System.out.print("4. Enter email (must end with @gmail.com): ");
// email = sc.next().trim();

// if (email.isEmpty()) {
// System.out.println(" Please enter an email address.");
// continue;
// }

// if (!GMAIL_PATTERN.matcher(email).matches()) {
// System.out.println(" Invalid email! Email must end with '@gmail.com' (e.g.,
// example@gmail.com).");
// continue;
// }
// break;
// }

// // Password validation loop
// while (true) {
// System.out.print("5. Enter password (minimum 8 characters, no spaces): ");
// password = sc.next();

// if (password.length() < 8 && !password.contains(" ")) {
// System.out.println(" Weak password! Please enter at least 8 characters.");
// continue;
// }
// System.out.println(" Password accepted!");
// break;

// }

// try {
// // Step 1️⃣: Check if user already exists (same email or password)
// String checkSql = "SELECT * FROM regsiter WHERE email = ? OR password = ?";
// PreparedStatement checkPs = conn.prepareStatement(checkSql);
// checkPs.setString(1, email);
// checkPs.setString(2, password);
// ResultSet rs = checkPs.executeQuery();

// if (rs.next()) {
// System.out.println(" User already exists! Please try to reregister or login
// .");
// register.main1();// direct login open
// } else {
// // Step 2️⃣: If not exist, insert new record
// String insertSql = "INSERT INTO regsiter (user_name, email, password,
// role,age) VALUES (?, ?, ?, ?,?)";
// PreparedStatement ps = conn.prepareStatement(insertSql);
// ps.setString(1, user_name);
// ps.setString(2, email);
// ps.setString(3, password);
// ps.setString(4, role);
// ps.setInt(5, age);

// int row = ps.executeUpdate();
// if (row > 0) {
// System.out.println("\n REGISTERED SUCCESSFULLY!");
// System.out.println("=========================");
// System.out.println("Now please login below with same email and password :");
// User.login3(); // call login after successful registration
// } else {
// System.out.println(" Registration failed.");
// }
// }

// } catch (SQLException e) {
// System.out.println("Database error: ");
// }
// }

// public static void login3() {
// try {
// if (conn == null || conn.isClosed()) {
// conn = DBConnection.getConnection();
// }
// } catch (SQLException e) {
// conn = DBConnection.getConnection();
// }
// String email;
// String password;
// while (true) {
// System.out.print("1. Enter email : ");
// email = sc.next().trim();

// if (email.isEmpty()) {
// System.out.println(" Please enter an email address.");
// continue;
// }

// if (!GMAIL_PATTERN.matcher(email).matches()) {
// System.out.println(" Invalid email! Email must end with '@gmail.com' (e.g.,
// example@gmail.com).");
// continue;
// }
// break;
// }

// // Password validation loop
// while (true) {
// System.out.print("2. Enter password (minimum 8 characters): ");
// password = sc.next();

// if (password.length() < 8 && !password.contains(" ")) {
// System.out.println("wrong password");
// continue;
// }
// break;
// }

// String sql = "SELECT * FROM regsiter WHERE email = ? AND password = ?";
// try (PreparedStatement ps = conn.prepareStatement(sql)) {
// ps.setString(1, email);
// ps.setString(2, password);

// ResultSet rs = ps.executeQuery();

// if (rs.next()) {
// System.out.println("LOGIN SUCCESSFUL!");
// System.out.println("=======================================================");
// int reg_id = rs.getInt("reg_id");
// System.out.println("REGISTER ID : " + rs.getInt("reg_id"));
// System.out.println("USER NAME : " + rs.getString("user_name"));
// System.out.println("EMAIL : " + rs.getString("email"));
// System.out.println("ROLE : " + rs.getString("role"));
// System.out.println("=======================================================");
// String role = rs.getString("role");
// // ✅ Step 1: Insert into login table
// String insertLogin = "INSERT INTO user (email, password, role) VALUES (?, ?,
// ?)";
// try (PreparedStatement loginPs = conn.prepareStatement(insertLogin,
// Statement.RETURN_GENERATED_KEYS)) {
// loginPs.setString(1, email);
// loginPs.setString(2, password);
// loginPs.setString(3, rs.getString("role"));

// int rows = loginPs.executeUpdate();
// ResultSet generatedKeys = loginPs.getGeneratedKeys();
// if (generatedKeys.next()) {
// int loginId = generatedKeys.getInt(1);
// currentUserId = loginId;
// System.out.println(" Your Session User ID: " + User.currentUserId);

// // ✅ Step 3: Update regsiter table with loginId
// String updateSql = "UPDATE regsiter SET userID = ? WHERE reg_id = ?";
// try (PreparedStatement updatePs = conn.prepareStatement(updateSql)) {
// updatePs.setInt(1, loginId);
// updatePs.setInt(2, reg_id);
// int updated = updatePs.executeUpdate();

// if (updated > 0) {
// System.out.println("login successfully");

// switch (role.toLowerCase()) {
// case "attendee":
// register.attendence();
// break;

// case "organizer":
// register.organizer();
// break;
// default:
// System.out.println(" Unknown role: " + role);
// }
// }
// }

// }
// }
// }

// else {
// System.out.println(" LOGIN FAILED — invalid email or password.");
// }

// } catch (SQLException e) {
// System.out.println("SQL ERROR (login): " + e.getMessage());
// return;
// }

// }

// public static void login() {
// System.out.println("\n======= LOGIN =======");
// String email;
// String password;
// String role;

// while (true) {
// System.out.print("1. Enter email (must end with @gmail.com): ");
// email = sc.next().trim();

// if (email.isEmpty()) {
// System.out.println(" Please enter an email address.");
// continue;
// }

// if (!GMAIL_PATTERN.matcher(email).matches()) {
// System.out.println(" Invalid email! Email must end with '@gmail.com' (e.g.,
// example@gmail.com).");
// continue;
// }
// break;

// }

// // Password validation loop
// while (true) {
// System.out.print("2. Enter password (minimum 8 characters): ");
// password = sc.next();

// if (password.length() < 8) {
// System.out.println(" Weak password! Please enter at least 8 characters.");
// continue;
// }
// break;
// }
// System.out.print("3. Ente your role ('attendee','organizer','staff','admin'):
// ");
// role = sc.next();

// String sql = "SELECT * FROM user WHERE email = ? AND password = ? AND
// role=?";

// try (PreparedStatement ps = conn.prepareStatement(sql)) {
// ps.setString(1, email);
// ps.setString(2, password);
// ps.setString(3, role);

// ResultSet rs = ps.executeQuery();

// if (rs.next()) {
// if (role.equalsIgnoreCase("organizer")) {
// try {
// String status = rs.getString("status"); // will work only if column exists
// if (status != null && status.equalsIgnoreCase("Blocked")) {
// System.out.println("\nYour account is currently BLOCKED by the admin.");
// System.out.println("Please contact support or the admin to get it
// unblocked.");
// return; // stop login
// }
// } catch (SQLException e) {
// // if column 'status' not found, ignore
// System.out.println("(Your are not blocked )");
// }
// }

// System.out.println("\n LOGIN SUCCESSFUL!");
// String findUserID = "SELECT userID FROM user WHERE email = ?";
// PreparedStatement ps2 = conn.prepareStatement(findUserID);
// ps2.setString(1, email);
// ResultSet rs2 = ps2.executeQuery();

// if (rs2.next()) {
// User.currentUserId = rs2.getInt("userID");
// System.out.println("Your User ID: " + User.currentUserId);
// System.out.println("=======================================================");
// }

// role = rs.getString("role");
// switch (role.toLowerCase()) {
// case "attendee":
// register.attendence();
// break;

// case "organizer":
// register.organizer();
// break;

// case "staff":
// register.staff();

// case "admin":
// register.adminMenu();
// break;

// default:
// System.out.println(" Unknown role: " + role);
// }
// } else {
// System.out.println("\n Either your Email or Password is incorrect,");
// System.out.println(" or you have not registered yet. Please register
// first.\n");
// System.out.println("Choose an option:");
// System.out.println("1. Try Login Again");
// System.out.println("2. Register");
// System.out.println("3. Back to register page");
// System.out.print("Enter choice: ");
// int choice = sc.nextInt();

// switch (choice) {
// case 1:
// login(); // opens login page again
// break;
// case 2:
// register(); // opens register page
// break;
// case 3:
// register(); // your main menu
// break;
// default:
// System.out.println("Invalid choice. Returning to main menu.");

// }
// }

// } catch (SQLException e) {
// System.out.println("SQL ERROR: " + e.getMessage());
// return;
// }
// }
// }

// class TicketIO {
// static void generateTicketFile(int ticketId, int eventId, int userId, double
// totalAmount) {
// String fileName = "Ticket_" + ticketId + ".txt";

// try (FileOutputStream fos = new FileOutputStream(fileName);
// PrintStream ps = new PrintStream(fos)) {

// ps.println("==================================");
// ps.println(" EVENT TICKET ");
// ps.println("==================================");
// ps.println("Ticket id : " + ticketId);
// ps.println("Event id : " + eventId);
// ps.println("User id : " + userId);
// ps.println("Total Pay : ₹" + totalAmount);
// ps.println("----------------------------------");
// ps.println("QR Data : TICKET#" + ticketId + "_EVENT#" + eventId + "_USER#" +
// userId);
// ps.println("Scan this QR info at entry gate.");
// ps.println("==================================");
// ps.println("Generate Date : " + java.time.LocalDate.now());
// ps.println("==================================");

// System.out.println("Ticket file saved : " + fileName);

// } catch (IOException e) {
// System.out.println("File write error: " + e.getMessage());
// }
// }

// public static void refundReceiptFile(int ticketId, double refundAmt) {
// String fileName = "Refund_" + ticketId + ".txt";

// try (FileOutputStream fos = new FileOutputStream(fileName);
// PrintWriter pw = new PrintWriter(fos)) {

// pw.println("==================================");
// pw.println(" REFUND RECEIPT");
// pw.println("==================================");
// pw.println("Ticket id : " + ticketId);
// pw.println("Refund date : " + java.time.LocalDate.now());
// pw.println("Refund amount: " + refundAmt);
// pw.println("Refund mode : bank ");
// pw.println("----------------------------------");
// pw.println("Processed by Event System");
// pw.println("==================================");

// System.out.println(" Refund file generate: " + fileName);

// } catch (IOException e) {
// System.out.println("File Error: " + e.getMessage());
// }
// }
// }

// class EventService {
// static Connection conn;
// static Scanner sc = new Scanner(System.in);

// public EventService() {
// conn = DBConnection.getConnection();
// }

// public static void createEvent() {

// System.out.println("=== Add New Event ===");

// System.out.print("Enter organiser Id :");
// String organizer_id = sc.nextLine();

// System.out.print("Enter event name :");
// String event_name = sc.nextLine();

// System.out.print("enter event cataegory :");
// String cataegory = sc.nextLine();

// System.out.print("enter event date :");
// String date = sc.nextLine();

// System.out.print("Enter event location :");
// String location = sc.nextLine();
// System.out.print("Enter capacity:");
// int capacity = sc.nextInt();
// System.out.print("Enter price: ");
// int price = sc.nextInt();
// sc.nextLine();

// conn = DBConnection.getConnection();
// String sql = "INSERT INTO events( organizer_id, event_name, category ,
// event_date, location, capacity, base_price,sold_ticket) VALUES
// (?,?,?,?,?,?,?,0)";
// try (PreparedStatement ps = conn.prepareStatement(sql)) {
// ps.setString(1, organizer_id);
// ps.setString(2, event_name);
// ps.setString(3, cataegory);
// ps.setString(4, date);
// ps.setString(5, location);
// ps.setInt(6, capacity);
// ps.setDouble(7, price);

// // ps.setString(8, Status);

// int rows = ps.executeUpdate();
// if (rows > 0)
// System.out.println(" Event added successfully!");
// else
// System.out.println(" Failed to add event.");
// } catch (SQLException ex) {
// System.out.println("SQL Error: " + ex.getMessage());
// }
// }

// public static void cancleEvent() {
// conn = DBConnection.getConnection();
// System.out.println("enter your event ID");
// int event_id = sc.nextInt();

// String sql = "UPDATE events Set status='cancelled' WHERE event_id=?";
// try (PreparedStatement ps = conn.prepareStatement(sql)) {
// ps.setInt(1, event_id);

// int rows = ps.executeUpdate();
// if (rows > 0)
// System.out.println("Event deleted successfully!");
// else
// System.out.println(" No event found with this ID.");
// } catch (SQLException ex) {
// System.out.println("SQL Error: " + ex.getMessage());
// }

// }

// static void updateEvent() {
// conn = DBConnection.getConnection();
// listOfEvents();
// System.out.println("To UPDATE EVENT:- ");
// System.out.println("Enter event id which you want to update :");
// int eventid = sc.nextInt();
// sc.nextLine();
// System.out.println("Enter new Event name");
// String newName = sc.nextLine();
// System.out.println("Enter new Event Date(yyyy-mm-dd)");
// String newDate = sc.nextLine();

// String sql = "UPDATE events SET event_name=?, event_date=? WHERE event_id=?";
// try (PreparedStatement ps = conn.prepareStatement(sql)) {
// ps.setString(1, newName);
// ps.setString(2, newDate);
// ps.setInt(3, eventid);

// int rows = ps.executeUpdate();
// if (rows > 0) {
// System.out.println(" Event updated successfully!");
// } else {
// System.out.println(" No event found with this ID.");
// }
// } catch (SQLException ex) {
// System.out.println("SQL Error: " + ex.getMessage());
// }
// }

// static void ticketsSales() {
// conn = DBConnection.getConnection();

// System.out.println("ENTER ANY EVENT_ID");
// int event_id = sc.nextInt();
// String sql = "SELECT e.event_id, e.capacity, e.sold_ticket AS
// sale,e.remaining_ticket AS rem " +
// "FROM events e " +
// "JOIN tickets t ON e.event_id = t.event_id " +
// "WHERE e.event_id = ? " +
// "GROUP BY e.event_id, e.capacity";

// try (PreparedStatement ps = conn.prepareStatement(sql)) {
// ps.setInt(1, event_id);

// try (ResultSet rs = ps.executeQuery()) {
// System.out.println("===================== Sales of Tickets
// ======================");
// System.out.println("-------------------------------------------------------------");
// System.out.printf("%-10s | %-20s | %-15s | %-15s%n",
// "Event_ID", "Capacity", "sold_ticket", "REM");
// System.out.println("-------------------------------------------------------------");

// if (rs.next()) {
// int id = rs.getInt("event_id");
// int capacity = rs.getInt("capacity");
// int sale = rs.getInt("sale");
// int rem = rs.getInt("rem");

// System.out.printf("%-10d | %-20d | %-15d | %-15d%n ", id, capacity, sale,
// rem);
// } else {
// System.out.println("No sales data found for the event ID " + event_id);
// }
// }
// System.out
// .println("-----------------------------------------------------");

// } catch (SQLException ex) {
// System.out.println("SQL Error: " + ex.getMessage());
// }

// }

// static void listOfEvents() {

// String sql = "SELECT event_id, event_name, category, event_date, location,
// base_price,status FROM events";

// try (Connection conn = DBConnection.getConnection();
// PreparedStatement ps = conn.prepareStatement(sql);
// ResultSet rs = ps.executeQuery()) {

// System.out
// .println(
// "========================================= LIST OF EVENTS
// =========================================");
// System.out
// .println(
// "--------------------------------------------------------------------------------------------------------------");

// System.out.printf("%-10s | %-20s | %-15s | %-12s | %-15s | %-15s | %-10s%n",
// "Event_ID", "Event_Name", "Category", "Event_Date", "Location", "Price",
// "status");
// System.out.println(
// "--------------------------------------------------------------------------------------------------------------");

// while (rs.next()) {
// int id = rs.getInt("event_id");
// String name = rs.getString("event_name");
// String category = rs.getString("category");
// String date = rs.getString("event_date");
// String location = rs.getString("location");
// double price = rs.getDouble("base_price");
// String status = rs.getString("status");

// System.out.printf("%-10d | %-20s | %-15s | %-12s | %-15s | %-10.2f |
// %-15s%n",
// id, name, category, date, location, price, status);
// }
// System.out
// .println(
// "--------------------------------------------------------------------------------------------------------------");

// } catch (SQLException ex) {
// System.out.println("SQL Error: " + ex.getMessage());
// }
// }

// static void listEventsByCategory() {
// Scanner sc = new Scanner(System.in);
// System.out.println("ENTER CATEGORY");
// String category = sc.next();
// String sql = "SELECT * FROM events WHERE category=?";
// try (Connection conn = DBConnection.getConnection();
// PreparedStatement ps = conn.prepareStatement(sql)) {

// ps.setString(1, category);
// ResultSet rs = ps.executeQuery();

// boolean found = false;
// while (rs.next()) {
// found = true;
// System.out.println(
// rs.getInt("event_id") + " | " +
// rs.getString("event_name") + " | " +
// rs.getString("category") + " | " +
// rs.getString("event_date") + " | " +
// rs.getString("location") + " | " +
// rs.getDouble("base_price") + " | " +
// rs.getString("status"));
// }

// if (!found)
// System.out.println("No events found in this category");

// } catch (SQLException ex) {
// System.out.println("SQL Error: " + ex.getMessage());
// }
// }

// static void listEventsByDate() {
// Scanner sc = new Scanner(System.in);
// System.out.println("eneter date");
// String date = sc.next();
// String sql = "SELECT * FROM events WHERE event_date=?";
// try (Connection conn = DBConnection.getConnection();
// PreparedStatement ps = conn.prepareStatement(sql)) {

// ps.setString(1, date);
// ResultSet rs = ps.executeQuery();

// boolean found = false;
// while (rs.next()) {
// found = true;
// System.out.println(
// rs.getInt("event_id") + " | " +
// rs.getString("event_name") + " | " +
// rs.getString("category") + " | " +
// rs.getString("event_date") + " | " +
// rs.getString("location") + " | ₹" +
// rs.getDouble("base_price") + " | " +
// rs.getString("status"));
// }

// if (!found)
// System.out.println("No events found on this date");

// } catch (SQLException ex) {
// System.out.println("SQL Error: " + ex.getMessage());
// }
// }

// static void listEventByLocation() {
// Scanner sc = new Scanner(System.in);
// System.out.println("ENTER LOCATION");
// String venue = sc.next();

// String sql = "select * FROM events WHERE location=?";
// try (Connection conn = DBConnection.getConnection();
// PreparedStatement ps = conn.prepareStatement(sql)) {

// ps.setString(1, venue);
// ResultSet rs = ps.executeQuery();

// boolean found = false;
// while (rs.next()) {
// found = true;
// System.out.println(
// rs.getInt("event_id") + " | " +
// rs.getString("event_name") + " | " +
// rs.getString("category") + " | " +
// rs.getString("event_date") + " | " +
// rs.getString("location") + " | ₹" +
// rs.getDouble("base_price") + " | " +
// rs.getString("status"));
// }
// if (!found)
// System.out.println("No events found on this location.");
// } catch (SQLException ex) {
// System.out.println("SQL Error: " + ex.getMessage());

// }

// }

// }

// class TicketService {
// private static Connection conn;
// static Scanner sc = new Scanner(System.in);

// public TicketService() {
// conn = DBConnection.getConnection();
// }

// public static void purchaseTicket() {
// EventService.listOfEvents();

// System.out.println("=== Ticket Counter ===");
// System.out.print("Enter Event ID: ");
// int event_id = sc.nextInt();
// System.out.print("Enter User ID: ");
// int user_id = sc.nextInt();
// System.out.print("Enter number of tickets (quantity): ");
// int quantity = sc.nextInt();
// sc.nextLine();

// conn = DBConnection.getConnection();

// try {
// // Check user role
// String sql = "SELECT role FROM user WHERE userID = ?";
// PreparedStatement psRole = conn.prepareStatement(sql);
// psRole.setInt(1, user_id);
// ResultSet rs = psRole.executeQuery();

// if (!rs.next()) {
// System.out.println("Invalid User ID!");
// return;
// }

// String userRole = rs.getString("role");
// if (!userRole.equalsIgnoreCase("attendee")) {
// System.out.println("Access Denied: Only attendees can purchase tickets");
// return;
// }

// // Get event details
// String priceQuery = "SELECT base_price, capacity, sold_ticket, event_date
// FROM events WHERE event_id = ?";
// PreparedStatement psPrice = conn.prepareStatement(priceQuery);
// psPrice.setInt(1, event_id);
// ResultSet rs1 = psPrice.executeQuery();

// if (!rs1.next()) {
// System.out.println("Invalid Event ID!");
// return;
// }

// double price = rs1.getDouble("base_price");
// int capacity = rs1.getInt("capacity");
// int sold_ticket = rs1.getInt("sold_ticket");
// LocalDate eventDate = rs1.getDate("event_date").toLocalDate();
// LocalDate today = LocalDate.now();

// // Price adjustments
// double ticketRatio = (double) sold_ticket / capacity;
// if (ticketRatio > 0.8) {
// price *= 1.8;
// System.out.println("High Demand Price increased 80%");
// }

// long daysLeft = ChronoUnit.DAYS.between(today, eventDate);
// if (daysLeft <= 2 && daysLeft >= 0) {
// price *= 1.5;
// System.out.println("Event is near. Price increased 50%!");
// } else {
// System.out.println("Normal Price.");
// }

// double totalAmount = price * quantity;

// System.out.println("--------------------------------------");
// System.out.println("Final Ticket Price : " + price);
// System.out.println("Quantity: " + quantity);
// System.out.println("Total Amount: " + totalAmount);
// System.out.println("--------------------------------------");

// // Insert ticket as Pending1

// String sqlInsert = "INSERT INTO tickets(event_id, userID, quantity,
// total_amt) VALUES (?, ?, ?, ?)";

// for (int i = 1; i <= quantity; i++) {
// try (PreparedStatement psInsert = conn.prepareStatement(sqlInsert,
// Statement.RETURN_GENERATED_KEYS)) {
// psInsert.setInt(1, event_id);
// psInsert.setInt(2, user_id);
// psInsert.setInt(3, 1);
// psInsert.setDouble(4, price);
// psInsert.executeUpdate();

// // Get generated ticket ID
// try (ResultSet rsKey = psInsert.getGeneratedKeys()) {
// if (rsKey.next()) {
// int ticketId = rsKey.getInt(1);
// System.out.println(" Ticket purchased successfully! Ticket ID: " + ticketId);
// }
// }
// }

// }
// } catch (SQLException ex) {
// System.out.println("SQL Error: " + ex.getMessage());
// }
// }

// static void approveBooking() {
// conn = DBConnection.getConnection();

// try {
// while (true) {
// // Show all pending tickets

// String pendingSql = "SELECT t.ticket_id, t.event_id, t.userID,
// t.quantity,t.total_amt, e.event_name AS event_name "
// + "FROM tickets t JOIN events e ON t.event_id = e.event_id WHERE
// t.status='Pending'";
// PreparedStatement psPending = conn.prepareStatement(pendingSql);
// ResultSet rsPending = psPending.executeQuery();

// boolean hasPending = false;
// System.out.println("=== Pending Ticket Requests ===");
// while (rsPending.next()) {
// hasPending = true;
// System.out.println("Ticket ID: " + rsPending.getInt("ticket_id") +
// ", Event: " + rsPending.getString("event_name") +
// ", User ID: " + rsPending.getInt("userID") +
// ", Quantity: " + rsPending.getInt("quantity") +
// ", Total Amount: " + rsPending.getDouble("total_amt"));
// }

// if (!hasPending) {
// System.out.println("No pending ticket requests.");
// return;
// }

// System.out.print("Enter Ticket ID to approve: ");
// int ticket_id = sc.nextInt();
// if (ticket_id == 0) {
// System.out.println("Exiting approval menu...");
// break;
// }

// String checkSql = "SELECT status, event_id,userID, quantity,total_amt FROM
// tickets WHERE ticket_id=?";
// PreparedStatement psCheck = conn.prepareStatement(checkSql);
// psCheck.setInt(1, ticket_id);
// ResultSet rsCheck = psCheck.executeQuery();

// if (!rsCheck.next()) {
// System.out.println("Invalid Ticket ID!");
// return;
// }

// String status = rsCheck.getString("status");
// int eventId = rsCheck.getInt("event_id");

// int quantity = rsCheck.getInt("quantity");
// double amount = rsCheck.getInt("total_amt");
// int userid = rsCheck.getInt("userID");

// if (!status.equalsIgnoreCase("Pending")) {
// System.out.println("Ticket is not pending approval");
// return;
// }
// // approver or cancel puchega
// System.out.println("Enter action cofirmed/cancelled : ");
// String status1 = sc.next().trim().toLowerCase();

// switch (status1) {

// // Approve ticket
// case "confirmed":
// String sql = "UPDATE tickets SET status='Confirmed' WHERE ticket_id=?";
// PreparedStatement ps = conn.prepareStatement(sql);
// ps.setInt(1, ticket_id);
// ps.executeUpdate();

// // Update sold_ticket in event table
// String sqlUpdate = "UPDATE events SET sold_ticket = sold_ticket + ? WHERE
// event_id=?";
// PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate);
// psUpdate.setInt(1, quantity);
// psUpdate.setInt(2, eventId);
// psUpdate.executeUpdate();
// TicketIO.generateTicketFile(ticket_id, eventId, userid, amount);

// System.out.println("Ticket Approved Successfully!");

// break;
// case "cancelled":
// String sql1 = "UPDATE tickets SET status='Cancelled' WHERE ticket_id=?";
// PreparedStatement ps1 = conn.prepareStatement(sql1);
// ps1.setInt(1, ticket_id);
// ps1.executeUpdate();

// // ye ticket table se amount le rahah ahi
// String amount1 = "SELECT total_amt FROM tickets WHERE ticket_id=?";
// PreparedStatement psAmt = conn.prepareStatement(amount1);
// psAmt.setInt(1, ticket_id);
// ResultSet rsAmt = psAmt.executeQuery();
// double refundamt = 0.0;
// if (rsAmt.next()) {
// refundamt = rsAmt.getDouble("total_amt");
// }
// // agar organmizer ticket cancel krta hai toh user ko pura paisa milna cha
// String refund = "INSERT INTO refunds(ticket_id, event_id, refund_amt,
// ref_date) VALUES (?, ?, ?, NOW())";
// PreparedStatement psRefund = conn.prepareStatement(refund);
// psRefund.setInt(1, ticket_id);
// psRefund.setInt(2, eventId);
// psRefund.setDouble(3, refundamt);
// psRefund.executeUpdate();

// System.out.println("ticket cancelled by organizer");
// System.out.println("Full refund of :" + refundamt);
// // Generate refund receipt file
// TicketIO.refundReceiptFile(ticket_id, refundamt);

// System.out.println("Ticket cancel Successfully!");
// break;
// }
// }

// } catch (SQLException e) {
// System.out.println("SQL Error: " + e.getMessage());
// }
// }

// static void canceltic() {
// conn = DBConnection.getConnection();
// System.out.print("Enter Ticket ID : ");
// int ticket_id = sc.nextInt();

// try {

// String sql = "SELECT t.event_id, t.total_amt,t.quantity, e.event_date AS
// event_date \n" + //
// "FROM tickets t\n" + //
// " JOIN events e\n" + //
// " ON t.event_id = e.event_id \n" + //
// " WHERE t.ticket_id = ?;";
// PreparedStatement ps1 = conn.prepareStatement(sql);
// ps1.setInt(1, ticket_id);
// ResultSet rs = ps1.executeQuery();

// if (!rs.next()) {
// System.out.println("ticket not found");

// return;
// }

// int eventId = rs.getInt("event_id");
// int quantity = rs.getInt("quantity");
// double totalAmount = rs.getDouble("total_amt");
// java.sql.Date eventDate = rs.getDate("event_date");

// // Calculate days
// java.time.LocalDate today = java.time.LocalDate.now();
// java.time.LocalDate eventDay = eventDate.toLocalDate();
// long days = java.time.temporal.ChronoUnit.DAYS.between(today, eventDay);
// //
// // refund
// double refund = totalAmount;
// if (days >= 10) {
// refund = totalAmount; // full
// } else if (days >= 5) {
// refund = totalAmount * 0.5; // half
// } else if (days <= 1) {
// refund = 0.0;
// }

// // Update ticket status to Cancelled
// String updateSql = "UPDATE tickets SET status = 'Cancelled' WHERE
// ticket_id=?";
// PreparedStatement ps2 = conn.prepareStatement(updateSql);
// ps2.setInt(1, ticket_id);
// int updated = ps2.executeUpdate();

// if (updated > 0) {
// System.out.println("tickret cnacel sucessfully");
// System.out.println("---------------------------------------");
// System.out.println("Event Date : " + eventDay);
// System.out.println("Days Before Event : " + days);
// System.out.println("Original Amount : " + totalAmount);
// System.out.println("Refund Amount : " + refund);
// System.out.println("---------------------------------------");

// // Insert into refund table
// String refundSql = "INSERT INTO refunds(ticket_id,
// event_id,refund_amt,ref_date) VALUES (?, ?, ?, NOW())";
// PreparedStatement ps3 = conn.prepareStatement(refundSql);
// ps3.setInt(1, ticket_id);
// ps3.setInt(2, eventId);
// ps3.setDouble(3, refund);
// ps3.executeUpdate();

// System.out.println("refund successfully");
// String sql1 = "update events set sold_ticket = sold_ticket - ? where
// event_id=?";
// PreparedStatement ps = conn.prepareStatement(sql1);
// ps.setInt(1, quantity);
// ps.setInt(2, eventId);
// ps.executeUpdate();

// System.out.println("remaing ticket incerase");
// } else {
// System.out.println("failled to cenxel ticket");
// }

// } catch (SQLException ex) {
// System.out.println("SQL Error: " + ex.getMessage());
// }
// }

// // static public boolean validateTicket(int eventId) {
// // Connection conn = DBConnection.getConnection();
// // boolean isValid = false;

// // try {
// // String query = "SELECT ticket_id, status FROM tickets event_id = ?";
// // PreparedStatement ps = conn.prepareStatement(query);

// // ps.setInt(1, eventId);

// // ResultSet rs = ps.executeQuery();

// // if (rs.next()) {
// // String status = rs.getString("status");
// // int ticketId = rs.getInt("ticket_id");

// // if (status.equals("Confirmed")) {
// // // Mark as checked-in
// // String insertLog = "INSERT INTO attendance_log(ticket_id, check_in_time)
// // VALUES (?, ?)";
// // PreparedStatement ps2 = conn.prepareStatement(insertLog);
// // ps2.setInt(1, ticketId);
// // ps2.executeUpdate();

// // // Update ticket status
// // String updateStatus = "UPDATE tickets SET status = 'CheckedIn' WHERE
// // ticket_id = ?";
// // PreparedStatement ps3 = conn.prepareStatement(updateStatus);
// // ps3.setInt(1, ticketId);
// // ps3.executeUpdate();

// // System.out.println("Ticket validated successfully. Entry allowed.");
// // isValid = true;
// // } else if (status.equals("CheckedIn")) {
// // System.out.println(" Ticket already used (duplicate scan). Entry
// denied.");
// // } else if (status.equals("Cancelled")) {
// // System.out.println(" Ticket cancelled. Entry denied.");
// // }
// // } else {
// // System.out.println(" Invalid or fake ticket. Not found in system.");
// // }

// // } catch (SQLException e) {
// // e.printStackTrace();
// // }

// // return isValid;
// // }
// }

// class rating {
// private static Connection conn;

// public rating() {
// conn = DBConnection.getConnection();
// }

// static Scanner sc = new Scanner(System.in);

// public static void collect() {
// System.out.println("========= ADD EVENT RATING =========");

// System.out.print("Do you want to give a rating? (yes/no): ");
// String choice = sc.next().trim();

// if (choice.equalsIgnoreCase("no")) {
// System.out.println("Returning to main menu...");
// return; // yahan se sidha main menu me wapas jayega
// }

// if (!choice.equalsIgnoreCase("yes")) {
// System.out.println("Invalid input! Please type 'yes' or 'no'. Returning to
// main menu...");
// return;
// }

// System.out.print("Enter Event ID: ");
// int event_id = sc.nextInt();

// System.out.print("ENTER USER ID: ");
// int userid = sc.nextInt();

// System.out.print("ENTER YOUR RATING 1-5: ");
// int rating_value = sc.nextInt();

// // rating validation
// if (rating_value < 1 || rating_value > 5) {
// System.out.println("Invalid rating! Please enter a value between 1 and 5.");
// return;
// }

// conn = DBConnection.getConnection();

// try {
// // Check role
// String sql = "SELECT role FROM user WHERE userID = ?";
// PreparedStatement ps = conn.prepareStatement(sql);
// ps.setInt(1, userid);
// ResultSet rs = ps.executeQuery();

// if (!rs.next()) {
// System.out.println("Invalid user id!");
// System.out.println("Your user id is: " + User.currentUserId);
// return;
// }

// String role = rs.getString("role");

// // only attendee rating
// if (!role.equalsIgnoreCase("attendee")) {
// System.out.println("Only Attendee can give rating!");
// return;
// }

// // check if already rated
// String sql1 = "SELECT * FROM rating WHERE event_id = ? AND user_id = ?";
// PreparedStatement ps1 = conn.prepareStatement(sql1);
// ps1.setInt(1, event_id);
// ps1.setInt(2, userid);
// ResultSet rs1 = ps1.executeQuery();

// if (rs1.next()) {
// System.out.println("You already rated this event!");
// System.out.println("(: ***T H A N K Y O U *** :)");
// return;
// }

// // Insert rating
// String sql2 = "INSERT INTO rating(event_id, user_id, rating_value) VALUES (?,
// ?, ?)";
// PreparedStatement ps2 = conn.prepareStatement(sql2);
// ps2.setInt(1, event_id);
// ps2.setInt(2, userid);
// ps2.setInt(3, rating_value);

// int rows = ps2.executeUpdate();

// if (rows > 0) {
// System.out.println("Rating submitted successfully!");
// System.out.println("(: ***T H A N K Y O U *** :)");
// } else {
// System.out.println("Failed to submit rating!");
// }

// } catch (SQLException ex) {
// System.out.println("SQL ERROR: " + ex.getMessage());
// }
// }

// public static void analyze() {
// conn = DBConnection.getConnection();
// System.out.println("===========ANALYZE EVENT RATING ============");
// System.out.println("ENTER event_id");
// int event_id = sc.nextInt();

// String sql = "select AVG(rating_value) as avg_rating ,count(*) as totalrating
// from rating where event_id=?";
// try (PreparedStatement ps = conn.prepareStatement(sql)) {
// ps.setInt(1, event_id);

// ResultSet rs = ps.executeQuery();

// if (rs.next()) {
// double avgRating = rs.getDouble("avg_rating");
// int totalrating = rs.getInt("totalrating");
// System.out.println("The avg rating : " + avgRating);

// if (totalrating > 0) {
// System.out.println("event id :" + event_id);
// System.out.println("kitne log ne rating dali:" + totalrating);
// } else {
// System.out.println("No ratings for this event");
// }
// } else {
// System.out.println("not rating");
// }

// } catch (SQLException e) {
// System.out.println("SQL ERROR " + e.getMessage());
// }
// }
// }

// class Attendae {
// static Connection conn = DBConnection.getConnection();
// static Scanner sc = new Scanner(System.in);

// public static void markAttendance() {
// System.out.println("====== MARK ATTENDANCE ======");
// System.out.print("Enter Ticket ID: ");
// int ticketId = sc.nextInt();

// try {
// String sql = "SELECT t.ticket_id, t.userID,
// t.event_id,t.quantity,t.status,r.user_name "
// + "FROM tickets t JOIN user u ON t.userID= u.userID join regsiter r on
// u.userID=r.userID WHERE t.ticket_id=?";
// PreparedStatement ps = conn.prepareStatement(sql);
// ps.setInt(1, ticketId);
// ResultSet rs = ps.executeQuery();

// if (!rs.next()) {
// System.out.println("Invalid Ticket ID!");
// return;
// }

// int eventId = rs.getInt("event_id");
// int userId = rs.getInt("userID");
// int total = rs.getInt("quantity");
// String status = rs.getString("status");
// String userName = rs.getString("user_name");

// if (!status.equalsIgnoreCase("Confirmed")) {
// System.out.println("ticket not confirmed ");
// return;
// }
// String fraudSql = "SELECT * FROM attendance WHERE ticket_id = ? AND
// attended_count > 0";
// PreparedStatement fraudPs = conn.prepareStatement(fraudSql);
// fraudPs.setInt(1, ticketId);
// ResultSet fraudRs = fraudPs.executeQuery();
// if (fraudRs.next()) {
// System.out.println("Duplicate/Fraud Detected: Ticket already checkedin");
// return;
// }
// // validate attendee identify
// System.out.println("user verfiyed :" + userName);
// System.out.println("user id : " + userId);
// System.out.println("total tickret purcahdd :" + total);

// // System.out.println("User: " + userName);
// // System.out.println("Total tickets purchased: " + total);
// System.out.print("ENTER 1 TO GIVE ATTENDANCE : ");
// int attended = sc.nextInt();

// if (attended < 0 || attended > total) {
// System.out.println("Invalid number of attendees");
// return;
// }

// int absent = total - attended;

// // Check if attendance already exists for this user & event
// String checkSql = "SELECT * FROM attendance WHERE ticket_id=? ";
// PreparedStatement checkPs = conn.prepareStatement(checkSql);
// checkPs.setInt(1, ticketId);
// // checkPs.setInt(2, eventId);
// ResultSet checkRs = checkPs.executeQuery();

// if (checkRs.next()) {
// // Already exists -> update totals

// String updateSql = "UPDATE attendance SET "
// + "total_booked = total_booked + ?, "
// + "attended_count = attended_count + ?, "
// + "absent_count = absent_count + ?, "
// + "check_in_time = NOW() "
// + "WHERE ticket_id = ?";
// PreparedStatement updatePs = conn.prepareStatement(updateSql);
// updatePs.setInt(1, total);
// updatePs.setInt(2, attended);
// updatePs.setInt(3, absent);
// updatePs.setInt(4, ticketId);
// // updatePs.setInt(5, eventId);
// updatePs.executeUpdate();

// System.out.println("Attendance updated successfully merged");
// } else {
// // No record exists -> insert new
// String insert = "INSERT INTO attendance(ticket_id,
// event_id,userID,total_booked, attended_count, absent_count) "
// +
// "VALUES (?, ?, ?, ?, ?, ?)";
// PreparedStatement ps2 = conn.prepareStatement(insert);
// ps2.setInt(1, ticketId);
// ps2.setInt(2, eventId);
// ps2.setInt(3, userId);
// ps2.setInt(4, total);
// ps2.setInt(5, attended);
// ps2.setInt(6, absent);
// ps2.executeUpdate();

// System.out.println("Attendance recorded successfully");
// }
// // ye check krega user ne event je andar enetry kr li

// checkIn_checkOut();

// } catch (SQLException e) {
// System.out.println("SQL Error: " + e.getMessage());
// }
// }

// public static void viewAttendanceReport() {
// System.out.println("========= ATTENDANCE REPORT =========");
// System.out.print("ENTER EVENT_ID TO VIEW ATTENDANCE REPORT: ");
// int eventId = sc.nextInt();

// String sql = "SELECT r.user_name,a.ticket_id,a.total_booked,a.attended_count,
// a.absent_count " +
// "FROM attendance a " +
// "JOIN regsiter r ON a.userID= r.userID " +
// "JOIN tickets t ON a.ticket_id = t.ticket_id " +
// "WHERE a.event_id = ? AND t.status IN ('Confirmed','Used')";

// try (PreparedStatement ps = conn.prepareStatement(sql)) {
// ps.setInt(1, eventId);
// ResultSet rs = ps.executeQuery();

// // System.out.printf("%-15s | %-10s | %-10s | %-10s | %-10s%n",
// // "User Name", "Ticket Id", "Booked", "Attended", "Absent");
// //
// System.out.println("--------------------------------------------------------------");

// int totalbook = 0, totalattended = 0, totalabsent = 0;
// while (rs.next()) {
// String name = rs.getString("user_name");
// int ticketId = rs.getInt("ticket_id");
// int booked = rs.getInt("total_booked");
// int attended = rs.getInt("attended_count");
// int absent = rs.getInt("absent_count");

// totalbook += booked;
// totalattended += attended;
// totalabsent += absent;
// System.out.println("======================================================================");
// System.out.printf("| %-20s | %-10s | %-10s | %-10s | %-10s |\n",
// "User Name", "Ticket ID", "Booked", "Attended", "Absent");
// System.out.println("======================================================================");

// // Table Rows
// System.out.printf("| %-20s | %-10d | %-10d | %-10d | %-10d |\n",
// name, ticketId, booked, attended, absent);
// System.out.println("--------------------------------------------------------------");
// }
// // After all users printed
// System.out.println();
// System.out.println("=================== SUMMARY ===================");
// System.out.printf("| %-20s | %-10d |\n", "Total Tickets Sold", totalbook);
// System.out.printf("| %-20s | %-10d |\n", "Total Attended", totalattended);
// System.out.printf("| %-20s | %-10d |\n", "Total Absent", totalabsent);
// System.out.println("===============================================");

// } catch (SQLException e) {
// System.out.println("SQL Error: " + e.getMessage());
// }
// }

// public static void validateTicketFromFile() {
// Scanner sc = new Scanner(System.in);
// System.out.print("Enter ticket file name (example: Ticket_1.txt): ");
// String fileName = sc.nextLine();

// try (FileInputStream fis = new FileInputStream(fileName);
// BufferedReader br = new BufferedReader(new InputStreamReader(fis))) {

// String line;
// String qrData = null;

// // read QR data from file
// while ((line = br.readLine()) != null) {
// if (line.startsWith("QR Data")) {
// qrData = line.substring(line.indexOf(":") + 1).trim();
// break;
// }
// }

// if (qrData == null) {
// System.out.println("No QR data found in file!");
// return;
// }

// // Extract ticket_id from QR string like "Ticket#5_Event#3_User#2"
// String[] parts = qrData.split("_");
// if (parts.length == 0) {
// System.out.println("Invalid QR format!");
// return;
// }

// String[] ticketPart = parts[0].split("#");
// if (ticketPart.length < 2) {
// System.out.println("Invalid Ticket format in QR data!");
// return;
// }
// int ticketId = Integer.parseInt(ticketPart[1]);

// conn = DBConnection.getConnection();
// String sql = "SELECT status FROM tickets WHERE ticket_id=?";
// PreparedStatement ps = conn.prepareStatement(sql);
// ps.setInt(1, ticketId);
// ResultSet rs = ps.executeQuery();

// if (rs.next()) {
// String status = rs.getString("status");

// if (status.equalsIgnoreCase("Confirmed")) {
// System.out.println("Valid Ticket! Entry Allowed.");

// // Update ticket as used
// PreparedStatement ps2 = conn.prepareStatement(
// "UPDATE tickets SET status='Used' WHERE ticket_id=?");
// ps2.setInt(1, ticketId);
// ps2.executeUpdate();

// // call attendance marking directly
// markAttendanceByTicketId(ticketId);

// } else {
// System.out.println("Ticket not valid for entry! Status: " + status);
// }
// } else {
// System.out.println("Ticket not found in database!");
// }

// } catch (IOException | SQLException e) {
// System.out.println("Error: " + e.getMessage());
// }
// }

// private static void markAttendanceByTicketId(int ticketId) {
// try {
// String sql = "SELECT t.ticket_id, t.userID, t.event_id, t.quantity, t.status,
// r.user_name "
// + "FROM tickets t JOIN user u ON t.userID = u.userID join regsiter r on
// r.userID=u.userID WHERE t.ticket_id=?";
// PreparedStatement ps = conn.prepareStatement(sql);
// ps.setInt(1, ticketId);
// ResultSet rs = ps.executeQuery();

// if (!rs.next()) {
// System.out.println("Invalid Ticket ID!");
// return;
// }

// int eventId = rs.getInt("event_id");
// int userId = rs.getInt("userID");
// int total = rs.getInt("quantity");
// String userName = rs.getString("user_name");

// System.out.println("User verified: " + userName);
// System.out.println("Ticket ID: " + ticketId);
// System.out.println("Event ID: " + eventId);
// System.out.println("Total Tickets: " + total);

// // For QR entry assume all attended
// int attended = total;
// int absent = total - attended;

// // Insert attendance directly
// String insert = "INSERT INTO attendance(ticket_id, event_id, userID,
// total_booked, attended_count, absent_count, check_in_time) "
// + "VALUES (?, ?, ?, ?, ?, ?, NOW())";
// PreparedStatement ps2 = conn.prepareStatement(insert);
// ps2.setInt(1, ticketId);
// ps2.setInt(2, eventId);
// ps2.setInt(3, userId);
// ps2.setInt(4, total);
// ps2.setInt(5, attended);
// ps2.setInt(6, absent);
// ps2.executeUpdate();

// System.out.println("Attendance recorded automatically for QR scan!");

// } catch (SQLException e) {
// System.out.println("SQL Error: " + e.getMessage());
// }
// }

// static void checkIn_checkOut() {
// conn = DBConnection.getConnection(); // DB connection
// try {

// System.out.print("Enter Ticket ID: ");
// int ticketId = sc.nextInt();
// String updateTicketSql = "UPDATE tickets SET status = 'Used' WHERE ticket_id
// = ?";
// PreparedStatement ticketPs = conn.prepareStatement(updateTicketSql);
// ticketPs.setInt(1, ticketId);
// ticketPs.executeUpdate();
// System.out.println("Ticket status updated to 'Used'.");

// // Step 1: check ticket status from tickets table
// String checkStatusSql = "SELECT status FROM tickets WHERE ticket_id = ?";
// PreparedStatement psCheck = conn.prepareStatement(checkStatusSql);
// psCheck.setInt(1, ticketId);
// ResultSet rs = psCheck.executeQuery();

// if (!rs.next()) {
// System.out.println("Invalid Ticket ID!");
// return;
// }

// String ticketStatus = rs.getString("status");

// // Only confirmed tickets can check in/out
// if (!ticketStatus.equalsIgnoreCase("used")) {
// System.out.println(" Ticket is not confirmed! Only confirmed tickets can
// check in or out.");
// return;
// }

// // Step 2: ask user for check-in or check-out
// System.out.print("Mark attendee as checked in now? (yes for Check-In / no for
// Check-Out): ");
// String choice = sc.next().trim().toLowerCase();

// if (choice.equalsIgnoreCase("yes")) {
// String inSql = "UPDATE attendance SET check_in_time = NOW(), status =
// 'Checked-In' WHERE ticket_id = ?";
// PreparedStatement psIn = conn.prepareStatement(inSql);
// psIn.setInt(1, ticketId);
// int rows = psIn.executeUpdate();

// if (rows > 0)
// System.out.println(" Attendee checked in successfully!");
// else
// System.out.println(" Attendance record not found!");
// } else if (choice.equalsIgnoreCase("no")) {
// String outSql = "UPDATE attendance SET check_out_time = NOW(), status =
// 'Checked-Out' WHERE ticket_id = ?";
// PreparedStatement psOut = conn.prepareStatement(outSql);
// psOut.setInt(1, ticketId);
// int rows = psOut.executeUpdate();

// if (rows > 0) {
// System.out.println(" Attendee checked out successfully!");
// rating.collect();
// } else
// System.out.println(" Attendance record not found!");
// } else {
// System.out.println(" Invalid input! Please enter 'yes' or 'no'.");
// }

// } catch (Exception e) {
// e.printStackTrace();
// }
// }

// }

// class AdminPanel {
// private static Connection conn = DBConnection.getConnection();

// static Scanner sc = new Scanner(System.in);

// static void viewAllUsers() {
// System.out.println("\n--- All Registered Users ---");
// String sql = "SELECT u.userID,r.user_name, u.email, r.role, u.status FROM
// user u JOIN regsiter r on r.userID=u.userID";
// try (PreparedStatement ps = conn.prepareStatement(sql);
// ResultSet rs = ps.executeQuery()) {
// System.out.println("--------------------------------------------------------------------------------");

// System.out.printf("%-10s %-20s %-25s %-12s %-10s%n",
// "UserID", "User Name", "Email", "Role", "Status");
// System.out.println("--------------------------------------------------------------------------------");

// while (rs.next()) {
// System.out.printf("%-10d %-20s %-25s %-12s %-10s%n",
// rs.getInt("userID"),
// rs.getString("user_name"),
// rs.getString("email"),
// rs.getString("role"),
// rs.getString("status"));
// System.out.println("-------------------------------------------------------------------------------");

// }

// } catch (SQLException e) {
// System.out.println("SQL Error: " + e.getMessage());
// }
// }

// static void viewOverallRevenue() {
// String sql = "SELECT SUM(total_amt) AS totalRevenue FROM tickets WHERE status
// IN ('Confirmed','Used')";

// // String sql = "SELECT SUM(t.total_amt) AS totalRevenue " +
// // "FROM tickets t " +
// // "JOIN events e ON t.event_id = e.event_id " +
// // "WHERE t.status IN ('Confirmed', 'Used')";

// try (PreparedStatement ps = conn.prepareStatement(sql);
// ResultSet rs = ps.executeQuery()) {

// if (rs.next()) {
// double revenue = rs.getDouble("totalRevenue");
// System.out.println(" Total Platform Revenue: $" + revenue);
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
// // String sql = "SELECT e.event_name, COUNT(t.ticket_id) AS totalTickets,
// // SUM(t.total_amt) AS revenue " +
// // "FROM events e LEFT JOIN tickets t ON e.event_id = t.event_id " +
// // "GROUP BY e.event_id";
// String sql = "SELECT e.event_name,COUNT(t.ticket_id) AS totalTickets,
// SUM(t.total_amt) AS revenue " +
// "FROM events e " +
// "JOIN tickets t ON e.event_id = t.event_id " +
// "WHERE t.status IN ('Confirmed', 'Used')" +
// "GROUP BY e.event_id";

// try (PreparedStatement ps = conn.prepareStatement(sql);
// ResultSet rs = ps.executeQuery()) {

// System.out.printf("%-25s %-15s %-15s%n", "Event Name", "Tickets Sold",
// "Revenue ($)");
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

// }

// class InvalidChoiceException extends Exception {
// public InvalidChoiceException(String message) {
// super(message);
// }
// }

// public class register {

// private static final Scanner sc = new Scanner(System.in);

// public static void main(String[] args) {
// register.main1();
// }

// static void main1() {
// while (true) {
// try {
// System.out.println("--------------------------------------------");
// System.out.println("==================WELCOME===================");
// System.out.println("--------------------------------------------");
// System.out.println("1. Register/Login for Attender & Organizer");
// System.out.println("2. Login AS Staff");
// System.out.println("3. Login AS Admin ");
// System.out.println("--------------------------------------------");
// System.out.print("Enter choice NO. : ");
// if (!sc.hasNextInt()) {
// sc.next();
// throw new InvalidChoiceException(" Invalid input! Please enter a number
// between 1-3.");
// }
// int ch = sc.nextInt();
// switch (ch) {
// case 1:
// registerLog();

// break;
// case 2:
// User.login();

// break;
// case 3:

// User.login();
// break;
// default:
// throw new InvalidChoiceException("Invalid choice! Please select from 1-3.");

// }
// } catch (InvalidChoiceException e) {
// System.out.println(e.getMessage());
// } catch (Exception e) {
// System.out.println(" Unexpected error! Please try again.");
// sc.nextLine(); // clear buffer
// }
// }
// }

// public static void registerLog() {
// try {
// System.out.println("1.REGISTERE");
// System.out.println("2.LOGIN");
// System.out.println("3.Exit");

// System.out.print("ENTER ANY NUMBER : ");
// if (!sc.hasNextInt()) {
// sc.next();
// throw new InvalidChoiceException(" Please enter 1, 2, or 3 only!");
// }
// int ch = sc.nextInt();

// if (ch == 1) {
// User.register();

// }
// if (ch == 2) {
// User.login();
// }
// if (ch == 3) {
// main1();
// }
// } catch (InvalidChoiceException e) {
// System.out.println(e.getMessage());
// registerLog();
// } catch (Exception e) {
// System.out.println(" Something went wrong. Please try again.");
// registerLog();
// }

// }

// public static void attendence() {

// int choice;
// boolean t = true;

// while (t) {
// try {
// System.out.println("======Attender menu======");
// System.out.println("1.Browose event");
// System.out.println("2.Purchase ");
// System.out.println("3.Cancelled");

// System.out.println("4.Exit");
// System.out.print("ENTER YOUR CHOICE : ");
// if (!sc.hasNextInt()) {
// sc.next();
// throw new InvalidChoiceException(" Please enter a valid number .");
// }
// choice = sc.nextInt();

// switch (choice) {

// case 1:
// EventService.listOfEvents();
// boolean t1 = true;
// while (t1) {
// System.out.println(("visit events by :-"));
// System.out.println("1.Categeroy");
// System.out.println("2.Date");
// System.out.println("3.Location");
// System.out.println("4.Exit");
// if (!sc.hasNextInt()) {
// sc.next();
// throw new InvalidChoiceException(" Please enter valid no. only!");
// }

// int c = sc.nextInt();
// switch (c) {
// case 1:
// EventService.listEventsByCategory();
// break;

// case 2:
// EventService.listEventsByDate();
// break;

// case 3:
// EventService.listEventByLocation();
// break;
// case 4:
// t1 = false;
// break;
// default:
// System.out.print("enter valid choice : ");
// sc.close();
// }

// }
// break;
// case 2:
// TicketService.purchaseTicket();
// break;

// case 3:
// TicketService.canceltic();
// break;
// case 4:
// t = false;
// break;

// default:
// System.out.println("invalid choice ");
// sc.close();
// }
// } catch (InvalidChoiceException e) {
// System.out.println(e.getMessage());
// } catch (Exception e) {
// System.out.println(" Error occurred. Please try again.");
// sc.nextLine();
// }
// }
// register.main1();

// }

// public static void organizer() {

// int choice;
// boolean t = true;
// while (t) {
// try {
// System.out.println("======Organizer Menu======");

// System.out.println("1.CREATE EVENT");
// System.out.println("2.UPDATE EVENT ");
// System.out.println("3.CANCLE EVENT");
// System.out.println("4.VIEW TICKET SALES");
// System.out.println("5.RATING ");
// System.out.println("6.VIEW ATTENDANCE");
// System.out.println("7.Approved/Cancel Ticket");
// System.out.println("8.Exit");
// System.out.print("ENTER YOUR CHOICE : ");
// if (!sc.hasNextInt()) {
// sc.next();
// throw new InvalidChoiceException(" Please enter a valid number .");
// }
// choice = sc.nextInt();

// switch (choice) {
// case 1:
// EventService.createEvent();
// break;

// case 2:
// EventService.updateEvent();
// break;
// case 3:
// EventService.cancleEvent();

// break;
// case 4:
// EventService.ticketsSales();
// break;

// case 5:
// rating.analyze();

// case 6:
// Attendae.viewAttendanceReport();

// break;
// case 7:
// TicketService.approveBooking();
// break;
// case 8:
// t = false;

// default:
// System.out.println("Please enter choice from above options !");

// break;

// }
// } catch (InvalidChoiceException e) {
// System.out.println(e.getMessage());
// } catch (Exception e) {
// System.out.println(" Something went wrong. Please try again.");
// sc.nextLine();
// }
// }
// register.main1();
// }

// public static void staff() {

// Scanner sc = new Scanner(System.in);

// // Predefined staff username and password
// String STAFF_USERNAME = "staff123";
// String STAFF_PASSWORD = "staff@2025";
// System.out.print("Enter Staff Username: ");
// String username = sc.next();
// System.out.print("Enter Staff Password: ");
// String password = sc.next();

// if (username.equals(STAFF_USERNAME) && password.equals(STAFF_PASSWORD)) {
// System.out.println("Staff Login Successful!");
// boolean t = true;
// while (t) {
// try {
// System.out.println("========Staff menu========");
// System.out.println("1. Mark Attendeance ");
// System.out.println("2. Valid Ticket from Files");
// System.out.println("3.checkin/checkout");
// System.out.println("4.Exit");
// System.out.print("ENTER YOUR CHOICE : ");
// if (!sc.hasNextInt()) {
// sc.next();
// throw new InvalidChoiceException(" Please enter valid number.");
// }
// int choice = sc.nextInt();

// switch (choice) {
// case 1:
// Attendae.markAttendance();

// break;

// case 2:
// Attendae.validateTicketFromFile();
// break;
// case 3:
// Attendae.checkIn_checkOut();
// break;

// case 4:
// t = false;
// break;
// }
// } catch (InvalidChoiceException e) {
// System.out.println(e.getMessage());
// } catch (Exception e) {
// System.out.println("Error! Please try again.");
// sc.nextLine();
// }

// }

// } else {
// System.out.println(" Invalid Staff Credentials!");
// System.out.println("\n Either your user_name or Password is incorrect,");
// System.out.println(" Please enter again.\n");
// staff();

// }
// register.main1();
// }

// public static void adminMenu() {
// boolean running = true;

// while (running) {
// try {
// System.out.println("\n======== ADMIN DASHBOARD ========");
// System.out.println("1. View All Users");
// System.out.println("2. Block or Unblock Organizer");
// System.out.println("3. View Overall Revenue");
// System.out.println("4. View Ticket Sales Summary");
// System.out.println("5. Analyze Event Popularity");

// System.out.println("6. Exit to Main Menu");
// System.out.print("Enter your choice: ");
// if (!sc.hasNextInt()) {
// sc.next();
// throw new InvalidChoiceException(" Please enter a valid number .");
// }

// int choice = sc.nextInt();

// switch (choice) {
// case 1:
// AdminPanel.viewAllUsers();
// break;
// case 2:
// AdminPanel.blockUnblockOrganizer();
// break;
// case 3:
// AdminPanel.viewOverallRevenue();
// break;
// case 4:
// AdminPanel.viewTicketSalesSummary();
// break;
// case 5:
// AdminPanel.analyzeEventPopularity();
// break;

// case 6:
// running = false;
// break;

// default:
// System.out.println("Invalid choice! Please try again.");
// }
// } catch (InvalidChoiceException e) {
// System.out.println(e.getMessage());
// } catch (Exception e) {
// System.out.println(" Something went wrong. Try again.");
// sc.nextLine();

// }
// }
// register.main1();

// }
// }

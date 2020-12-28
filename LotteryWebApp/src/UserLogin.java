import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;


@WebServlet("/UserLogin")
public class UserLogin extends HttpServlet {

    private Connection conn;
    private Statement stmt;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
        String USER = "root";
        String PASS = "password";

        // URLs to connect to database depending on your development approach
        // (NOTE: please change to option 1 when submitting)

        // 1. use this when running everything in Docker using docker-compose
        String DB_URL = "jdbc:mysql://db:3306/lottery";

        // 2. use this when running tomcat server locally on your machine and mysql database server in Docker
        //String DB_URL = "jdbc:mysql://localhost:33333/lottery";

        // 3. use this when running tomcat and mysql database servers on your machine
        //String DB_URL = "jdbc:mysql://localhost:3306/test2";

        try {
            // create database connection and statement
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();

            //delete old user data
            HttpSession session = request.getSession();
            session.removeAttribute("firstname");
            session.removeAttribute("lastname");
            session.removeAttribute("username");
            session.removeAttribute("email");
            session.removeAttribute("password");

            //determine if user is allowed to attempt login
            if (session.getAttribute("attempts") == null) {
                session.setAttribute("attempts", 3);
            }
            if ((int)session.getAttribute("attempts") == 0){
                RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
                request.setAttribute("message", "Login Unsuccessful, " + session.getAttribute("attempts") + " further login attempts blocked");
                dispatcher.forward(request, response);
            }
            else {

                String username = request.getParameter("username");
                String password = request.getParameter("password");

                // query database and get results
                ResultSet rs = stmt.executeQuery("SELECT * FROM userAccounts WHERE Username = '" + username + "'");


                if (rs.next()) {
                    //only login if correct password is entered
                    if (HashPassword(password).equals(rs.getString("Pwd"))) {
                        session.setAttribute("firstname", rs.getString("Firstname"));
                        session.setAttribute("lastname", rs.getString("Lastname"));
                        session.setAttribute("username", rs.getString("Username"));
                        session.setAttribute("email", rs.getString("Email"));
                        session.setAttribute("password", rs.getString("Pwd"));
                        session.setAttribute("admin", rs.getString("Role"));
                        session.setAttribute("attempts", 3);

                        if (session.getAttribute("admin").equals("admin")) {
                            rs = stmt.executeQuery("SELECT Firstname, Lastname, Email, Phone, Username, Role FROM userAccounts");
                            // create HTML table text
                            String content = "<table border='1' cellspacing='2' cellpadding='2' width='100%' align='left'>" +
                                    "<tr><th>First name</th><th>Last name</th><th>Email</th><th>Phone number</th><th>Username</th><th>Role</th></tr>";

                            // add HTML table data using data from database
                            while (rs.next()) {
                                content += "<tr><td>"+ rs.getString("Firstname") + "</td>" +
                                        "<td>" + rs.getString("Lastname") + "</td>" +
                                        "<td>" + rs.getString("Email") + "</td>" +
                                        "<td>" + rs.getString("Phone") + "</td>" +
                                        "<td>" + rs.getString("Username") + "</td>" +
                                        "<td>" + rs.getString("Role") + "</td></tr>";
                            }
                            // finish HTML table text
                            content += "</table>";

                            // close connection
                            conn.close();

                            // display output.jsp page with given content above if successful
                            RequestDispatcher dispatcher = request.getRequestDispatcher("/admin/admin_home.jsp");
                            request.setAttribute("message", "Login Success");
                            request.setAttribute("data", content);
                            dispatcher.forward(request, response);
                        }
                        else{
                            RequestDispatcher dispatcher = request.getRequestDispatcher("/account.jsp");
                            request.setAttribute("message", "Login Success");
                            dispatcher.forward(request, response);
                        }
                    } else {
                        session.setAttribute("attempts", (int) session.getAttribute("attempts") - 1);
                        RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
                        request.setAttribute("message", "Login Unsuccessful, " + session.getAttribute("attempts") + " attempts remaining");
                        dispatcher.forward(request, response);
                    }
                } else {
                    session.setAttribute("attempts", (int) session.getAttribute("attempts") - 1);
                    RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
                    request.setAttribute("message", "Login Unsuccessful, " + session.getAttribute("attempts") + "attempts remaining");
                    dispatcher.forward(request, response);
                }
            }
        } catch (Exception se) {
            se.printStackTrace();
            // display error.jsp page with given message if successful
            RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
            request.setAttribute("message", "Database Error, Please try again");
            dispatcher.forward(request, response);
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    public String HashPassword(String inputPassword){
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] bytes = md.digest(inputPassword.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++){
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}

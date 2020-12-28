import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.Scanner;

@WebServlet("/CheckWin")
public class CheckWin extends HttpServlet {

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

            //get the winning draw from the database
            HttpSession session = request.getSession();
            String numbers;
            Boolean won = false;
            ResultSet rs = stmt.executeQuery("SELECT draw FROM winningdraw");
            rs.next();
            numbers = rs.getString("draw");

            //delete the existing draws
            File Filename = new File("UserFiles/" + session.getAttribute("password").toString().substring(0, 20) + ".txt");
            Filename.delete();
            session.removeAttribute("keypair" + session.getAttribute("username"));
            session.removeAttribute("cipher" + session.getAttribute("username"));

            //check if the draws win
            String[] draws = (String[]) session.getAttribute("draws");
            session.removeAttribute("draws");
            if (draws != null) {
                for (int i = 0; i < draws.length; i++) {
                    if (draws[i].equals(numbers)){
                      won = true;
                      break;
                  }
                }
            }

            //display appropriate message to the user
            RequestDispatcher dispatcher = request.getRequestDispatcher("/account.jsp");
            if (won) {
                request.setAttribute("message", "You won! The numbers were: " + numbers);
            }
            else {
                request.setAttribute("message", "You lost");
            }
            dispatcher.forward(request, response);
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

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}

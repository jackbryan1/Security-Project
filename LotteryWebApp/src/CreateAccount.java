import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

@WebServlet("/CreateAccount")
public class CreateAccount extends HttpServlet {

    private Connection conn;
    private PreparedStatement stmt;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // MySql database connection info
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

        HttpSession session = request.getSession();
        session.removeAttribute("firstname");
        session.removeAttribute("lastname");
        session.removeAttribute("username");
        session.removeAttribute("email");
        session.removeAttribute("password");

        // get parameter data that was submitted in HTML form (use form attributes 'name')
        String firstname = request.getParameter("firstname");
        String lastname = request.getParameter("lastname");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String username = request.getParameter("username");
        String password = hashPassword(request.getParameter("password"));
        String role = request.getParameter("role");

        try{
            // create database connection and statement
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            //set user data as session attributes
            session.setAttribute("firstname", firstname);
            session.setAttribute("lastname", lastname);
            session.setAttribute("username", username);
            session.setAttribute("email", email);
            session.setAttribute("password", password);
            session.setAttribute("role", role);


            // Create sql query
            String query = "INSERT INTO userAccounts (Firstname, Lastname, Email, Phone, Username, Pwd, Role)"
                    + " VALUES (?, ?, ?, ?, ?, ?, ?)";


            // set values into SQL query statement
            stmt = conn.prepareStatement(query);
            stmt.setString(1,firstname);
            stmt.setString(2,lastname);
            stmt.setString(3,email);
            stmt.setString(4,phone);
            stmt.setString(5,username);
            stmt.setString(6,password);
            stmt.setString(7,role);

            // execute query and close connection
            stmt.execute();
            conn.close();

            // display account.jsp page with given message if successful
            if (session.getAttribute("role").equals("public")){
                RequestDispatcher dispatcher = request.getRequestDispatcher("/account.jsp");
                request.setAttribute("message", firstname+", you have successfully created an account");
                dispatcher.forward(request, response);
            }
            else {
                RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp");
                request.setAttribute("message", firstname+", you have successfully created an account");
                request.setAttribute("data", "Please log back in to view user data");
                dispatcher.forward(request, response);
            }

        } catch(Exception se){
            se.printStackTrace();
            // display error.jsp page with given message if unsuccessful
            RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
            request.setAttribute("message", firstname+", this username/password combination already exists. Please try again");
            dispatcher.forward(request, response);
        }
        finally{
            try{
                if(stmt!=null)
                    stmt.close();
            }
            catch(SQLException se2){}
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }


    }

    public String hashPassword(String inputPassword){
        //perform SHA-512 hash (without salt)
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

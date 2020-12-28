import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

@WebServlet("/AddUserNumbers")
public class AddUserNumbers extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String numbers = request.getParameter("num1") + "," + request.getParameter("num2") + "," + request.getParameter("num3") + "," + request.getParameter("num4") + "," + request.getParameter("num5") + "," + request.getParameter("num6");

        //generate cipher and keypair
        KeyPairGenerator keyPairGen = null;
        Cipher cipher = null;
        try {
            keyPairGen = KeyPairGenerator.getInstance("RSA");
            cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        }
        KeyPair pair = keyPairGen.generateKeyPair();

        HttpSession session = request.getSession();

        //adds cipher and keypair to the session array for the specific user
        ArrayList<KeyPair> pairarray = new ArrayList<>();
        ArrayList<Cipher> cipherarray =  new ArrayList<>();
        if (session.getAttribute("keypair" + session.getAttribute("username")) != null){
            pairarray.addAll((ArrayList<KeyPair>) session.getAttribute("keypair" + session.getAttribute("username")));
        }
        if (session.getAttribute("cipher" + session.getAttribute("username")) != null) {
            cipherarray.addAll((ArrayList<Cipher>) session.getAttribute("cipher" + session.getAttribute("username")));
        }
        pairarray.add(pair);
        cipherarray.add(cipher);
        session.setAttribute("keypair" + session.getAttribute("username"), pairarray);
        session.setAttribute("cipher" + session.getAttribute("username"), cipherarray);

        //encrypt the numbers and write them to file
        EncryptedStorage es = new EncryptedStorage();
        es.bytesFileWriter("UserFiles/" + session.getAttribute("password").toString().substring(0, 20) + ".txt",es.encryptData(numbers, pair, cipher));
        RequestDispatcher dispatcher = request.getRequestDispatcher("/account.jsp");
        request.setAttribute("message", "Numbers added successfully");
        dispatcher.forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }


}

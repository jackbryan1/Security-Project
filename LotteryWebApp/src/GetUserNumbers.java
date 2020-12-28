import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

@WebServlet("/GetUserNumbers")
public class GetUserNumbers extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();

        //only start if there are keypairs and ciphers to decrypt with
        if (session.getAttribute("keypair" + session.getAttribute("username")) != null && (session.getAttribute("cipher" + session.getAttribute("username")) != null)) {

            //get data from file
            byte[] EncryptedData;
            int i = 0;
            EncryptedStorage es = new EncryptedStorage();
            EncryptedData = es.bytesFileReader("UserFiles/" + session.getAttribute("password").toString().substring(0, 20) + ".txt");
            int size = EncryptedData.length / 256;
            String[] Data = new String[size];
            ArrayList<KeyPair> pairarray = new ArrayList<>();
            ArrayList<Cipher> cipherarray = new ArrayList<>();
            pairarray.addAll((ArrayList<KeyPair>) session.getAttribute("keypair" + session.getAttribute("username")));
            cipherarray.addAll((ArrayList<Cipher>) session.getAttribute("cipher" + session.getAttribute("username")));

            //decrypt data
            while (i < size) {
                Data[i] = (es.decryptData(Arrays.copyOfRange(EncryptedData, i * 256, ((i + 1) * 256)), pairarray.get(i), cipherarray.get(i)));
                i++;
            }
            session.setAttribute("draws", Data);

            RequestDispatcher dispatcher = request.getRequestDispatcher("/account.jsp");
            request.setAttribute("message", "Draws found");
            dispatcher.forward(request, response);
        }
        else{
            RequestDispatcher dispatcher = request.getRequestDispatcher("/account.jsp");
            request.setAttribute("message", "No draws found");
            dispatcher.forward(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}

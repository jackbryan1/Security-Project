import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpSession;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;

public class EncryptedStorage {

    public EncryptedStorage(){}

    //encrypt data
    public byte[] encryptData(String data, KeyPair pair, Cipher cipher) {
        try{
            PublicKey publicKey = pair.getPublic();
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            cipher.update(data.getBytes());
            return cipher.doFinal();
        }
        catch (InvalidKeyException ex){
            ex.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }

    //write data to a file
    public void bytesFileWriter(String filename, byte[] data){
        try {
            OutputStream os = new FileOutputStream(filename, true);
            os.write(data);
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //read data from a file
    public byte[] bytesFileReader(String filename){
        try {
            byte[] test = Files.readAllBytes(Paths.get(filename));
            return test;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //decrypt data
    public String decryptData(byte[] data, KeyPair pair, Cipher cipher) {
        try {
            cipher.init(Cipher.DECRYPT_MODE, pair.getPrivate());
            byte[] decipheredText = cipher.doFinal(data);
            return new String(decipheredText, StandardCharsets.UTF_8);
        }
        catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }
}


import javax.crypto.*;
import java.security.NoSuchAlgorithmException;

public class Test {
    public static void main(String... args) throws NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {

    }


    public void encryptData(String plaintext) {
        System.out.println("-------Encrypting data using AES algorithm-------");
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128);
            SecretKey secretKey = keyGenerator.generateKey();
            byte[] plaintTextByteArray = plaintext.getBytes("UTF8");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding s");
// 	       cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] cipherText = cipher.doFinal(plaintTextByteArray);

            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            System.out.println("Original data: " + plaintext);
            System.out.println("Encrypted data:");
            for (int i = 0; i < cipherText.length; i++) {
                System.out.print(cipherText[i] + " ");

            }



        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}


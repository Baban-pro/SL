import java.util.*;
import java.io.*;

public class CaesarCipher {
    public static String temp = "";
    public static int k;

    // Encrypts text using a shift cipher (Caesar Cipher)
    public static StringBuffer encrypt(String m, int k) {
        StringBuffer encrypto = new StringBuffer();
        for (int i = 0; i < m.length(); i++) {
            char ch = m.charAt(i);

            if (Character.isUpperCase(ch)) {
                // For uppercase letters
                char encCh = (char) (((int) ch + k - 65) % 26 + 65);
                encrypto.append(encCh);
            } else if (Character.isLowerCase(ch)) {
                // For lowercase letters
                char encCh = (char) (((int) ch + k - 97) % 26 + 97);
                encrypto.append(encCh);
            } else {
                // Non-alphabetic characters (spaces, punctuation, etc.)
                encrypto.append(ch);
            }
        }
        temp = encrypto.toString();
        return encrypto;
    }

    // Decrypts text using the shift cipher (Caesar Cipher)
    public static StringBuffer decrypt(String temp, int k) {
        StringBuffer decrypto = new StringBuffer();
        for (int i = 0; i < temp.length(); i++) {
            char ch = temp.charAt(i);

            if (Character.isUpperCase(ch)) {
                // For uppercase letters (adjust for negative values)
                char decCh = (char) (((int) ch - k - 65 + 26) % 26 + 65);
                decrypto.append(decCh);
            } else if (Character.isLowerCase(ch)) {
                // For lowercase letters (adjust for negative values)
                char decCh = (char) (((int) ch - k - 97 + 26) % 26 + 97);
                decrypto.append(decCh);
            } else {
                // Non-alphabetic characters (spaces, punctuation, etc.)
                decrypto.append(ch);
            }
        }
        return decrypto;
    }

    // Main method
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Input the key for the cipher
        System.out.println("Enter a key:");
        int k = sc.nextInt();
        sc.nextLine(); // Consume newline

        // Input the text to encrypt
        System.out.println("Enter the text:");
        String m = sc.nextLine();

        // Perform encryption and decryption
        System.out.println("Text: " + m);
        System.out.println("Key: " + k);
        System.out.println("Cipher: " + encrypt(m, k));
        System.out.println("Decrypted: " + decrypt(temp, k));

        sc.close();
    }
}

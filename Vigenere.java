import java.util.Scanner;

public class Vigenere {

    // Method to generate the key in a cyclic manner to match the length of the plaintext
    static String generateKey(String str, String key) {
        int x = str.length();
        for (int i = 0; ; i++) {
            if (key.length() == x) {
                break;
            }
            key += (key.charAt(i));
        }
        return key;
    }

    // Method to encrypt the plaintext using the Vigenère cipher
    static String cipherText(String str, String key) {
        String cipher_text = "";
        for (int i = 0; i < str.length(); i++) {
            int x = (str.charAt(i) + key.charAt(i)) % 26;
            // Convert into a character in the range 'A'-'Z'
            x += 'A';
            cipher_text += (char) (x);
        }
        return cipher_text;
    }

    // Method to decrypt the ciphertext back to the original plaintext
    static String originalText(String cipher_text, String key) {
        String orig_text = "";
        for (int i = 0; i < cipher_text.length() && i < key.length(); i++) {
            int x = (cipher_text.charAt(i) - key.charAt(i) + 26) % 26;
            // Convert into a character in the range 'A'-'Z'
            x += 'A';
            orig_text += (char) (x);
        }
        return orig_text;
    }

    // Method to convert all lowercase letters to uppercase
    static String LowerToUpper(String s) {
        StringBuffer str = new StringBuffer(s);
        for (int i = 0; i < s.length(); i++) {
            if (Character.isLowerCase(s.charAt(i))) {
                str.setCharAt(i, Character.toUpperCase(s.charAt(i)));
            }
        }
        return str.toString();
    }

    // Main method to drive the program
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Take input for plaintext
        System.out.println("Enter a string (plaintext):");
        String Str = sc.nextLine();

        // Take input for the keyword
        System.out.println("Enter a keyword:");
        String Keyword = sc.nextLine();

        // Convert plaintext and keyword to uppercase
        String str = LowerToUpper(Str);
        String keyword = LowerToUpper(Keyword);

        // Generate the key based on the keyword
        String key = generateKey(str, keyword);

        // Encrypt the plaintext
        String cipher_text = cipherText(str, key);

        // Output the results
        System.out.println("Ciphertext : " + cipher_text);
        System.out.println("Original/Decrypted Text : " + originalText(cipher_text, key));

        sc.close();
    }
}

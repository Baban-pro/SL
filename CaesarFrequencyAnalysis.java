import java.util.*;

public class CaesarFrequencyAnalysis {
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

    // Frequency analysis function
    public static Map<Character, Integer> calculateFrequency(String cipherText) {
        Map<Character, Integer> frequencyMap = new HashMap<>();

        for (char ch : cipherText.toCharArray()) {
            if (Character.isLetter(ch)) {
                ch = Character.toLowerCase(ch); // Treat uppercase and lowercase letters as the same
                frequencyMap.put(ch, frequencyMap.getOrDefault(ch, 0) + 1);
            }
        }
        return frequencyMap;
    }

    // Display frequencies in descending order
    public static void displayFrequencies(Map<Character, Integer> freqMap) {
        freqMap.entrySet().stream()
            .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue())) // Sort by frequency (descending)
            .forEach(e -> System.out.println(e.getKey() + ": " + e.getValue()));
    }

    // Main method
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Input the key for the cipher
        System.out.println("Enter a key for encryption:");
        int k = sc.nextInt();
        sc.nextLine(); // Consume newline

        // Input the text to encrypt
        System.out.println("Enter the text to encrypt:");
        String m = sc.nextLine();

        // Perform encryption
        System.out.println("Original Text: " + m);
        System.out.println("Key: " + k);
        System.out.println("Encrypted Text (Cipher): " + encrypt(m, k));

        // Perform decryption
        System.out.println("Decrypted Text: " + decrypt(temp, k));

        // Perform frequency analysis on the encrypted text
        System.out.println("\nPerforming Frequency Analysis on the Encrypted Text...");
        Map<Character, Integer> freqMap = calculateFrequency(temp);

        // Display letter frequencies in the cipher text
        System.out.println("Letter Frequencies:");
        displayFrequencies(freqMap);

        sc.close();
    }
}

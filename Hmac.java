import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Hmac {

    // Function to compute SHA-256 hash
    public static byte[] getSHA(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        return md.digest(input.getBytes(StandardCharsets.UTF_8));
    }

    // Convert byte array to hex string
    public static String toHexString(byte[] hash) {
        BigInteger number = new BigInteger(1, hash);
        StringBuilder hexString = new StringBuilder(number.toString(16));

        while (hexString.length() < 64) {
            hexString.insert(0, '0');
        }

        return hexString.toString();
    }

    // HMAC generation using SHA-256
    public static String calculateHMAC(String data, String key) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(), "HmacSHA256");
        sha256_HMAC.init(secret_key);

        byte[] hash = sha256_HMAC.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(hash); // Encoded as Base64 for simplicity
    }

    // AES encryption
    public static String encrypt(String data, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    // AES decryption
    public static String decrypt(String encryptedData, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        return new String(decryptedBytes);
    }

    // Main method to drive the user interaction
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            // User input
            System.out.println("Enter a message to hash and encrypt:");
            String message = scanner.nextLine();

            System.out.println("Enter a secret key for HMAC and AES encryption:");
            String secretKeyInput = scanner.nextLine();

            // 1. Hash the message using SHA-256
            System.out.println("\nSHA-256 Hash:");
            String shaHash = toHexString(getSHA(message));
            System.out.println(shaHash);

            // 2. Generate HMAC for the message
            System.out.println("\nHMAC (SHA-256):");
            String hmac = calculateHMAC(message, secretKeyInput);
            System.out.println(hmac);

            // 3. Generate AES key for encryption
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(128); // AES key size
            SecretKey aesKey = keyGen.generateKey();

            // 4. Encrypt the message using AES
            String encryptedMessage = encrypt(message, aesKey);
            System.out.println("\nAES Encrypted Message:");
            System.out.println(encryptedMessage);

            // 5. Decrypt the message using AES
            String decryptedMessage = decrypt(encryptedMessage, aesKey);
            System.out.println("\nAES Decrypted Message:");
            System.out.println(decryptedMessage);

            // 6. Verify HMAC: Recalculate HMAC and compare
            String recalculatedHmac = calculateHMAC(message, secretKeyInput);
            if (recalculatedHmac.equals(hmac)) {
                System.out.println("\nHMAC Verification: Success (Message is authentic)");
            } else {
                System.out.println("\nHMAC Verification: Failed (Message integrity compromised)");
            }

        } catch (NoSuchAlgorithmException e) {
            System.out.println("Error: No such algorithm found: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

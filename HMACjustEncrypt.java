import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class HMACjustEncrypt {

    public static String calculateHMAC(String data, String key) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(), "HmacSHA256");
        sha256_HMAC.init(secret_key);

        byte[] hash = sha256_HMAC.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(hash); // Encoded as Base64 for simplicity
    }

    public static void main(String[] args) {
        try {
            String message = "This is a message";
            String secretKey = "topsecretkey";

            String hmac = calculateHMAC(message, secretKey);
            System.out.println("HMAC: " + hmac);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

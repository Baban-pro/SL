import java.math.BigInteger;
import java.util.Scanner;

public class RSA {

    // Function to compute GCD (Greatest Common Divisor)
    public static BigInteger gcd(BigInteger a, BigInteger b) {
        if (b.equals(BigInteger.ZERO)) {
            return a;
        }
        return gcd(b, a.mod(b));
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Step 1: Input two large prime numbers
        System.out.println("Enter first prime number (p): ");
        BigInteger p = sc.nextBigInteger();
        System.out.println("Enter second prime number (q): ");
        BigInteger q = sc.nextBigInteger();

        // Step 2: Compute n = p * q
        BigInteger n = p.multiply(q);

        // Step 3: Compute z = (p - 1) * (q - 1)
        BigInteger z = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));

        // Step 4: Choose e (public key exponent), such that 1 < e < z and gcd(e, z) = 1
        BigInteger e;
        for (e = BigInteger.valueOf(2); e.compareTo(z) < 0; e = e.add(BigInteger.ONE)) {
            if (gcd(e, z).equals(BigInteger.ONE)) {
                break;
            }
        }

        // Step 5: Compute d (private key exponent) such that (d * e) % z = 1
        BigInteger d = e.modInverse(z);

        // Step 6: Input message to encrypt
        System.out.println("Enter the message to be encrypted (as an integer): ");
        BigInteger message = sc.nextBigInteger();

        // Step 7: Encryption: c = (message^e) % n
        BigInteger encryptedMessage = message.modPow(e, n);
        System.out.println("Encrypted message is: " + encryptedMessage);

        // Step 8: Decryption: originalMessage = (c^d) % n
        BigInteger decryptedMessage = encryptedMessage.modPow(d, n);
        System.out.println("Decrypted message is: " + decryptedMessage);

        sc.close();
    }
}

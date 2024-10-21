import java.util.Scanner;

public class HillCipher {

    // Function to multiply the key matrix and message vector
    public static int[] matrixMultiply(int[][] keyMatrix, int[] messageVector, int size) {
        int[] resultVector = new int[size];
        for (int i = 0; i < size; i++) {
            resultVector[i] = 0;
            for (int j = 0; j < size; j++) {
                resultVector[i] += keyMatrix[i][j] * messageVector[j];
            }
            resultVector[i] %= 26; // Modular arithmetic for encryption/decryption
        }
        return resultVector;
    }

    // Function to find the modular inverse of a number mod 26
    public static int modInverse(int determinant, int mod) {
        determinant = determinant % mod;
        for (int i = 1; i < mod; i++) {
            if ((determinant * i) % mod == 1) {
                return i;
            }
        }
        return -1; // No modular inverse found
    }

    // Function to calculate the determinant of a matrix
    public static int findDeterminant(int[][] matrix, int size) {
        if (size == 2) {
            return (matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0]) % 26;
        }
        int determinant = 0;
        for (int i = 0; i < size; i++) {
            determinant += (Math.pow(-1, i) * matrix[0][i] * findDeterminant(getMinor(matrix, 0, i), size - 1));
        }
        return determinant % 26;
    }

    // Function to get the minor matrix for determinant calculation
    public static int[][] getMinor(int[][] matrix, int row, int col) {
        int size = matrix.length;
        int[][] minor = new int[size - 1][size - 1];
        int r = 0;
        for (int i = 0; i < size; i++) {
            if (i == row) continue;
            int c = 0;
            for (int j = 0; j < size; j++) {
                if (j == col) continue;
                minor[r][c++] = matrix[i][j];
            }
            r++;
        }
        return minor;
    }

    // Function to get the adjugate of the matrix (used to find inverse matrix)
    public static int[][] getAdjugate(int[][] matrix, int size) {
        int[][] adjugate = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                adjugate[j][i] = (int) (Math.pow(-1, i + j) * findDeterminant(getMinor(matrix, i, j), size - 1));
                adjugate[j][i] = (adjugate[j][i] + 26) % 26; // Adjust negative values
            }
        }
        return adjugate;
    }

    // Function to find the inverse of the matrix mod 26
    public static int[][] findInverseMatrix(int[][] keyMatrix, int size) {
        int determinant = findDeterminant(keyMatrix, size);
        determinant = (determinant + 26) % 26; // Adjust negative determinants
        int inverseDet = modInverse(determinant, 26);

        if (inverseDet == -1) {
            throw new ArithmeticException("Matrix is not invertible.");
        }

        int[][] adjugate = getAdjugate(keyMatrix, size);

        int[][] inverseMatrix = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                inverseMatrix[i][j] = (adjugate[i][j] * inverseDet) % 26;
                if (inverseMatrix[i][j] < 0) {
                    inverseMatrix[i][j] += 26; // Adjust negative values
                }
            }
        }
        return inverseMatrix;
    }

    // Function to encrypt the message
    public static String encrypt(String message, int[][] keyMatrix, int size) {
        int[] messageVector = new int[size];
        StringBuilder cipherText = new StringBuilder();

        // Process the message in blocks of the size of the key matrix
        for (int i = 0; i < message.length(); i += size) {
            // Prepare message vector for each block
            for (int j = 0; j < size; j++) {
                messageVector[j] = message.charAt(i + j) - 'A'; // Convert to 0-25 range
            }

            // Multiply key matrix with the message vector
            int[] cipherVector = matrixMultiply(keyMatrix, messageVector, size);

            // Convert the result back to characters
            for (int j = 0; j < size; j++) {
                cipherText.append((char) (cipherVector[j] + 'A'));
            }
        }
        return cipherText.toString();
    }

    // Function to decrypt the message using the inverse key matrix
    public static String decrypt(String cipherText, int[][] keyMatrix, int size) {
        int[][] inverseKeyMatrix = findInverseMatrix(keyMatrix, size);
        int[] cipherVector = new int[size];
        StringBuilder originalText = new StringBuilder();

        // Process the ciphertext in blocks
        for (int i = 0; i < cipherText.length(); i += size) {
            // Prepare cipher vector for each block
            for (int j = 0; j < size; j++) {
                cipherVector[j] = cipherText.charAt(i + j) - 'A';
            }

            // Multiply inverse key matrix with the cipher vector
            int[] originalVector = matrixMultiply(inverseKeyMatrix, cipherVector, size);

            // Convert the result back to characters
            for (int j = 0; j < size; j++) {
                originalText.append((char) (originalVector[j] + 'A'));
            }
        }
        return originalText.toString();
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Take matrix size as input
        System.out.println("Enter the size of the key matrix:");
        int size = sc.nextInt();

        int[][] keyMatrix = new int[size][size];

        // Take input for the key matrix, ensuring it is invertible
        while (true) {
            System.out.println("Enter the " + size + "x" + size + " key matrix (values 0-25):");
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    keyMatrix[i][j] = sc.nextInt();
                }
            }

            int determinant = findDeterminant(keyMatrix, size);
            determinant = (determinant + 26) % 26; // Adjust negative determinants

            if (modInverse(determinant, 26) != -1) {
                break;
            } else {
                System.out.println("Error: Matrix is not invertible. Please enter a different key matrix.");
            }
        }

        // Take input for the message to encrypt
        System.out.println("Enter the message (only A-Z, no spaces):");
        String message = sc.next().toUpperCase();

        // Ensure message length is a multiple of the matrix size
        while (message.length() % size != 0) {
            message += "X"; // Padding with 'X' if necessary
        }

        // Encrypt the message
        String cipherText = encrypt(message, keyMatrix, size);
        System.out.println("Encrypted message: " + cipherText);

        // Decrypt the message
        try {
            String decryptedText = decrypt(cipherText, keyMatrix, size);
            System.out.println("Decrypted message: " + decryptedText);
        } catch (ArithmeticException e) {
            System.out.println("Error: " + e.getMessage());
        }

        sc.close();
    }
}

import java.awt.Point;
import java.util.Scanner;

public class PlayfairCipher {
    // Length of digraph array
    private int length = 0;
    // Playfair cipher key matrix
    private String[][] table;

    // Main method
    public static void main(String args[]) {
        PlayfairCipher pf = new PlayfairCipher();
    }

    // Constructor: prompts user for input and encodes the plaintext
    private PlayfairCipher() {
        Scanner sc = new Scanner(System.in);

        // Prompts user for the key
        System.out.print("Enter the key for Playfair cipher: ");
        String key = parseString(sc.nextLine());
        while (key.equals("")) {
            key = parseString(sc.nextLine());
        }

        // Generate the cipher key matrix
        table = cipherTable(key);

        // Prompts user for the plaintext
        System.out.print("Enter the plaintext to encipher: ");
        String input = parseString(sc.nextLine());
        while (input.equals("")) {
            input = parseString(sc.nextLine());
        }

        // Perform encryption and decryption
        String output = cipher(input);
        String decodedOutput = decode(output);

        // Output the results
        keyTable(table);
        printResults(output, decodedOutput);

        sc.close();
    }

    // Parses input string: removes numbers, punctuation, and replaces J with I
    private String parseString(String input) {
        input = input.toUpperCase().replaceAll("[^A-Z]", "");
        input = input.replace("J", "I");
        return input;
    }

    // Generates the 5x5 Playfair cipher table based on the key
    private String[][] cipherTable(String key) {
        String[][] playfairTable = new String[5][5];
        String keyString = key + "ABCDEFGHIKLMNOPQRSTUVWXYZ";

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                playfairTable[i][j] = "";
            }
        }

        for (int k = 0; k < keyString.length(); k++) {
            boolean repeat = false;
            boolean used = false;
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    if (playfairTable[i][j].equals("" + keyString.charAt(k))) {
                        repeat = true;
                    } else if (playfairTable[i][j].equals("") && !repeat && !used) {
                        playfairTable[i][j] = "" + keyString.charAt(k);
                        used = true;
                    }
                }
            }
        }
        return playfairTable;
    }

    // Enciphers the input string using Playfair Cipher
    private String cipher(String input) {
        length = (input.length() / 2) + (input.length() % 2);

        for (int i = 0; i < (length - 1); i++) {
            if (input.charAt(2 * i) == input.charAt(2 * i + 1)) {
                input = new StringBuffer(input).insert(2 * i + 1, 'X').toString();
                length = (input.length() / 2) + (input.length() % 2);
            }
        }

        if (input.length() % 2 != 0) {
            input = input + "X";
            length = (input.length() / 2) + (input.length() % 2);
        }

        String[] digraph = new String[length];
        for (int j = 0; j < length; j++) {
            digraph[j] = input.charAt(2 * j) + "" + input.charAt(2 * j + 1);
        }

        String out = "";
        String[] encDigraphs = encodeDigraph(digraph);
        for (int k = 0; k < length; k++) {
            out += encDigraphs[k];
        }

        return out;
    }

    // Encodes the digraphs using Playfair cipher rules
    private String[] encodeDigraph(String[] digraph) {
        String[] encipher = new String[length];
        for (int i = 0; i < length; i++) {
            char a = digraph[i].charAt(0);
            char b = digraph[i].charAt(1);
            int r1 = (int) getPoint(a).getX();
            int r2 = (int) getPoint(b).getX();
            int c1 = (int) getPoint(a).getY();
            int c2 = (int) getPoint(b).getY();

            if (r1 == r2) {
                c1 = (c1 + 1) % 5;
                c2 = (c2 + 1) % 5;
            } else if (c1 == c2) {
                r1 = (r1 + 1) % 5;
                r2 = (r2 + 1) % 5;
            } else {
                int temp = c1;
                c1 = c2;
                c2 = temp;
            }

            encipher[i] = table[r1][c1] + "" + table[r2][c2];
        }
        return encipher;
    }

    // Deciphers the encoded text
    private String decode(String out) {
        String decoded = "";
        for (int i = 0; i < out.length() / 2; i++) {
            char a = out.charAt(2 * i);
            char b = out.charAt(2 * i + 1);
            int r1 = (int) getPoint(a).getX();
            int r2 = (int) getPoint(b).getX();
            int c1 = (int) getPoint(a).getY();
            int c2 = (int) getPoint(b).getY();

            if (r1 == r2) {
                c1 = (c1 + 4) % 5;
                c2 = (c2 + 4) % 5;
            } else if (c1 == c2) {
                r1 = (r1 + 4) % 5;
                r2 = (r2 + 4) % 5;
            } else {
                int temp = c1;
                c1 = c2;
                c2 = temp;
            }

            decoded += table[r1][c1] + "" + table[r2][c2];
        }
        return decoded;
    }

    // Returns the position of a character in the key table
    private Point getPoint(char c) {
        Point pt = new Point(0, 0);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (c == table[i][j].charAt(0)) {
                    pt = new Point(i, j);
                }
            }
        }
        return pt;
    }

    // Prints the cipher key matrix
    private void keyTable(String[][] printTable) {
        System.out.println("Playfair Cipher Key Matrix:");
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                System.out.print(printTable[i][j] + " ");
            }
            System.out.println();
        }
    }

    // Prints the encrypted and decrypted messages
    private void printResults(String encipher, String dec) {
        System.out.println("Encrypted Message: " + encipher);
        System.out.println("Decrypted Message: " + dec);
    }
}

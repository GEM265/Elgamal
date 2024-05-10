import java.math.BigInteger;
import java.util.Random;
import java.util.Scanner;

public class El {
    private static final Random random = new Random();
    private static final BigInteger TWO = BigInteger.valueOf(2);
    private static final BigInteger ONE = BigInteger.valueOf(1);

    public static boolean isPrime(BigInteger n, int k) {
        // Miller-Rabin primality test
        if (n.compareTo(TWO) < 0) {
            return false;
        }
        if (n.isProbablePrime(k)) {
            return true;
        }
        return false;
    }

    public static BigInteger findPrime(int bits) {
        // Find a random prime number of given bit length
        while (true) {
            BigInteger n = new BigInteger(bits, random);
            if (isPrime(n, 5)) {
                return n;
            }
        }
    }

    public static BigInteger findGenerator(BigInteger p) {
        // Find a generator of the multiplicative group modulo p
        BigInteger g = TWO;
        while (g.modPow(p.subtract(ONE).divide(TWO), p)
                .equals(ONE)) {
            g = g.add(ONE);
        }
        return g;
    }

    public static BigInteger[] encrypt(BigInteger msg, BigInteger p, BigInteger g, BigInteger y) {
        // Encrypt a message using ElGamal encryption
        BigInteger k = new BigInteger(p.bitLength(), random);
        BigInteger a = g.modPow(k, p);
        BigInteger b = msg.multiply(y.modPow(k, p)).mod(p);
        return new BigInteger[] {a, b};
    }

    public static BigInteger decrypt(BigInteger[] ctext, BigInteger p, BigInteger x) {
        // Decrypt a ciphertext using ElGamal decryption
        BigInteger a = ctext[0];
        BigInteger b = ctext[1];
        BigInteger s = a.modPow(p.subtract(ONE).subtract(x), p);
        return b.multiply(s).mod(p);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter key size in bits: ");
        int bits = scanner.nextInt();

        BigInteger p = findPrime(bits);
        BigInteger g = findGenerator(p);
        System.out.println("Prime: " + p);
        System.out.println("Generator: " + g);

        BigInteger x = new BigInteger(p.bitLength() - 1, random);
        BigInteger y = g.modPow(x, p);
        System.out.println("Private key: " + x);
        System.out.println("Public key: (" + g + ", " + y + ")");

        System.out.print("Enter a message (integer): ");
        BigInteger msg = scanner.nextBigInteger();
        BigInteger[] ctext = encrypt(msg, p, g, y);
        System.out.println("Ciphertext: (" + ctext[0] + ", " + ctext[1] + ")");

        BigInteger decrypted = decrypt(ctext, p, x);
        System.out.println("Decrypted message: " + decrypted);

        scanner.close();
    }
}
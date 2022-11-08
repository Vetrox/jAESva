package crypto;

public final class AESUtil {
    private AESUtil() {}

    /**
     * Encrypts a plaintext via AES to the ciphertext.
     *
     * @param plaintext Plaintext that will be encrypted
     *
     * @return ciphertext from the encrypted plaintext
     */
    public static void encrypt(byte[][] plaintext) {
        addRoundKey(plaintext);

        for (int i = 0; i < 9; i++) {
            subBytes(plaintext);
            shiftRows(plaintext);
            mixColumns(plaintext);
            addRoundKey(plaintext);
        }

        subBytes(plaintext);
        shiftRows(plaintext);
        addRoundKey(plaintext);
    }

    public static void addRoundKey(byte[][] plaintext) {

    }

    public static void subBytes(byte[][] plaintext) {

    }

    public static void shiftRows(byte[][] plaintext) {
        for (int i = 1; i < 4; i++) {
            plaintext[i] = shiftRow(plaintext[i], i);
        }
    }

    public static byte[] shiftRow(byte[] row, int amount) {
        byte[] rowCopy = row.clone();
        for (int i = 0; i < 4; i++) {
            rowCopy[i] = row[(i + amount) % 4];
        }
        return rowCopy;
    }

    public static void mixColumns(byte[][] plaintext) {

    }
}

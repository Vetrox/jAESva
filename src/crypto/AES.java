package crypto;

public class AES {

    private byte[][] plaintext;

    public AES(byte[][] plaintext) {
        this.plaintext = plaintext;
    }

    /**
     * Encrypts a plaintext via AES to the ciphertext.
     * @param plaintext Plaintext that will be encrypted
     * @return ciphertext from the encrypted plaintext
     */
    public byte[][] encrypt(byte[][] plaintext) {
        addRoundKey();

        for (int i = 0; i < 9; i++) {
            subBytes();
            shiftRows();
            mixColumns();
            addRoundKey();
        }

        subBytes();
        shiftRows();
        addRoundKey();

        return plaintext;
    }

    public void addRoundKey() {

    }

    public void subBytes() {

    }

    public void shiftRows() {
        byte temp = plaintext[1][0];
        for(int i = 1; i < 4; i++)
            plaintext[i] = shiftRow(plaintext[i], i);
    }

    private byte[] shiftRow(byte[] row, int amount) {
        byte[] rowCopy = row.clone();
        for (int i = 0; i < 4; i++) {
            rowCopy[i] = row[(i + amount) % 4];
        }
        return rowCopy;
    }

    public void mixColumns() {

    }

    public byte[][] getText() {
        return plaintext;
    }

    public void setText(byte[][] plaintext) {
        this.plaintext = plaintext;
    }
}

package crypto;

import java.security.SecureRandom;
import java.util.ArrayList;

public final class CBCMode {

    private CBCMode() {}

    /**
     * @param message      [modified during execution but returned to original state]
     * @param key          [const]
     * @param initialValue [const]
     *
     * @return ciphertext
     */
    public static ArrayList<Boolean> encrypt(ArrayList<Boolean> message, byte[][] key, byte[][] initialValue) {
        byte[][] initialValue_ = new byte[4][4];
        COPY(initialValue, initialValue_);
        Modes.padMessage(message);
        ArrayList<byte[][]> blocks = ECBMode.split(message);
        Modes.unPadMessage(message);
        for (byte[][] block : blocks) {
            XOR(initialValue_, block);
            AESUtil.encrypt(block, key);
            COPY(block, initialValue_);
        }
        return ECBMode.merge(blocks);
    }

    /**
     * @param ciphertext   [const]
     * @param key          [const]
     * @param initialValue [const]
     *
     * @return plaintext
     */
    public static ArrayList<Boolean> decrypt(ArrayList<Boolean> ciphertext, byte[][] key, final byte[][] initialValue) {
        byte[][] prevBlock = new byte[4][4];
        COPY(initialValue, prevBlock);
        byte[][] cipherCache = new byte[4][4];

        ArrayList<byte[][]> blocks = ECBMode.split(ciphertext);
        for (byte[][] block : blocks) {
            COPY(block, cipherCache);
            AESUtil.decrypt(block, key);
            XOR(prevBlock, block);
            COPY(cipherCache, prevBlock);
        }
        ArrayList<Boolean> ret = ECBMode.merge(blocks);
        Modes.unPadMessage(ret);
        return ret;
    }

    private static void COPY(final byte[][] source, byte[][] destination) {
        System.arraycopy(source[0], 0, destination[0], 0, 4);
        System.arraycopy(source[1], 0, destination[1], 0, 4);
        System.arraycopy(source[2], 0, destination[2], 0, 4);
        System.arraycopy(source[3], 0, destination[3], 0, 4);
    }

    private static void XOR(final byte[][] source, byte[][] destination) {
        destination[0][0] ^= source[0][0];
        destination[0][1] ^= source[0][1];
        destination[0][2] ^= source[0][2];
        destination[0][3] ^= source[0][3];
        destination[1][0] ^= source[1][0];
        destination[1][1] ^= source[1][1];
        destination[1][2] ^= source[1][2];
        destination[1][3] ^= source[1][3];
        destination[2][0] ^= source[2][0];
        destination[2][1] ^= source[2][1];
        destination[2][2] ^= source[2][2];
        destination[2][3] ^= source[2][3];
        destination[3][0] ^= source[3][0];
        destination[3][1] ^= source[3][1];
        destination[3][2] ^= source[3][2];
        destination[3][3] ^= source[3][3];
    }

    private static final SecureRandom random = new SecureRandom();

    public static byte[][] generateInitialValue() {
        byte[][] iv = new byte[4][];
        for (int i = 0; i < 4; i++) {
            iv[i] = random.generateSeed(4);
        }
        return iv;
    }

}

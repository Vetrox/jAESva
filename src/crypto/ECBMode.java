package crypto;

import java.util.ArrayList;

public class ECBMode implements Modes {

    protected ECBMode() {}

    public static ArrayList<byte[][]> split(ArrayList<Boolean> message) {
        if (message.size() % Modes.blockSizeBits != 0) {
            throw new AssertionError("Un-padded Message.");
        }

        ArrayList<byte[][]> blocks = new ArrayList<>();
        for (int p = 0; p < message.size() / Modes.blockSizeBits; p++) {
            byte[][] block = new byte[4][4];
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    block[i][j] = Modes.getByte(message, p * Modes.blockSizeBits + i * 4 * 8 + j * 8);
                }
            }
            blocks.add(block);
        }
        return blocks;
    }

    public static ArrayList<Boolean> merge(ArrayList<byte[][]> blocks) {
        ArrayList<Boolean> message = new ArrayList<>();
        for (byte[][] block : blocks) {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    for (int k = 0; k < 8; k++) {
                        message.add((block[i][j] & (1 << (7 - k))) != 0);
                    }
                }
            }
        }
        return message;
    }

    /**
     * @param message [modified during execution but returned to original state]
     * @param key     [const]
     *
     * @return ciphertext
     */
    public static ArrayList<Boolean> encrypt(ArrayList<Boolean> message, byte[][] key) {
        Modes.padMessage(message);
        ArrayList<byte[][]> blocks = split(message);
        Modes.unPadMessage(message);
        for (byte[][] block : blocks) {
            AESUtil.encrypt(block, key);
        }
        return merge(blocks);
    }

    public static ArrayList<Boolean> decrypt(ArrayList<Boolean> ciphertext, byte[][] key) {
        ArrayList<byte[][]> blocks = split(ciphertext);
        for (byte[][] block : blocks) {
            AESUtil.decrypt(block, key);
        }
        ArrayList<Boolean> ret = merge(blocks);
        Modes.unPadMessage(ret);
        return ret;
    }
}

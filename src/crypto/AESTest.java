package crypto;

import org.junit.jupiter.api.Assertions;

class AESTest {

    private static final byte[][] beforeSubBytes = new byte[][]{
            hexStringToByteArray("19a09ae9"),
            hexStringToByteArray("3df4c6f8"),
            hexStringToByteArray("e3e28d48"),
            hexStringToByteArray("be2b2a08")
    };
    ;
    private static final byte[][] afterSubBytes = new byte[][]{
            hexStringToByteArray("d4e0b81e"),
            hexStringToByteArray("27bfb441"),
            hexStringToByteArray("11985d52"),
            hexStringToByteArray("aef1e530")
    };
    public static final byte[][] afterShiftRows = new byte[][]{
            hexStringToByteArray("d4e0b81e"),
            hexStringToByteArray("bfb44127"),
            hexStringToByteArray("5d521198"),
            hexStringToByteArray("30aef1e5")
    };

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    @org.junit.jupiter.api.Test
    void subBytes() {
        byte[][] cache = beforeSubBytes.clone();
        AESUtil.subBytes(cache);
        Assertions.assertArrayEquals(cache, afterSubBytes);
    }

    @org.junit.jupiter.api.Test
    void shiftRows() {
        byte[][] cache = afterSubBytes.clone();
        AESUtil.shiftRows(cache);
        Assertions.assertArrayEquals(cache, afterShiftRows);
    }
}
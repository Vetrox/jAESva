package crypto;

import org.junit.jupiter.api.Assertions;

import static org.junit.jupiter.api.Assertions.*;

class AESTest {

    static byte[][] beforeSubBytes;
    static byte[][] afterSubBytes;
    static byte[][] afterShiftRows;

    @org.junit.jupiter.api.BeforeAll
    public static void init() {
        beforeSubBytes = new byte[][] {
                hexStringToByteArray("19a09ae9"),
                hexStringToByteArray("3df4c6f8"),
                hexStringToByteArray("e3e28d48"),
                hexStringToByteArray("be2b2a08")
        };

        afterSubBytes = new byte[][] {
                hexStringToByteArray("d4e0b81e"),
                hexStringToByteArray("27bfb441"),
                hexStringToByteArray("11985d52"),
                hexStringToByteArray("aef1e530")
        };

        afterShiftRows = new byte[][] {
                hexStringToByteArray("d4e0b81e"),
                hexStringToByteArray("bfb44127"),
                hexStringToByteArray("5d521198"),
                hexStringToByteArray("30aef1e5")
        };
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    @org.junit.jupiter.api.Test
    void shiftRows() {
        AES aes = new AES(afterSubBytes);
        aes.shiftRows();
        Assertions.assertArrayEquals(aes.getText(), afterShiftRows);
    }
}
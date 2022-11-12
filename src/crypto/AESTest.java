package crypto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AESTest {

    public static final byte[][] key = new byte[][]{
            hexStringToByteArray("2b28ab09"),
            hexStringToByteArray("7eaef7cf"),
            hexStringToByteArray("15d2154f"),
            hexStringToByteArray("16a6883c")
    };

    private static final byte[][] plaintext = new byte[][]{
            hexStringToByteArray("328831e0"),
            hexStringToByteArray("435a3137"),
            hexStringToByteArray("f6309807"),
            hexStringToByteArray("a88da234")
    };

    private static final byte[][] ciphertext = new byte[][]{
            hexStringToByteArray("3902dc19"),
            hexStringToByteArray("25dc116a"),
            hexStringToByteArray("8409850b"),
            hexStringToByteArray("1dfb9732")
    };

    private static final byte[][] keyScheduleExp = new byte[][]{
            hexStringToByteArray("2b28ab09 a088232a f27a5973 3d471e6d efa8b6db d47cca11 6d11dbca 4e5f844e eab5317f " + "ac192857 d0c9e1b6"),
            hexStringToByteArray("7eaef7cf fa54a36c c2963559 8016237a 4452710b d183f2f9 880bf900 545fa6a6 d28d2b8d " + "77fad15c 14ee3f63"),
            hexStringToByteArray("15d2154f fe2c3976 95b980f6 47fe7e88 a55b25ad c69db815 a33e8693 f7c94fdc 73baf529 " + "66dc2900 f9250c0c"),
            hexStringToByteArray("16a6883c 17b13905 f2437a7f 7d3e443b 417f3b00 f887bcbc 7afd41fd 0ef3b24f 21d2602f " + "f321416e a889c8a6")
    };

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
    public static final byte[][] afterMixColumns = new byte[][]{
            hexStringToByteArray("04e04828"),
            hexStringToByteArray("66cbf806"),
            hexStringToByteArray("8119d326"),
            hexStringToByteArray("e59a7a4c")
    };

    public static byte[] hexStringToByteArray(String s) {
        s = s.replaceAll("\\s+", "");
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    @Test
    void addRoundKey() {
        byte[][] c = copy(plaintext);
        AESUtil.addRoundKey(c, AESUtil.genKeySchedule(key), 0);
        Assertions.assertArrayEquals(beforeSubBytes, c);
    }

    private static byte[][] copy(byte[][] inp) {
        byte[][] ret = new byte[inp.length][inp[0].length];
        for (int i = 0; i < inp.length; i++) {
            System.arraycopy(inp[i], 0, ret[i], 0, inp[0].length);
        }
        return ret;
    }

    @Test
    void encrypt() {
        byte[][] cache = copy(plaintext);
        AESUtil.encrypt(cache, key);
        Assertions.assertArrayEquals(ciphertext, cache);
    }

    @Test
    void decrypt() {
        byte[][] cache = copy(ciphertext);
        AESUtil.decrypt(cache, key);
        Assertions.assertArrayEquals(plaintext, cache);

        AESUtil.encrypt(cache, key);
        AESUtil.decrypt(cache, key);
        Assertions.assertArrayEquals(plaintext, cache);
    }

    @Test
    void subBytes() {
        byte[][] cache = copy(beforeSubBytes);
        AESUtil.subBytes(cache);
        Assertions.assertArrayEquals(afterSubBytes, cache);
    }

    @Test
    void inverseSubBytes() {
        byte[][] cache = copy(afterSubBytes);
        AESUtil.inverseSubBytes(cache);
        Assertions.assertArrayEquals(beforeSubBytes, cache);

        cache = copy(beforeSubBytes);
        AESUtil.subBytes(cache);
        AESUtil.inverseSubBytes(cache);
        Assertions.assertArrayEquals(beforeSubBytes, cache);
    }

    @Test
    void shiftRows() {
        byte[][] cache = copy(afterSubBytes);
        AESUtil.shiftRows(cache);
        Assertions.assertArrayEquals(afterShiftRows, cache);
    }

    @Test
    void inverseShiftRows() {
        byte[][] cache = copy(afterShiftRows);
        AESUtil.inverseShiftRows(cache);
        Assertions.assertArrayEquals(afterSubBytes, cache);

        cache = copy(afterSubBytes);
        AESUtil.shiftRows(cache);
        AESUtil.inverseShiftRows(cache);
        Assertions.assertArrayEquals(afterSubBytes, cache);
    }

    @Test
    void keySchedule() {
        Assertions.assertArrayEquals(keyScheduleExp, AESUtil.genKeySchedule(key));
    }

    @Test
    void mixColumns() {
        byte[][] cache = copy(afterShiftRows);
        AESUtil.mixColumns(cache, AESUtil.AES_MATRIX);
        Assertions.assertArrayEquals(afterMixColumns, cache);

        AESUtil.mixColumns(cache, AESUtil.INVERSE_AES_MATRIX);
        Assertions.assertArrayEquals(afterShiftRows, cache);
    }

    @Test
    void multGalois() {
        Assertions.assertEquals(AESUtil.multGF256((byte) 0x02, (byte) 0xd4), (byte) 0xb3);
    }

    @Test
    void unsignedByte() {
        Assertions.assertEquals(Byte.toUnsignedInt((byte) 0xff), 0xff);
        Assertions.assertTrue((((byte) (0x80)) & 0b1000_0000) != 0);
    }
}
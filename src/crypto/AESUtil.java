package crypto;

public final class AESUtil {

    public static final int IRREDUCIBLE_POLYNOMIAL = 0b1_0001_1011;
    public static final byte[][] AES_MATRIX = new byte[][]{
            {0x2, 0x3, 0x1, 0x1}, {0x1, 0x2, 0x3, 0x1}, {0x1, 0x1, 0x2, 0x3}, {0x3, 0x1, 0x1, 0x2}
    };

    public static final byte[][] INVERSE_AES_MATRIX = new byte[][]{
            {0x0E, 0x0B, 0x0D, 0x09}, {0x09, 0x0E, 0x0B, 0x0D}, {0x0D, 0x09, 0x0E, 0x0B}, {0x0B, 0x0D, 0x09, 0x0E}
    };

    private static final int[] sBox = {
            0x63, 0x7c, 0x77, 0x7b, 0xf2, 0x6b, 0x6f, 0xc5, 0x30, 0x01, 0x67, 0x2b, 0xfe, 0xd7, 0xab, 0x76,

            0xca, 0x82, 0xc9, 0x7d, 0xfa, 0x59, 0x47, 0xf0, 0xad, 0xd4, 0xa2, 0xaf, 0x9c, 0xa4, 0x72, 0xc0,

            0xb7, 0xfd, 0x93, 0x26, 0x36, 0x3f, 0xf7, 0xcc, 0x34, 0xa5, 0xe5, 0xf1, 0x71, 0xd8, 0x31, 0x15,

            0x04, 0xc7, 0x23, 0xc3, 0x18, 0x96, 0x05, 0x9a, 0x07, 0x12, 0x80, 0xe2, 0xeb, 0x27, 0xb2, 0x75,

            0x09, 0x83, 0x2c, 0x1a, 0x1b, 0x6e, 0x5a, 0xa0, 0x52, 0x3b, 0xd6, 0xb3, 0x29, 0xe3, 0x2f, 0x84,

            0x53, 0xd1, 0x00, 0xed, 0x20, 0xfc, 0xb1, 0x5b, 0x6a, 0xcb, 0xbe, 0x39, 0x4a, 0x4c, 0x58, 0xcf,

            0xd0, 0xef, 0xaa, 0xfb, 0x43, 0x4d, 0x33, 0x85, 0x45, 0xf9, 0x02, 0x7f, 0x50, 0x3c, 0x9f, 0xa8,

            0x51, 0xa3, 0x40, 0x8f, 0x92, 0x9d, 0x38, 0xf5, 0xbc, 0xb6, 0xda, 0x21, 0x10, 0xff, 0xf3, 0xd2,

            0xcd, 0x0c, 0x13, 0xec, 0x5f, 0x97, 0x44, 0x17, 0xc4, 0xa7, 0x7e, 0x3d, 0x64, 0x5d, 0x19, 0x73,

            0x60, 0x81, 0x4f, 0xdc, 0x22, 0x2a, 0x90, 0x88, 0x46, 0xee, 0xb8, 0x14, 0xde, 0x5e, 0x0b, 0xdb,

            0xe0, 0x32, 0x3a, 0x0a, 0x49, 0x06, 0x24, 0x5c, 0xc2, 0xd3, 0xac, 0x62, 0x91, 0x95, 0xe4, 0x79,

            0xe7, 0xc8, 0x37, 0x6d, 0x8d, 0xd5, 0x4e, 0xa9, 0x6c, 0x56, 0xf4, 0xea, 0x65, 0x7a, 0xae, 0x08,

            0xba, 0x78, 0x25, 0x2e, 0x1c, 0xa6, 0xb4, 0xc6, 0xe8, 0xdd, 0x74, 0x1f, 0x4b, 0xbd, 0x8b, 0x8a,

            0x70, 0x3e, 0xb5, 0x66, 0x48, 0x03, 0xf6, 0x0e, 0x61, 0x35, 0x57, 0xb9, 0x86, 0xc1, 0x1d, 0x9e,

            0xe1, 0xf8, 0x98, 0x11, 0x69, 0xd9, 0x8e, 0x94, 0x9b, 0x1e, 0x87, 0xe9, 0xce, 0x55, 0x28, 0xdf,

            0x8c, 0xa1, 0x89, 0x0d, 0xbf, 0xe6, 0x42, 0x68, 0x41, 0x99, 0x2d, 0x0f, 0xb0, 0x54, 0xbb, 0x16
    };

    private static final int[] inverseSBox = {
            82, 9, 106, 213, 48, 54, 165, 56, 191, 64, 163, 158, 129, 243, 215, 251, 124, 227, 57, 130, 155,

            47, 255, 135, 52, 142, 67, 68, 196, 222, 233, 203, 84, 123, 148, 50, 166, 194, 35, 61, 238, 76,

            149, 11, 66, 250, 195, 78, 8, 46, 161, 102, 40, 217, 36, 178, 118, 91, 162, 73, 109, 139, 209,

            37, 114, 248, 246, 100, 134, 104, 152, 22, 212, 164, 92, 204, 93, 101, 182, 146, 108, 112, 72,

            80, 253, 237, 185, 218, 94, 21, 70, 87, 167, 141, 157, 132, 144, 216, 171, 0, 140, 188, 211, 10,

            247, 228, 88, 5, 184, 179, 69, 6, 208, 44, 30, 143, 202, 63, 15, 2, 193, 175, 189, 3, 1, 19, 138,

            107, 58, 145, 17, 65, 79, 103, 220, 234, 151, 242, 207, 206, 240, 180, 230, 115, 150, 172, 116,

            34, 231, 173, 53, 133, 226, 249, 55, 232, 28, 117, 223, 110, 71, 241, 26, 113, 29, 41, 197, 137,

            111, 183, 98, 14, 170, 24, 190, 27, 252, 86, 62, 75, 198, 210, 121, 32, 154, 219, 192, 254, 120,

            205, 90, 244, 31, 221, 168, 51, 136, 7, 199, 49, 177, 18, 16, 89, 39, 128, 236, 95, 96, 81, 127,

            169, 25, 181, 74, 13, 45, 229, 122, 159, 147, 201, 156, 239, 160, 224, 59, 77, 174, 42, 245, 176,

            200, 235, 187, 60, 131, 83, 153, 97, 23, 43, 4, 126, 186, 119, 214, 38, 225, 105, 20, 99, 85, 33, 12, 125
    };

    private AESUtil() {}

    /**
     * Encrypts a plaintext via AES to the ciphertext.
     *
     * @param plaintext Plaintext that will be encrypted
     */
    public static void encrypt(byte[][] plaintext, byte[][] key) {
        byte[][] keySchedule = genKeySchedule(key);
        addRoundKey(plaintext, keySchedule, 0);

        for (int i = 0; i < 9; i++) {
            subBytes(plaintext);
            shiftRows(plaintext);
            mixColumns(plaintext, AES_MATRIX);
            addRoundKey(plaintext, keySchedule, i + 1);
        }

        subBytes(plaintext);
        shiftRows(plaintext);
        addRoundKey(plaintext, keySchedule, 10);
    }

    public static void decrypt(byte[][] plaintext, byte[][] key) {
        byte[][] keySchedule = genKeySchedule(key);
        addRoundKey(plaintext, keySchedule, 10);
        inverseShiftRows(plaintext);
        inverseSubBytes(plaintext);

        for (int i = 8; i >= 0; i--) {
            addRoundKey(plaintext, keySchedule, i + 1);
            mixColumns(plaintext, INVERSE_AES_MATRIX);
            inverseShiftRows(plaintext);
            inverseSubBytes(plaintext);
        }

        addRoundKey(plaintext, keySchedule, 0);
    }

    public static final byte[][] roundConstants = {
            {0x01, 0x00, 0x00, 0x00},
            {0x02, 0x00, 0x00, 0x00},
            {0x04, 0x00, 0x00, 0x00},
            {0x08, 0x00, 0x00, 0x00},
            {0x10, 0x00, 0x00, 0x00},
            {0x20, 0x00, 0x00, 0x00},
            {0x40, 0x00, 0x00, 0x00},
            {(byte) 0x80, 0x00, 0x00, 0x00},
            {0x1b, 0x00, 0x00, 0x00},
            {0x36, 0x00, 0x00, 0x00}
    };

    public static byte[][] genKeySchedule(byte[][] key) {
        byte[][] keySchedule = new byte[4][44]; // fill first 4 columns with keybytes
        for (int i = 0; i < 4; i++) {
            System.arraycopy(key[i], 0, keySchedule[i], 0, 4);
        }

        for (int roundKeyIndex = 4; roundKeyIndex < 44; roundKeyIndex++) {
            if (roundKeyIndex % 4 == 0) {
                byte[] col = getColumn(keySchedule, roundKeyIndex - 1);
                col = rotColumn(col);
                for (int i = 0; i < 4; i++) {
                    col[i] = subByte(col[i]);
                }
                for (int i = 0; i < 4; i++) {
                    keySchedule[i][roundKeyIndex] =
                            (byte) (getColumn(keySchedule, roundKeyIndex - 4)[i] ^ col[i] ^ roundConstants[roundKeyIndex / 4 - 1][i]);
                }
            } else {
                for (int i = 0; i < 4; i++) {
                    keySchedule[i][roundKeyIndex] =
                            (byte) (keySchedule[i][roundKeyIndex - 1] ^ keySchedule[i][roundKeyIndex - 4]);
                }
            }
        }
        return keySchedule;
    }

    public static void addRoundKey(byte[][] plaintext, byte[][] keySchedule, int roundNumber) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                plaintext[i][j] ^= keySchedule[i][4 * roundNumber + j];
            }
        }
    }

    public static byte[] getColumn(byte[][] input, int columnIndex) {
        return new byte[]{input[0][columnIndex], input[1][columnIndex], input[2][columnIndex], input[3][columnIndex]};
    }

    public static void subBytes(byte[][] plaintext) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                plaintext[i][j] = subByte(plaintext[i][j]);
            }
        }
    }

    public static void inverseSubBytes(byte[][] plaintext) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                plaintext[i][j] = inverseSubByte(plaintext[i][j]);
            }
        }
    }

    public static byte inverseSubByte(byte input) {
        return (byte) inverseSBox[Byte.toUnsignedInt(input)];
    }

    public static byte subByte(byte input) {
        return (byte) sBox[Byte.toUnsignedInt(input)];
    }

    public static void shiftRows(byte[][] plaintext) {
        for (int i = 1; i < 4; i++) {
            plaintext[i] = shiftRow(plaintext[i], i);
        }
    }

    public static void inverseShiftRows(byte[][] plaintext) {
        for (int i = 1; i < 4; i++) {
            plaintext[i] = shiftRow(plaintext[i], -i);
        }
    }

    public static byte[] rotColumn(byte[] column) {
        return shiftRow(column, 1);
    }

    public static byte[] shiftRow(byte[] row, int amount) {
        amount = amount % 4;
        byte[] rowCopy = row.clone();
        for (int i = 0; i < 4; i++) {
            rowCopy[i] = row[(4 + i + amount) % 4];
        }
        return rowCopy;
    }

    public static void mixColumns(byte[][] plaintext, byte[][] MATRIX) {
        byte[][] ciphertext = new byte[4][4];
        for (int row = 0; row < 4; row++) {
            for (int column = 0; column < 4; column++) {
                ciphertext[row][column] = multColumnGF(plaintext, column, row, MATRIX);
            }
        }
        for (int i = 0; i < 4; i++) {
            System.arraycopy(ciphertext[i], 0, plaintext[i], 0, 4);
        }
    }

    public static byte multColumnGF(byte[][] plaintext, int plaintextColumnIndex, int aesRowIndex, byte[][] MATRIX) {
        byte sum = 0;
        for (int i = 0; i < 4; i++) {
            sum ^= multGF256(plaintext[i][plaintextColumnIndex], MATRIX[aesRowIndex][i]);
        }
        return sum;
    }

    public static byte multGF256(byte a, byte b) {
        int sol = 0;
        for (int i = 0; i < 8; i++) {
            if ((a & (0b1 << i)) != 0) {
                sol ^= Byte.toUnsignedInt(b) << i;
            }
        }
        return modGF256AES(sol);
    }

    public static byte modGF256AES(int sol) {
        for (int i = 4 * 8 - 1; i >= 0; i--) {
            if ((sol & (0x100 << i)) != 0) {
                sol ^= IRREDUCIBLE_POLYNOMIAL << i;
            }
        }
        return (byte) sol;
    }
}

package crypto;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public final class CTRMode {

    private CTRMode() {}

    public static class Encryptor implements Function<Boolean, Boolean> {
        private final PadGenerator generator;

        public Encryptor(PadGenerator generator) {
            this.generator = generator;
        }

        @Override
        public Boolean apply(Boolean aBoolean) {
            return generator.get() ^ aBoolean;
        }
    }

    public static class PadGenerator implements Supplier<Boolean> {
        private final byte[][] key;
        private BigInteger counter; // 128 bit
        private static final BigInteger modulus;

        static {
            BigInteger modulus1;
            modulus1 = BigInteger.TWO;
            modulus1 = modulus1.pow(128);
            modulus = modulus1;
        }

        public PadGenerator(byte[][] key, BigInteger counter) {
            this.key = key;
            this.counter = counter;
        }

        private final byte[][] cachedPad = new byte[4][4];
        private int bitCounter = 0; // [0, 4*4*8)


        @Override
        public Boolean get() {
            boolean needToGen = bitCounter == 0;
            if (needToGen) {
                byte[] arr = counter.toByteArray(); // first Byte is the sign. Ignore!
                counter = counter.add(BigInteger.ONE).mod(modulus);
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 4; j++) {
                        cachedPad[3 - i][3 - j] = i * 4 + j + 1 >= arr.length ? 0 : arr[i * 4 + j + 1];
                    }
                }
                AESUtil.encrypt(cachedPad, key);
            }
            boolean bit = getBit(cachedPad[bitCounter / 4 / 8][(bitCounter / 8) % 4], bitCounter % 8);
            bitCounter = (bitCounter + 1) % 128;
            return bit;
        }

        private boolean getBit(byte b, int bit) {
            return (b & (1 << (7 - bit))) != 0;
        }
    }

    private static final SecureRandom random = new SecureRandom();

    public static BigInteger getSeed() {
        return new BigInteger(1, random.generateSeed(16));
    }

    public static ArrayList<Boolean> encryptOrDecrypt(ArrayList<Boolean> plaintextOrCiphertext, BigInteger seed) {
        CTRMode.Encryptor encryptor = new CTRMode.Encryptor(new CTRMode.PadGenerator(AESTest.key, seed));
        return plaintextOrCiphertext.stream().map(encryptor).collect(Collectors.toCollection(ArrayList::new));
    }

}

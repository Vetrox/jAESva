package crypto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import static crypto.Modes.boolArrToStr;
import static crypto.Modes.strToBoolArr;

class CTRModeTest {

    @Test
    void bigInt() {
        BigInteger modulus1;
        modulus1 = BigInteger.TWO;
        modulus1 = modulus1.pow(128);
        modulus1 = modulus1.subtract(BigInteger.ONE);
        Assertions.assertEquals(128, modulus1.bitLength());
        System.out.println(Arrays.toString(modulus1.toByteArray()));
    }

    @Test
    void encrypt() {
        ArrayList<Boolean> m = strToBoolArr(ModesTest.message);
        BigInteger seed = CTRMode.getSeed();
        CTRMode.Encryptor encryptor = new CTRMode.Encryptor(new CTRMode.PadGenerator(AESTest.key, seed));
        ArrayList<Boolean> encrypted = m.stream().map(encryptor).collect(Collectors.toCollection(ArrayList::new));
        System.out.println(boolArrToStr(encrypted));

        encryptor = new CTRMode.Encryptor(new CTRMode.PadGenerator(AESTest.key, seed));
        ArrayList<Boolean> decrypted =
                encrypted.stream().map(encryptor).collect(Collectors.toCollection(ArrayList::new));

        System.out.println(boolArrToStr(decrypted));
    }

    @Test
    void equalDistribution() {
        int t = 0, f = 0;
        final int TOTAL = 100000;
        BigInteger seed = CTRMode.getSeed();
        CTRMode.PadGenerator generator = new CTRMode.PadGenerator(AESTest.key, seed);
        for (int i = 0; i < TOTAL; i++) {
            if (generator.get()) {
                t++;
            } else {
                f++;
            }
        }
        double relativeT = (double) t / TOTAL, relativeF = (double) f / TOTAL;

        System.out.println("T: " + relativeT + "\nF: " + relativeF);

        Assertions.assertEquals(0.5, relativeT, 0.01);
        Assertions.assertEquals(0.5, relativeF, 0.01);
    }

}
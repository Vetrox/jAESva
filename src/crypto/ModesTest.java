package crypto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static crypto.Modes.boolArrToStr;
import static crypto.Modes.strToBoolArr;

class ModesTest {

    private static final String message = "Never gonna give you up!";

    @Test
    void padMessage() {
        ArrayList<Boolean> m = strToBoolArr(message);
        Modes.padMessage(m);
        Assertions.assertEquals(256, m.size());
    }

    @Test
    void unPadMessage() {
        ArrayList<Boolean> m = strToBoolArr(message);
        Modes.padMessage(m);
        Modes.unPadMessage(m);
        Assertions.assertEquals(strToBoolArr(message), m);
    }

    @Test
    void splitAndMergeECB() {
        ArrayList<Boolean> m = strToBoolArr(message);
        Modes.padMessage(m);
        ArrayList<byte[][]> blocks = ECBMode.split(m);
        ArrayList<Boolean> m1 = ECBMode.merge(blocks);
        Assertions.assertEquals(m, m1);
    }

    @Test
    void encryptDecryptECB() {
        System.out.println("Plaintext: " + message);
        ArrayList<Boolean> m = strToBoolArr(message);
        ArrayList<Boolean> encrypted = ECBMode.encrypt(m, AESTest.key);
        System.out.println("Ciphertext: " + Modes.boolArrToStr(encrypted));

        ArrayList<Boolean> decrypted = ECBMode.decrypt(encrypted, AESTest.key);
        Assertions.assertEquals(strToBoolArr(message), decrypted);
        System.out.println("Decrypted Ciphertext: " + boolArrToStr(decrypted));
    }

    @Test
    void encryptDecryptCBC() {
        System.out.println("Plaintext: " + message);
        ArrayList<Boolean> m = strToBoolArr(message);
        byte[][] initialValue = CBCMode.generateInitialValue();
        System.out.println("InitialValue: " + Arrays.deepToString(initialValue));

        ArrayList<Boolean> encrypted = CBCMode.encrypt(m, AESTest.key, initialValue);
        System.out.println("Ciphertext: " + Modes.boolArrToStr(encrypted));

        ArrayList<Boolean> decrypted = CBCMode.decrypt(encrypted, AESTest.key, initialValue);
        System.out.println("Decrypted Ciphertext: " + boolArrToStr(decrypted));
        Assertions.assertEquals(strToBoolArr(message), decrypted);
    }

    @Test
    void strToBoolArrToStr() {
        ArrayList<Boolean> m = strToBoolArr(message);
        String s = boolArrToStr(m);
        Assertions.assertEquals(message, s);
    }

}
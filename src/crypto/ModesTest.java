package crypto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

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
    void encryptECB() {
        ArrayList<Boolean> m = strToBoolArr(message);
        ArrayList<Boolean> encrypted = ECBMode.encrypt(m, AESTest.key);
        System.out.println(Modes.boolArrToStr(encrypted)); // TODO: We have no decrypt atm. Implement and test here.
    }

    @Test
    void strToBoolArrToStr() {
        ArrayList<Boolean> m = strToBoolArr(message);
        String s = boolArrToStr(m);
        Assertions.assertEquals(message, s);
    }

}
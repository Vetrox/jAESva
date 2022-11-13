package crypto;

import java.util.ArrayList;

public interface Modes {
    int blockSizeBits = 128;

    static void padMessage(ArrayList<Boolean> message) {
        message.add(true);
        while (message.size() % blockSizeBits != 0) {
            message.add(false);
        }
    }

    static void unPadMessage(ArrayList<Boolean> message) {
        while (!message.get(message.size() - 1)) {
            message.remove(message.size() - 1);
        }
        message.remove(message.size() - 1);
    }

    static byte getByte(ArrayList<Boolean> message, int start) {
        byte ret = 0;
        for (int i = 0; i < 8; i++) {
            ret |= (message.get(start + i) ? 1 : 0) << (7 - i);
        }
        return ret;
    }

    static ArrayList<Boolean> strToBoolArr(String message) {
        ArrayList<Boolean> m = new ArrayList<>();
        byte[] bytes = message.getBytes();
        for (byte aByte : bytes) {
            for (int j = 0; j < 8; j++) {
                m.add((aByte & (1 << (7 - j))) != 0);
            }
        }
        return m;
    }

    static String boolArrToStr(ArrayList<Boolean> message) {
        byte[] ret = new byte[message.size() / 8];
        for (int i = 0; i < message.size() / 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (message.get(i * 8 + j)) {
                    ret[i] |= 1 << (7 - j);
                }
            }
        }
        return new String(ret);
    }
}
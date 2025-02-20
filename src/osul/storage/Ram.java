package osul.storage;

import java.util.HashMap;
import java.util.Map;

public class Ram {
    Map<Integer, Boolean> ramMap = new HashMap<>();
    HashMap<Integer, Integer> readData = new HashMap<>();


    public Ram() {
        storeWord(0x100, 100);
    }

    public void storeWord(int index, int word) {
        store(index * 8, word, 32);
    }

    public int loadWord(int index) {
        int value = load(index * 8, 32);
        readData.put(index, value);
        return value;
    }

    public void storeByte(int index, int word) {
        store(index, word, 8);
    }

    public int loadByte(int index) {
        return load(index, 8);
    }

    private void store(int index, int data, int dataSize) {
        for (int i = 0; i < dataSize; i++) {
            ramMap.put(index + i, (data & (1 << (dataSize - i - 1))) != 0);
        }
    }


    private int load(int index, int dataSize) {
        int result = 0;
        for (int i = 0; i < dataSize; i++) {
            result <<= 1;
            if (ramMap.getOrDefault(index + i, false)) {
                result |= 1;
            }
        }
        return result;
    }



    public HashMap<String, Integer> getReadData() {
        HashMap<String, Integer> result = new HashMap<>();
        readData.forEach((k, v) -> {
            result.put(""+k, v);
        });
        return result;
    }

    public void flush() {
        readData = new HashMap<>();
    }
}

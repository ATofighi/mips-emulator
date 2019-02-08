package osul.storage;

import osul.parser.ParseException;
import osul.parser.Parser;

import java.util.HashMap;
import java.util.Map;

public class Registers {
    private int[] registers = new int[32];

    public Registers() {
        set(29, 0x00ff_0000); // $sp = 00FF 0000_hex
    }

    public int get(int k) {
        return registers[k];
    }

    public int get(String a) {
        try {
            return get(Parser.getRegisterNumber(a));
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void set(int k, int v) {
        if(k != 0) {
            registers[k] = v;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        String comma = "";
        for(int i = 0; i < 32; i++) {
            sb.append(comma);
            sb.append(Parser.registerNames[i]);
            sb.append("=");
            sb.append(registers[i]);
            comma = ", ";
        }
        sb.append("}");
        return sb.toString();
    }
}

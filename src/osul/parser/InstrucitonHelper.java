package osul.parser;

public class InstrucitonHelper {
    private int instruction;
    public InstrucitonHelper(int instruction) {
        this.instruction = instruction;
    }

    public int getOpcode() {
        return instruction >>> 26;
    }

    public int getFunct() {
        return instruction & 0b111111;
    }

    public String getInstructionName() {
        switch (getOpcode()) {
            case 0:
                switch (getFunct()) {
                    case 0x20:
                        return "add";
                    case 0x24:
                        return "and";
                    case 0x27:
                        return "nor";
                    case 0x25:
                        return "or";
                    case 0x2a:
                        return "slt";
                    case 0x22:
                        return "sub";
                }
                break;
            case 0x23:
                return "lw";
            case 0x4:
                return "beq";
            case 0x2b:
                return "sw";
        }
        return "";
    }

    public int getSignExtendedImmediate() {
        int immediate = instruction & 0b1111111111111111;
        int sign = immediate >>> 15;
        for(int i = 16; i < 32; i++) {
            immediate |= sign << i;
        }
        return immediate;
    }

    public int getRd() {
        return (instruction >>> 11) & 0b11111;
    }

    public int getRt() {
        return (instruction >>> 16) & 0b11111;
    }

    public int getRs() {
        return (instruction >>> 21) & 0b11111;
    }
}

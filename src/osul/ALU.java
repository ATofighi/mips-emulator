package osul;

public class ALU {
    public static int calc(int x, int y, int op0, int op1, int funct) {
        if (op0 == 0 && op1 == 1) { // r-type
            switch (funct) {
                case 0x20: // add
                    return x + y;
                case 0x24: // and
                    return x & y;
                case 0x27: // not
                    return ~(x | y);
                case 0x25: // or
                    return x | y;
                case 0x2a: //slt
                    return (x < y) ? 1 : 0;
                case 0x22:
                    return x - y;
            }
            return 0;
        } else if (op0 == 0 && op1 == 0) {
            return x + y;
        } else if (op0 == 1 && op1 == 0) {
            return x - y;
        }
        return 0;
    }
}

package osul;

import osul.parser.InstrucitonHelper;
import osul.storage.Registers;

import java.util.Map;

public class Result {
    int clockId;
    private final String registers;
    private final Map<String, Integer> ifid;
    private final Map<String, Integer> idex;
    private final Map<String, Integer> exmem;
    private final Map<String, Integer> memwb;
    private Map<String, Integer> oldIfid;
    private Map<String, Integer> oldIdex;
    private Map<String, Integer> oldExmem;
    private Map<String, Integer> oldMemwb;
    private Map<String, Integer> registerRead;
    private int pc;

    public Result(int clockId, Registers registers, Map<String, Integer> ifid, Map<String, Integer> idex, Map<String, Integer> exmem, Map<String, Integer> memwb,
                  Map<String, Integer> oldIfid, Map<String, Integer> oldIdex, Map<String, Integer> oldExmem, Map<String, Integer> oldMemwb,
                  int pc) {

        this.clockId = clockId;
        this.registers = registers.toString();
        this.ifid = ifid;
        this.idex = idex;
        this.exmem = exmem;
        this.memwb = memwb;
        this.oldIfid = oldIfid;
        this.oldIdex = oldIdex;
        this.oldExmem = oldExmem;
        this.oldMemwb = oldMemwb;

        this.registerRead = registers.getReadData();
        this.pc = pc;
    }

    @Override
    public String toString() {
        return "Result{" +
                "clockId=" + (clockId + 1) +
                ", instructions={" + getInstructions() + "}" +
                ", registers=" + registers +
                ", registerReads=" + mapString(registerRead) +
                ", ifid=" + mapString(ifid) +
                ", idex=" + mapString(idex) +
                ", exmem=" + mapString(exmem) +
                ", memwb=" + mapString(memwb) +
                ", pc=" + pc +
                '}';
    }

    private String getInstructions() {
        return "IF=" + new InstrucitonHelper(ifid.getOrDefault("instruction", 0)) +
                ", ID=" + new InstrucitonHelper(oldIfid.getOrDefault("instruction", 0)) +
                ", EX=" + new InstrucitonHelper(oldIdex.getOrDefault("instruction", 0)) +
                ", MEM=" + new InstrucitonHelper(oldExmem.getOrDefault("instruction", 0)) +
                ", WB=" + new InstrucitonHelper(oldMemwb.getOrDefault("instruction", 0));
    }

    private String mapString(Map<String, Integer> m) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        String comma = "";
        for (Map.Entry<String, Integer> entry : m.entrySet()) {
            sb.append(comma);
            sb.append(entry.getKey());
            sb.append("=");
            sb.append(entry.getValue());
            comma = ", ";
        }
        sb.append("}");
        return sb.toString();
    }
}

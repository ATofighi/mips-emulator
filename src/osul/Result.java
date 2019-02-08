package osul;

import osul.storage.Registers;

import java.util.Map;

public class Result {
    int clockId;
    private final Registers registers;
    private final Map<String, Integer> ifid;
    private final Map<String, Integer> idex;
    private final Map<String, Integer> exmem;
    private final Map<String, Integer> memwb;

    public Result(int clockId, Registers registers, Map<String, Integer> ifid, Map<String, Integer> idex, Map<String, Integer> exmem, Map<String, Integer> memwb) {

        this.clockId = clockId;
        this.registers = registers;
        this.ifid = ifid;
        this.idex = idex;
        this.exmem = exmem;
        this.memwb = memwb;
    }

    @Override
    public String toString() {
        return "Result{" +
                "clockId=" + clockId +
                ", registers=" + registers +
                ", ifid=" + mapString(ifid) +
                ", idex=" + mapString(idex) +
                ", exmem=" + mapString(exmem) +
                ", memwb=" + mapString(memwb) +
                '}';
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

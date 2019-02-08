package osul.parser;


public class Parser {
    public static int lineToCode(String line, LinesTable linesTable) throws ParseException {
        line = line.trim().toLowerCase();
        String lineName = null;
        if (line.contains(":")) {
            String[] seperatedLine = line.split(":");
            lineName = seperatedLine[0];
            line = seperatedLine[1];
        }


        if (lineName != null) {
            if (linesTable.containsKey(lineName)) {
                throw new ParseException("Duplicate lineName: " + lineName);
            }
            linesTable.put(lineName, 0);
        }

        String instructionName = line.split(" ")[0];
        InstructionParser instructionParser;
        switch (instructionName) {
            case "add":
                instructionParser = new AddParser(line, linesTable);
                break;
            case "sub":
                instructionParser = new SubParser(line, linesTable);
                break;
            case "and":
                instructionParser = new AndParser(line, linesTable);
                break;
            case "or":
                instructionParser = new OrParser(line, linesTable);
                break;
            case "nor":
                instructionParser = new NorParser(line, linesTable);
                break;
            case "slt":
                instructionParser = new SltParser(line, linesTable);
                break;
            case "beq":
                instructionParser = new BeqParser(line, linesTable);
                break;
            case "lw":
                instructionParser = new LwParser(line, linesTable);
                break;
            case "sw":
                instructionParser = new SwParser(line, linesTable);
                break;
            default:
                throw new ParseException("Unknown Instruction: " + instructionName);
        }

        int result = instructionParser.getInstructionCode();
        return result;
    }


    public static final String[] registerNames = new String[]{
            "$0", "$at", "$v0", "$v1", "$a0", "$a1", "$a2", "$a3", "$t0", "$t1", "$t2", "$t3", "$t4",
            "$t5", "$t6", "$t7", "$s0", "$s1", "$s2", "$s3", "$s4", "$s5", "$s6", "$s7",
            "$t8", "$t9", "$k0", "$k1", "$gp", "$sp", "$fp", "$ra"
    };

    public static int getRegisterNumber(String registerName) throws ParseException {
        registerName = registerName.trim();
        switch (registerName) {
            case "$0":
            case "$zero":
                return 0;
            case "$at":
            case "$1":
                return 1;
            case "$v0":
            case "$2":
                return 2;
            case "$v1":
            case "$3":
                return 3;
            case "$a0":
            case "$4":
                return 4;
            case "$a1":
            case "$5":
                return 5;
            case "$a2":
            case "$6":
                return 6;
            case "$a3":
            case "$7":
                return 7;
            case "$t0":
            case "$8":
                return 8;
            case "$t1":
            case "$9":
                return 9;
            case "$t2":
            case "$10":
                return 10;
            case "$t3":
            case "$11":
                return 11;
            case "$t4":
            case "$12":
                return 12;
            case "$t5":
            case "$13":
                return 13;
            case "$t6":
            case "$14":
                return 14;
            case "$t7":
            case "$15":
                return 15;
            case "$s0":
            case "$16":
                return 16;
            case "$s1":
            case "$17":
                return 17;
            case "$s2":
            case "$18":
                return 18;
            case "$s3":
            case "$19":
                return 19;
            case "$s4":
            case "$20":
                return 20;
            case "$s5":
            case "$21":
                return 21;
            case "$s6":
            case "$22":
                return 22;
            case "$s7":
            case "$23":
                return 23;
            case "$t8":
            case "$24":
                return 24;
            case "$t9":
            case "$25":
                return 25;
            case "$k0":
            case "$26":
                return 26;
            case "$k1":
            case "$27":
                return 27;
            case "$gp":
            case "$28":
                return 28;
            case "$sp":
            case "$29":
                return 29;
            case "$fp":
            case "$30":
                return 30;
            case "$ra":
            case "$31":
                return 31;
        }
        throw new ParseException("Register is not valid: " + registerName);
    }
}

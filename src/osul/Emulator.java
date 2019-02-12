package osul;

import osul.parser.InstrucitonHelper;
import osul.parser.LinesTable;
import osul.parser.ParseException;
import osul.parser.Parser;
import osul.storage.Ram;
import osul.storage.Registers;
import osul.swing.Editor;
import osul.swing.FileChooser;

import java.io.FileNotFoundException;
import java.util.*;

public class Emulator {

    private static final int startLine = 0x10;


    private Ram ram = new Ram();
    private Registers registers = new Registers();
    private int currentClock = 0;
    List<Result> resultList = new ArrayList<>();

    private Map<String, Integer> IFID = new HashMap<>();
    private Map<String, Integer> IDEX = new HashMap<>();
    private Map<String, Integer> EXMEM = new HashMap<>();
    private Map<String, Integer> MEMWB = new HashMap<>();
    private Map<String, Integer> newIFID = new HashMap<>();
    private Map<String, Integer> newIDEX = new HashMap<>();
    private Map<String, Integer> newEXMEM = new HashMap<>();
    private Map<String, Integer> newMEMWB = new HashMap<>();

    private int pcSrc = 0;
    private int pc = startLine;

    private int writeBackData = 0;
    private Editor editor;

    public Emulator(Editor editor, FileChooser outputFile) {
        this.editor = editor;
    }

    public void assemble() throws FileNotFoundException, ParseException {
        Scanner scanner = new Scanner(editor.getTextArea().getText());
        LinesTable linesTable = new LinesTable();
        for (int i = startLine; scanner.hasNextLine(); i += 4) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) {
                i -= 4;
                continue;
            }
            ram.storeWord(i, Parser.lineToCode(line, linesTable));
        }
    }

    public void clock() {
        writeBack();
        memoryAccess();
        execution();
        instructionDecode();
        instructionFetch();
        resultList.add(new Result(currentClock, registers, newIFID, newIDEX, newEXMEM, newMEMWB, IFID, IDEX, EXMEM, MEMWB, pc));
        flush();
        currentClock++;
        registers.flush();
    }

    private void flush() {
        IFID = newIFID;
        IDEX = newIDEX;
        EXMEM = newEXMEM;
        MEMWB = newMEMWB;
        newIFID = new HashMap<>();
        newIDEX = new HashMap<>();
        newEXMEM = new HashMap<>();
        newMEMWB = new HashMap<>();
    }

    private void instructionFetch() {
        if (pcSrc == 0) {
        } else {
            pc = newMEMWB.getOrDefault("addedPc", 0);
        }
        int insturction = ram.loadWord(pc);
        newIFID.put("instruction", insturction);
        pc += 4;

        newIFID.put("pc", pc);
    }

    private void instructionDecode() {

        // Control Unit
        int regWrite = 0;
        int memToReg = 0;
        int memWrite = 0;
        int memRead = 0;
        int ALUOp0 = 0;
        int ALUOp1 = 0;
        int regDst = 0;
        int ALUSrc = 0;
        int branch = 0;
//        int PCSrc = 0;
        newIDEX.putAll(IFID);
        int instruction = IFID.getOrDefault("instruction", 0);
        InstrucitonHelper instrucitonHelper = new InstrucitonHelper(instruction);
        if (hazardDetection() || beqHazard()) {
            pc -= 4;
            newIDEX.put("instruction", 0);
            // stall
        } else {
            if (instrucitonHelper.getOpcode() == 0) { // R-type
                if (!instrucitonHelper.getInstructionName().isEmpty()) { // R-Type but not stall
                    ALUOp0 = 0;
                    ALUOp1 = 1;
                }
            } else {
                switch (instrucitonHelper.getInstructionName()) { // I-type
                    case "lw":
                    case "sw":
                        ALUOp0 = ALUOp1 = 0;
                        break;
                    case "beq":
                        ALUOp0 = 1;
                        ALUOp1 = 0;
                        break;
                }

            }

            switch (instrucitonHelper.getInstructionName()) {
                case "add":
                case "sub":
                case "and":
                case "or":
                case "nor":
                case "slt":
                    regWrite = 1;
                    ALUSrc = 0;
                    memWrite = 0;
                    memToReg = 0;
                    memRead = 0;
                    branch = 0;
                    regDst = 1;
                    break;
                case "beq":
                    regWrite = 0;
                    ALUSrc = 0;
                    memWrite = 0;
                    memToReg = 0;
                    memRead = 0;
                    branch = 1;
                    regDst = 0;
                    break;
                case "lw":
                    regWrite = 1;
                    ALUSrc = 1;
                    memWrite = 0;
                    memToReg = 1;
                    memRead = 1;
                    branch = 0;
                    regDst = 0;
                    break;
                case "sw":
                    regWrite = 0;
                    ALUSrc = 1;
                    memWrite = 1;
                    memToReg = 0;
                    memRead = 0;
                    branch = 0;
                    regDst = 0;
                    break;
            }
        }

        newIDEX.put("regWrite", regWrite);
        newIDEX.put("regDst", regDst);
        newIDEX.put("branch", branch);
        newIDEX.put("memRead", memRead);
        newIDEX.put("memWrite", memWrite);
        newIDEX.put("memToReg", memToReg);
        newIDEX.put("ALUOp0", ALUOp0);
        newIDEX.put("ALUOp1", ALUOp1);
        newIDEX.put("ALUSrc", ALUSrc);
        newIDEX.put("regWrite", regWrite);
        newIDEX.put("immediate", instrucitonHelper.getSignExtendedImmediate());
        newIDEX.put("rt", instrucitonHelper.getRt()); // 16 - 20
        newIDEX.put("rd", instrucitonHelper.getRd()); // 11 - 15
        newIDEX.put("rs", instrucitonHelper.getRs());
        newIDEX.put("data1", registers.get(instrucitonHelper.getRs()));
        newIDEX.put("data2", registers.get(instrucitonHelper.getRt()));
    }

    private void execution() {
        int calculatedForwarding = EXMEM.getOrDefault("calculated", 0);
        newEXMEM.putAll(IDEX);
        newEXMEM.put("addedPc", IDEX.getOrDefault("pc", 0) + (IDEX.getOrDefault("immediate", 0) << 2));
        {
            int x;
            switch (forwardingA()) {
                case 0:
                    x = IDEX.getOrDefault("data1", 0);
                    break;
                case 1:
                    x = writeBackData;
                    break;
                default:
                    x = calculatedForwarding;
            }

            int y;
            switch (forwardingB()) {
                case 0:
                    y = IDEX.getOrDefault("data2", 0);
                    break;
                case 1:
                    y = writeBackData;
                    break;
                default:
                    y = calculatedForwarding;
            }

            newEXMEM.put("data2", y);
            if (IDEX.getOrDefault("ALUSrc", 0) != 0) {
                y = IDEX.getOrDefault("immediate", 0);
            }
            int calculated = ALU.calc(x, y, IDEX.getOrDefault("ALUOp0", 0), IDEX.getOrDefault("ALUOp1", 0), IDEX.getOrDefault("immediate", 0) & 0b111111);
            newEXMEM.put("calculated", calculated);
            newEXMEM.put("zero", (calculated == 0) ? 1 : 0);
        }
        if (IDEX.getOrDefault("regDst", 0) == 0) {
            newEXMEM.put("dst", IDEX.getOrDefault("rt", 0));
        } else {
            newEXMEM.put("dst", IDEX.getOrDefault("rd", 0));
        }

    }

    private void memoryAccess() {
        newMEMWB.putAll(EXMEM);
        pcSrc = EXMEM.getOrDefault("zero", 0) & EXMEM.getOrDefault("branch", 0);

        int address = EXMEM.getOrDefault("calculated", 0);
        int writeData = EXMEM.getOrDefault("data2", 0);
        int memRead = EXMEM.getOrDefault("memRead", 0);
        int memWrite = EXMEM.getOrDefault("memWrite", 0);
        if (memWrite == 1) {
            ram.storeWord(address, writeData);
        }
        if (memRead == 1) {
            newMEMWB.put("data", ram.loadWord(address));
        }
    }

    private void writeBack() {
        writeBackData = 0;
        if (MEMWB.getOrDefault("regWrite", 0) == 1) {
            if (MEMWB.getOrDefault("memToReg", 0) == 1) {
                writeBackData = MEMWB.getOrDefault("data", 0);
            } else {
                writeBackData = MEMWB.getOrDefault("calculated", 0);
            }
            registers.set(MEMWB.getOrDefault("dst", 0), writeBackData);
        }
    }

    public Result getResult(int clockNumber) {
        return resultList.get(clockNumber - 1);
    }

    public int getClock() {
        return currentClock;
    }

    private boolean hazardDetection() {
        return (IDEX.getOrDefault("memRead", 0) == 1 &&
                (IDEX.getOrDefault("rt", 0).equals(new InstrucitonHelper(IFID.getOrDefault("instruction", 0)).getRs()) ||
                        IDEX.getOrDefault("rt", 0).equals(new InstrucitonHelper(IFID.getOrDefault("instruction", 0)).getRt())));
    }

    private int forwardingA() {
        int forwardA = 0;
        if (EXMEM.getOrDefault("regWrite", 0) == 1
                && EXMEM.getOrDefault("dst", 0) != 0
                && EXMEM.getOrDefault("dst", 0).equals(IDEX.getOrDefault("rs", 0)))
            forwardA = 2;
        else if (MEMWB.getOrDefault("regWrite", 0) == 1
                && MEMWB.getOrDefault("dst", 0) != 0
                && MEMWB.getOrDefault("dst", 0).equals(IDEX.getOrDefault("rs", 0)))
            forwardA = 1;
        return forwardA;
    }

    private int forwardingB() {
        int forwardB = 0;
        if (EXMEM.getOrDefault("regWrite", 0) == 1
                && EXMEM.getOrDefault("dst", 0) != 0
                && EXMEM.getOrDefault("dst", 0).equals(IDEX.getOrDefault("rt", 0)))
            forwardB = 2;
        else if (MEMWB.getOrDefault("regWrite", 0) == 1
                && MEMWB.getOrDefault("dst", 0) != 0
                && MEMWB.getOrDefault("dst", 0).equals(IDEX.getOrDefault("rt", 0)))
            forwardB = 1;
        return forwardB;
    }

    private boolean beqHazard() {
        return new InstrucitonHelper(IDEX.getOrDefault("instruction", 0)).getInstructionName().equals("beq")
                || new InstrucitonHelper(EXMEM.getOrDefault("instruction", 0)).getInstructionName().equals("beq")
                || new InstrucitonHelper(MEMWB.getOrDefault("instruction", 0)).getInstructionName().equals("beq");
    }
}

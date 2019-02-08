package osul;

import osul.parser.InstrucitonHelper;
import osul.parser.LinesTable;
import osul.parser.ParseException;
import osul.parser.Parser;
import osul.storage.Ram;
import osul.storage.Registers;
import osul.swing.FileChooser;

import java.io.FileNotFoundException;
import java.util.*;

public class Emulator {

    private static final int startLine = 0x10;

    private FileChooser inputFile;

    private Ram ram = new Ram();
    private Registers registers = new Registers();
    private int currentClock = 0;
    List<Result> resultList = new ArrayList<>();

    private Map<String, Integer> IFID = new HashMap<>();
    private Map<String, Integer> IDEX = new HashMap<>();
    private Map<String, Integer> EXMEM = new HashMap<>();
    private Map<String, Integer> MEMWB = new HashMap<>();

    private int pcSrc = 0;
    private int addedPc = 0;
    private int pc = startLine;

    public Emulator(FileChooser inputFile, FileChooser outputFile) {
        this.inputFile = inputFile;
    }

    public void assemble() throws FileNotFoundException, ParseException {
        Scanner scanner = new Scanner(inputFile.getFile());
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
        resultList.add(new Result(currentClock, registers, IFID, IDEX, EXMEM, MEMWB)); // TODO
        currentClock++;
    }

    private void instructionFetch() {
        IFID.clear();
        if (pcSrc == 0) {
        } else {
            pc = addedPc;
        }
        int insturction = ram.loadWord(pc);
        IFID.put("instruction", insturction);
        pc += 4;

        IFID.put("pc", pc);
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
        int instruction = IFID.getOrDefault("instruction", 0);
        InstrucitonHelper instrucitonHelper = new InstrucitonHelper(instruction);
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
        IDEX.clear();
        IDEX.putAll(IFID);
        IDEX.put("regWrite", regWrite);
        IDEX.put("regDst", regDst);
        IDEX.put("branch", branch);
        IDEX.put("memRead", memRead);
        IDEX.put("memWrite", memWrite);
        IDEX.put("memToReg", memToReg);
        IDEX.put("ALUOp0", ALUOp0);
        IDEX.put("ALUOp1", ALUOp1);
        IDEX.put("ALUSrc", ALUSrc);
        IDEX.put("regWrite", regWrite);
        IDEX.put("immediate", instrucitonHelper.getSignExtendedImmediate());
        IDEX.put("rt", instrucitonHelper.getRt()); // 16 - 20
        IDEX.put("rd", instrucitonHelper.getRd()); // 11 - 15
        IDEX.put("data1", registers.get(instrucitonHelper.getRs()));
        IDEX.put("data2", registers.get(instrucitonHelper.getRt()));
        // TODO: regWrite from writeBack
    }

    private void execution() {
        EXMEM.clear();
        EXMEM.putAll(IDEX);
        EXMEM.put("addedPc", IDEX.getOrDefault("pc", 0) + IDEX.getOrDefault("immediate", 0) << 2);
        addedPc = EXMEM.getOrDefault("addedPc", 0);
        {
            int x;
            if (IDEX.getOrDefault("ALUSrc", 0) == 0) {
                x = IDEX.getOrDefault("data2", 0);
            } else {
                x = IDEX.getOrDefault("immediate", 0);
            }
            int y = IDEX.getOrDefault("data1", 0);
            EXMEM.put("calculated", ALU.calc(x, y, IDEX.getOrDefault("ALUOp0", 0), IDEX.getOrDefault("ALUOp1", 0), IDEX.getOrDefault("immediate", 0) & 0x111111));
            EXMEM.put("zero", (EXMEM.getOrDefault("calculated", 0) == 0) ? 1 : 0);
        }
        EXMEM.put("data2", EXMEM.getOrDefault("data2", 0));

        if (IDEX.getOrDefault("regDst", 0) == 0) {
            EXMEM.put("dst", IDEX.getOrDefault("rt", 0));
        } else {
            EXMEM.put("dst", IDEX.getOrDefault("rd", 0));
        }

    }

    private void memoryAccess() {
        MEMWB.clear();
        MEMWB.putAll(EXMEM);
        pcSrc = EXMEM.getOrDefault("zero", 0) & EXMEM.getOrDefault("branch", 0);

        int address = EXMEM.getOrDefault("calculated", 0);
        int writeData = EXMEM.getOrDefault("data2", 0);
        int memRead = EXMEM.getOrDefault("memRead", 0);
        int memWrite = EXMEM.getOrDefault("memWrite", 0);
        if (memWrite == 1) {
            ram.storeWord(address, writeData);
        }
        if (memRead == 1) {
            MEMWB.put("data", ram.loadWord(address));
        }
    }

    private void writeBack() {
        if (MEMWB.getOrDefault("regWrite", 0) == 1) {
            if (MEMWB.getOrDefault("memToReg", 0) == 1) {
                registers.set(MEMWB.getOrDefault("dst", 0), MEMWB.getOrDefault("data", 0));
            } else {
                registers.set(MEMWB.getOrDefault("dst", 0), MEMWB.getOrDefault("calculated", 0));
            }
        }
    }

    public Result getResult(int clockNumber) {
        return resultList.get(clockNumber - 1);
    }

    public int getClock() {
        return currentClock;
    }
}

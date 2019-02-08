package osul.parser;

public class LwParser extends ITypeMemoryInstructionParser {

    public LwParser(String line, LinesTable linesTable) {
        super(line, linesTable);
    }

    @Override
    protected int getOpcode() {
        return 0x23;
    }
}

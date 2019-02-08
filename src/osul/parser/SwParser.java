package osul.parser;

public class SwParser extends ITypeMemoryInstructionParser {

    public SwParser(String line, LinesTable linesTable) {
        super(line, linesTable);
    }

    @Override
    protected int getOpcode() {
        return 0x2b;
    }
}

package osul.parser;

public class SltParser extends RTypeInstructionParser {


    public SltParser(String line, LinesTable linesTable) {
        super(line, linesTable);
    }

    @Override
    protected int getFunct() {
        return 0x2a;
    }
}

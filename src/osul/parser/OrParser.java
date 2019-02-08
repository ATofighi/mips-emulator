package osul.parser;

public class OrParser extends RTypeInstructionParser {


    public OrParser(String line, LinesTable linesTable) {
        super(line, linesTable);
    }

    @Override
    protected int getFunct() {
        return 0x25;
    }
}

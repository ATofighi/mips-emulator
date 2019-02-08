package osul.parser;

public class SubParser extends RTypeInstructionParser {


    public SubParser(String line, LinesTable linesTable) {
        super(line, linesTable);
    }

    @Override
    protected int getFunct() {
        return 0x22;
    }
}

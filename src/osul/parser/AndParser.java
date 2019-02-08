package osul.parser;

public class AndParser extends RTypeInstructionParser {


    public AndParser(String line, LinesTable linesTable) {
        super(line, linesTable);
    }

    @Override
    protected int getFunct() {
        return 0x24;
    }
}

package osul.parser;

public class NorParser extends RTypeInstructionParser {


    public NorParser(String line, LinesTable linesTable) {
        super(line, linesTable);
    }

    @Override
    protected int getFunct() {
        return 0x27;
    }
}

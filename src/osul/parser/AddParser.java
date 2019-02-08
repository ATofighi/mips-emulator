package osul.parser;

public class AddParser extends RTypeInstructionParser {


    public AddParser(String line, LinesTable linesTable) {
        super(line, linesTable);
    }

    @Override
    protected int getFunct() {
        return 0x20;
    }
}

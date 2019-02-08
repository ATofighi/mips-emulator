package osul.parser;

public class BeqParser extends ITypeInstructionParser {

    public BeqParser(String line, LinesTable linesTable) {
        super(line, linesTable);
    }

    @Override
    protected int getImmediate() throws ParseException {
        try {
            String res = getParameter(2);
            if (linesTable.containsKey("res")) {
                return linesTable.get("res");
            }
        }
        catch (Exception e) {
            throw new ParseException(e.toString());
        }
        return super.getImmediate();
    }

    @Override
    protected int getOpcode() {
        return 0x4;
    }
}

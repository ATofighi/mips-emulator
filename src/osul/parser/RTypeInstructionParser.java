package osul.parser;

public abstract class RTypeInstructionParser implements InstructionParser {
    private String line;
    private LinesTable linesTable;

    public RTypeInstructionParser(String line, LinesTable linesTable) {
        this.line = line;
        this.linesTable = linesTable;
    }

    protected String getParameter(int parameterNumber) throws ParseException {
        try {
            return line.split(" ", 2)[1].split(",")[parameterNumber].trim();
        } catch (Exception e) {
            throw new ParseException(e.toString());
        }
    }

    protected int getOpcode() {
        return 0;
    }

    protected int getRs() throws ParseException {
        return Parser.getRegisterNumber(getParameter(1));
    }

    protected int getRt() throws ParseException {
        return Parser.getRegisterNumber(getParameter(2));
    }

    protected int getRd() throws ParseException {
        return Parser.getRegisterNumber(getParameter(0));
    }

    protected int getShamt() throws ParseException {
        return 0;
    }

    protected abstract int getFunct();

    @Override
    public int getInstructionCode() throws ParseException {
        return getFunct() | (getShamt() << 6) | (getRd() << 11) | (getRt() << 16) | (getRs() << 21) | (getOpcode() << 26);
    }
}

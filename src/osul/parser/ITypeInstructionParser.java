package osul.parser;

public abstract class ITypeInstructionParser implements InstructionParser {
    protected String line;
    protected LinesTable linesTable;

    public ITypeInstructionParser(String line, LinesTable linesTable) {
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

    protected abstract int getOpcode();

    protected int getRs() throws ParseException {
        return Parser.getRegisterNumber(getParameter(1));
    }

    protected int getRt() throws ParseException {
        return Parser.getRegisterNumber(getParameter(0));
    }


    protected int getImmediate() throws ParseException {
        try {
            int res = Integer.parseInt(getParameter(2));
            if((res & 0b11111111111111110000000000000000) != 0) {
                throw new ParseException("Immediate must be 16 bits: " + res);
            }
            return res;
        } catch (Exception e) {
            throw new ParseException(e.getMessage());
        }
    }

    @Override
    public int getInstructionCode() throws ParseException {
        return getImmediate() | (getRt() << 16) | (getRs() << 21) | (getOpcode() << 26);
    }
}

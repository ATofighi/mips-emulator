package osul.parser;

public abstract class ITypeMemoryInstructionParser extends ITypeInstructionParser {
    public ITypeMemoryInstructionParser(String line, LinesTable linesTable) {
        super(line, linesTable);
    }


    protected int getRs() throws ParseException {
        return Parser.getRegisterNumber(getParameter(1).split("\\(")[1].replaceAll("\\)", ""));
    }


    protected int getImmediate() throws ParseException {
        try {
            int res = Integer.parseInt(getParameter(1).split("\\(")[0]);
            if ((res & 0b11111111111111110000000000000000) != 0) {
                throw new ParseException("Immediate must be 16 bits: " + res);
            }
            return res;
        } catch (Exception e) {
            throw new ParseException(e.getMessage());
        }
    }

}

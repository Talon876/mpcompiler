package compiler.parser.symbol;

public class DataRow extends Row {
    private int memOffset;
    private Mode mode;

    public DataRow(String lexeme, Classification classification, Type type, int memOffset, Mode mode) {
        super(lexeme, classification);
        super.setType(type);
        this.memOffset = memOffset;
        this.mode = mode;
    }

    public int getMemOffset()
    {
        return memOffset;
    }

    public Mode getMode()
    {
        return mode;
    }

    @Override
    public void print() {
        System.out.println(this.getClassification().toString() + ": " + this.getLexeme() + "\tType " + super.getType()
                + "\tClassification: " + this.getClassification() + "\tMode: " + this.getMode() + "\tOffset: " + memOffset);

    }
}

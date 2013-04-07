package compiler.parser.symbol;

public class DataRow extends Row {
    private Type type;
    private int memOffset;

    public DataRow(String lexeme, Classification classification, Type type, int memOffset) {
        super(lexeme, classification);
        this.type = type;
        this.memOffset = memOffset;
    }

    public int getMemOffset()
    {
        return memOffset;
    }

    @Override
    public void print() {
        System.out.println(this.getClassification().toString() + ": " + this.getLexeme() + "\tType " + type
                + "\tClassification: " + this.getClassification() + "\tOffset: " + memOffset);

    }
}

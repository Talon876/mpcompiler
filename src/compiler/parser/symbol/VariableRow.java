package compiler.parser.symbol;

public class VariableRow extends Row {

    private Type type;

    public VariableRow(String lexeme, Classification classification, Type type) {
        super(lexeme, classification);
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public void print() {
        System.out.println("Variable: " + this.getLexeme() + "\tType " + this.type  + "\tClassification: " + this.getClassification());
    }

}

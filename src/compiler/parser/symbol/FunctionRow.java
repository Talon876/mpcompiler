package compiler.parser.symbol;

public class FunctionRow extends Row {

    private Type returnType;

    //TODO attributes

    public FunctionRow(String lexeme, Classification classification, Type returnType) {
        super(lexeme, classification);
        this.returnType = returnType;
    }

    public Type getReturnType() {
        return returnType;
    }

    public void setReturnType(Type returnType) {
        this.returnType = returnType;
    }

}

package compiler.parser.symbol;

public class FunctionRow extends Row {

    private Type returnType;

    private Attribute[] attributes;

    public FunctionRow(String lexeme, Classification classification, Type returnType, Attribute[] attributes) {
        super(lexeme, classification);
        this.returnType = returnType;
        this.attributes = attributes;
    }

    public Type getReturnType() {
        return returnType;
    }

    public void setReturnType(Type returnType) {
        this.returnType = returnType;
    }
    
    public Attribute[] getAttributes()
    {
        return attributes;
    }

    @Override
    public void print() {
        // TODO Auto-generated method stub
        
    }
}

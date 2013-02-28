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
        System.out.print("Function: " + this.getLexeme() + " attributes: ");
        for(Attribute a: attributes)
        {
            a.print();
        }
        System.out.println(" return type: " + this.getReturnType());
    }
}

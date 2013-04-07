package compiler.parser.symbol;

import java.util.ArrayList;
import java.util.List;

public class FunctionRow extends Row implements ModuleRow {

    private List<Attribute> attributes;

    public FunctionRow(String lexeme, Classification classification, Type returnType, Attribute[] attributes) {
        super(lexeme, classification);
        setType(returnType);
        this.attributes = new ArrayList<Attribute>();
        for (Attribute a : attributes) {
            this.attributes.add(a);
        }
    }

    public FunctionRow(String lexeme, Classification classification, Type returnType) {
        super(lexeme, classification);
        setType(returnType);
        attributes = new ArrayList<Attribute>();
    }

    public void addAttribute(Attribute attribute) {
        attributes.add(attribute);
    }

    public Type getReturnType() {
        return getType();
    }

    public void setReturnType(Type returnType) {
        setType(returnType);
    }

    public List<Attribute> getAttributes()
    {
        return attributes;
    }

    @Override
    public void print() {
        System.out.print("Function: " + getLexeme() + "\tattributes: ");
        for (Attribute a : attributes)
        {
            a.print();
        }
        System.out.println("\treturn type: " + getReturnType());
    }
}

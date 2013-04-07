package compiler.parser.symbol;

import java.util.ArrayList;
import java.util.List;

public class ProcedureRow extends Row implements ModuleRow {

    private List<Attribute> attributes;

    public ProcedureRow(String lexeme, Classification classification, Attribute[] attributes) {
        super(lexeme, classification);
        this.attributes = new ArrayList<Attribute>();
        setType(null);
        for (Attribute a : attributes) {
            this.attributes.add(a);
        }
    }

    public ProcedureRow(String lexeme, Classification classification) {
        super(lexeme, classification);
        setType(null);
        attributes = new ArrayList<Attribute>();
    }

    public void addAttribute(Attribute attribute) {
        attributes.add(attribute);
    }

    public List<Attribute> getAttributes()
    {
        return attributes;
    }

    public Type getType()
    {
        return null;
    }

    public void setType(Type t)
    {
        super.setType(null);
    }

    @Override
    public void print() {
        System.out.print("Procedure: " + getLexeme() + "\tattributes: ");
        for (Attribute a : attributes)
        {
            a.print();
        }
        System.out.println();

    }
}

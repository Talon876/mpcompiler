package compiler.parser.symbol;

import java.util.ArrayList;
import java.util.List;

public class ProcedureRow extends Row implements ModuleRow {

    private List<Attribute> attributes;

    public ProcedureRow(String lexeme, Classification classification, List<Attribute> attributes) {
        super(lexeme, classification);
        this.attributes = new ArrayList<Attribute>();
        setType(null);
        this.attributes.addAll(attributes);
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
        System.out.print("Procedure: " + getLexeme() + "\tformal attributes: ");
        boolean first = true;
        for (Attribute a : attributes)
        {
            if(!first)
            {
                System.out.print(", "); 
            }
            else
            {
                first = false;
            }
            a.print();
        }
        System.out.println();

    }
}

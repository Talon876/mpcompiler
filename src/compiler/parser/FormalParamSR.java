package compiler.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import compiler.parser.symbol.Attribute;

public class FormalParamSR {
    private List<Attribute> attributes;
    private AtomicInteger pointer;
    private String funcName;

    public FormalParamSR(String name, List<Attribute> attributes)
    {
        pointer = new AtomicInteger(0);
        this.attributes = new ArrayList<Attribute>();
        this.attributes.addAll(attributes);
        funcName = name;
    }

    public Attribute getCurrentAttribute() {
        if (pointer.get() < attributes.size())
        {
            return attributes.get(pointer.get());
        }
        else
        {
            return null;
        }
    }

    public Attribute getCurrentAttributeAndIncrement() {
        if (pointer.get() < attributes.size())
        {
            return attributes.get(pointer.getAndIncrement());
        }
        else
        {
            return null;
        }
    }

    /**
     * Checks if after incrementing all the actual parameters it is exactly equal to the formal parameter list size
     * 
     * @return true if the pointer is the same as the size
     */
    public boolean isCorrectParamCount()
    {
        return pointer.get() == attributes.size();
    }

    public String getName()
    {
        return funcName;
    }
}

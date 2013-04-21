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
        if(pointer.get() < attributes.size())
        {
            return attributes.get(pointer.get());
        }
        else
        {
            return null;
        }
    }
    
    public Attribute getCurrentAttributeAndIncrement() {
        if(pointer.get() < attributes.size())
        {
            return attributes.get(pointer.getAndIncrement());
        }
        else
        {
            return null;
        }
    }
    
    public String getName()
    {
        return funcName;
    }
}

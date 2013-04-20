package compiler.parser.symbol;

import java.util.List;

public interface ModuleRow {
    public void addAttribute(Attribute attribute);
    public List<Attribute> getAttributes();
    public String getBranchLabel();
    
}

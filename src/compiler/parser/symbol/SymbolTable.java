package compiler.parser.symbol;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class SymbolTable implements Printable {

    private static AtomicInteger NEXT_NESTING_LEVEL = new AtomicInteger();
    private String scopeName;
    private ArrayList<Row> rows;
    private int nestingLevel;
    private AtomicInteger memSize;
    private String branchLbl;
    private int variableCount;
    private int parameterCount;
    
    

    public SymbolTable(String scopeName, String branch) {
        this.scopeName = scopeName;
        rows = new ArrayList<Row>();
        memSize = new AtomicInteger();
        nestingLevel = getAndIncrementNestingLevel();
        branchLbl = branch;
        variableCount = 0;
        parameterCount = 0;
    }

    public static int getAndIncrementNestingLevel()
    {
        return NEXT_NESTING_LEVEL.getAndIncrement();
    }

    public static int decrementNestingLevel()
    {
        return NEXT_NESTING_LEVEL.decrementAndGet();
    }

    public int getTableSize()
    {
        return memSize.get();
    }

    private int getAndIncrementTableSize()
    {
        return memSize.getAndIncrement();
    }

    public String getScopeName() {
        return scopeName;
    }

    public void setScopeName(String scopeName) {
        this.scopeName = scopeName;
    }

    public int getNestingLevel()
    {
        return nestingLevel;
    }

    public void insertRow(Row row) {
        for (Row r : rows) {
            if (r.getLexeme().equalsIgnoreCase(row.getLexeme())) {
                System.out.println("Identifier '" + row.getLexeme() + "' has already been declared");
                System.exit(1);
            }
        }
        rows.add(row);
    }

    public Row findSymbol(String lexeme) {
        for (Row r : rows) {
            if (r.getLexeme().equalsIgnoreCase(lexeme)) {
                return r;
            }
        }
        return null;
    }

    public Row findSymbol(String lexeme, Classification c) {
        for (Row r : rows) {
            if (r.getLexeme().equalsIgnoreCase(lexeme) && r.getClassification() == c) {
                return r;
            }
        }
        return null;
    }

    public boolean contains(Row symbol)
    {
        for (Row r : rows) {
            if (r.equals(symbol)) {
                return true;
            }
        }
        return false;
    }

    public String getBranchLbl() {
        return branchLbl;
    }

    @Override
    public void print()
    {
        System.out.println("SymbolTable Name: " + scopeName + "\t Nesting Level: " + nestingLevel + "\tBranch Label: "
                + getBranchLbl() + "\t Size: " + getTableSize());
        for (Row r : rows)
        {
            System.out.print("\tRow ");
            r.print();
        }
    }

    public void addDataSymbolsToTable(Classification c, List<String> ids, List<Attribute> attributes) {
        for (int i = 0; i < ids.size(); i++) {
            String lex = ids.get(i);
            Attribute attribute = attributes.get(i);
            DataRow p = null;
            switch (c) {
            case VARIABLE:
                p = new DataRow(lex, c, attribute.getType(), getAndIncrementTableSize(), attribute.getMode());
                insertRow(p);
                variableCount++;
                break;
            case PARAMETER:
                p = new DataRow(lex, c, attribute.getType(), getAndIncrementTableSize(), attribute.getMode());
                insertRow(p);
                parameterCount++;
                break;
            case DISREG:
            case RETADDR:
                p = new DataRow(lex, c, attribute.getType(), getAndIncrementTableSize(), attribute.getMode());
                insertRow(p);
                break;
            default:
                break;
            }
        }
    }

    public void addDataSymbolsToTable(Classification c, List<String> ids, Attribute attribute)
    {
        List<Attribute> attributes = new ArrayList<Attribute>(ids.size());
        for (int i = 0; i < ids.size(); i++)
        {
            attributes.add(attribute);
        }
        addDataSymbolsToTable(c, ids, attributes);
    }

    public void addDataSymbolToTable(Classification c, String id, Attribute attribute)
    {
        List<String> ids = new ArrayList<String>();
        ids.add(id);
        List<Attribute> attributes = new ArrayList<Attribute>();
        attributes.add(attribute);
        addDataSymbolsToTable(c, ids, attributes);
    }
    
    public void addModuleSymbolsToTable(Classification c, String lexeme, Type returnType, List<Attribute> attributes, String branchLabel) {
        switch (c) {
        case FUNCTION:
            FunctionRow f = new FunctionRow(lexeme, c, returnType, attributes, branchLabel);
            insertRow(f);
            break;
        case PROCEDURE:
            ProcedureRow proc = new ProcedureRow(lexeme, c, attributes, branchLabel);
            insertRow(proc);
            break;
        default:
            break;
        }
    }

    public int getVariableCount() {
        return variableCount;
    }
    
    public int getParameterCount() {
        return parameterCount;
    }
}

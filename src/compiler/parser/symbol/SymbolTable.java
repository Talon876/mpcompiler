package compiler.parser.symbol;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import compiler.parser.Parser;

public class SymbolTable implements Printable {

    private static AtomicInteger NEXT_NESTING_LEVEL = new AtomicInteger();
    private String scopeName;
    private ArrayList<Row> rows;
    private int nestingLevel;
    private AtomicInteger memSize;

    public SymbolTable(String scopeName) {
        this.scopeName = scopeName;
        rows = new ArrayList<Row>();
        memSize = new AtomicInteger();
        nestingLevel = getAndIncrementNestingLevel();
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

    @Override
    public void print()
    {
        System.out.println("SymbolTable Name: " + scopeName + "\t Nesting Level: " + nestingLevel + "\t Size: "
                + getTableSize());
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
            switch (c) {
            case VARIABLE:
                DataRow v = new DataRow(lex, c, attribute.getType(), getAndIncrementTableSize(), attribute.getMode());
                insertRow(v);
                break;
            case PARAMETER:
                DataRow p = new DataRow(lex, c, attribute.getType(), getAndIncrementTableSize(), attribute.getMode());
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

    public void addModuleSymbolsToTable(Classification c, String lexeme, Type returnType, List<Attribute> attributes) {
        switch (c) {
        case FUNCTION:
            FunctionRow f = new FunctionRow(lexeme, c, returnType, attributes);
            insertRow(f);
            break;
        case PROCEDURE:
            ProcedureRow proc = new ProcedureRow(lexeme, c, attributes);
            insertRow(proc);
            break;
        default:
            break;
        }
    }

}

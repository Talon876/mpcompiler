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
    public ModuleRow mostRecentFunctionProcedure = null;

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

    public void addSymbolsToTable(Classification c, List<String> ids, Type t) {
        for (String lex : ids) {
            switch (c) {
            case VARIABLE:
                DataRow v = new DataRow(lex, c, t, getAndIncrementTableSize());
                insertRow(v);

                break;
            case PARAMETER:
                DataRow p = new DataRow(lex, c, t, getAndIncrementTableSize());
                insertRow(p);
                /*if (mostRecentFunctionProcedure != null) { //shouldn't have parameters without first hitting a function/procedure anyway
                    mostRecentFunctionProcedure.addAttribute(new Attribute(t, Mode.PARAMETER));
                }
                else
                {
                    Parser.semanticError("Parameter without Function/Procedure first");
                }*/
                break;
            case FUNCTION:
                FunctionRow f = new FunctionRow(lex, c, t);
                insertRow(f);
                mostRecentFunctionProcedure = f;
                break;
            case PROCEDURE:
                ProcedureRow proc = new ProcedureRow(lex, c);
                insertRow(proc);
                mostRecentFunctionProcedure = proc;
                break;
            }
        }
    }

    public void addSymbolsToTable(Classification c, String lexeme, Type t) {
        List<String> ids = new ArrayList<String>();
        ids.add(lexeme);
        addSymbolsToTable(c, ids, t);
    }

    public static void main(String[] args)
    {
        SymbolTable table = new SymbolTable("Maintest");
        table.rows.add(new DataRow("varStuff", Classification.VARIABLE, Type.INTEGER, 0));
        table.rows.add(new DataRow("paramStuff", Classification.PARAMETER, Type.FLOAT, 1));
        table.rows.add(new FunctionRow("funcStuff", Classification.FUNCTION, Type.BOOLEAN, new Attribute[] {
                new Attribute(Type.BOOLEAN, Mode.VARIABLE), new Attribute(Type.FLOAT, Mode.PARAMETER) }));
        table.rows.add(new ProcedureRow("procStuff", Classification.PROCEDURE, new Attribute[] {
                new Attribute(Type.INTEGER, Mode.VARIABLE), new Attribute(Type.FLOAT, Mode.VARIABLE) }));

        table.print();
    }

}

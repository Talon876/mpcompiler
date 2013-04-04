package compiler.parser.symbol;

import java.util.ArrayList;

public class SymbolTable implements Printable {

    private String scopeName;
    private ArrayList<Row> rows;

    public SymbolTable(String scopeName) {
        this.scopeName = scopeName;
        rows = new ArrayList<Row>();
    }

    public String getScopeName() {
        return scopeName;
    }

    public void setScopeName(String scopeName) {
        this.scopeName = scopeName;
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

    @Override
    public void print()
    {
        System.out.println("SymbolTable Name: " + scopeName);
        for (Row r : rows)
        {
            System.out.print("\tRow ");
            r.print();
        }
    }

    public static void main(String[] args)
    {
        SymbolTable table = new SymbolTable("Maintest");
        table.rows.add(new VariableRow("varStuff", Classification.VARIABLE, Type.INTEGER));
        table.rows.add(new ParameterRow("paramStuff", Classification.PARAMETER, Type.FLOAT));
        table.rows.add(new FunctionRow("funcStuff", Classification.FUNCTION, Type.BOOLEAN, new Attribute[] {
                new Attribute(Type.BOOLEAN, Mode.VARIABLE), new Attribute(Type.FLOAT, Mode.PARAMETER) }));
        table.rows.add(new ProcedureRow("procStuff", Classification.FUNCTION, new Attribute[] {
                new Attribute(Type.INTEGER, Mode.VARIABLE), new Attribute(Type.FLOAT, Mode.VARIABLE) }));

        table.print();
    }
}

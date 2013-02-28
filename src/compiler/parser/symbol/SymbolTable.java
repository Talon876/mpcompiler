package compiler.parser.symbol;

import java.util.ArrayList;

public class SymbolTable {

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
        rows.add(row);
    }
}

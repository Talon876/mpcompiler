package compiler.parser.symbol;

public abstract class Row {
    private Classification classification;
    private String lexeme;

    public Row(String lexeme, Classification classification) {
        this.lexeme = lexeme;
        this.classification = classification;
    }

    public Classification getClassification() {
        return classification;
    }

    public void setClassification(Classification classification) {
        this.classification = classification;
    }

    public String getLexeme() {
        return lexeme;
    }

    public void setLexeme(String lexeme) {
        this.lexeme = lexeme;
    }

}

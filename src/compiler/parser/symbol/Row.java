package compiler.parser.symbol;

public abstract class Row implements Printable {
    private Classification classification;
    private String lexeme;
    private Type type;

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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}

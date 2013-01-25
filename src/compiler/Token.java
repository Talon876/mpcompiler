package compiler;

public class Token {
    private TokenType type;
    private String lexeme;
    private int columnNumber;
    private int lineNumber;

    public Token(TokenType tpe, String lex, int line, int col) {
        type = tpe;
        lexeme = lex;
        lineNumber = line;
        columnNumber = col;
    }

    /**
     * @return the enum representing the micropascal token
     */
    public TokenType getType() {
        return type;
    }

    /**
     * @return the lexeme value the token represents (i.e "and" for token
     *         "MP_AND")
     */
    public String getLexeme() {
        return lexeme;
    }

    /**
     * @return line number where the token was found
     */
    public int getLineNumber() {
        return lineNumber;
    }

    /**
     * @return column number where the token started
     */
    public int getColumnNumber() {
        return columnNumber;
    }

    @Override
    public String toString() {
        return type.toString() + "\t" + lineNumber + "\t" + columnNumber + "\t"
                + lexeme;
    }
}

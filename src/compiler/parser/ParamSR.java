package compiler.parser;

import compiler.parser.symbol.Attribute;

public class ParamSR {
    private String lexeme;
    private Attribute attribute;

    public ParamSR(String lexeme, Attribute attribute)
    {
        this.lexeme = lexeme;
        this.attribute = attribute;
    }

    public String getLexeme() {
        return lexeme;
    }

    public Attribute getAttribute() {
        return attribute;
    }

}

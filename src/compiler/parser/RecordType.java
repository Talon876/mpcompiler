package compiler.parser;

public enum RecordType {
    /**
     * Data is the classification as String (from the compiler.parser.symbol.Classification enum), lexeme (Id Type retrieved from
     * symboltable)
     */
    IDENTIFIER,
    /**
     * Data is (Type.Boolean, Type.String, Type.Integer, Type.Float) value on stack 
     */
    LITERAL,
    /**
     * Data is (MP_NEQUAL, MP_GEQUAL, MP_LEQUAL, MP_GTHAN, MP_LTHAN, MP_EQUAL)
     */
    REL_OP,
    /**
     * Data is (MP_MINUS, MP_PLUS)
     */
    OPT_SIGN,
    /**
     * Data is (MP_PLUS, MP_MINUS, MP_OR
     */
    ADD_OP,
    /**
     * Data is (MP_AND, MP_MOD, MP_DIV, MP_TIMES)
     */
    MUL_OP,
    /**
     * Data is Current Symbol Table Name, Current Symbol Table Level, Current Table Size
     */
    SYM_TBL
}

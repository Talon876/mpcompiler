package compiler;

/**
 * List of tokens in micropascal
 */
public enum TokenType {
    // Enum from
    // http://www.cs.montana.edu/ross/classes/spring2008/cs450/pages/resources/tokens.html
    // using (MP_[A-Z]+)\W*["].+["]

    // Reserved Words
    MP_AND, MP_BEGIN, MP_DIV, MP_DO, MP_DOWNTO, MP_ELSE, MP_END, MP_FIXED, MP_FLOAT, MP_FOR, MP_FUNCTION, MP_IF, MP_INTEGER, MP_MOD, MP_NOT, MP_OR, MP_PROCEDURE, MP_PROGRAM, MP_READ, MP_REPEAT, MP_THEN, MP_TO, MP_UNTIL, MP_VAR, MP_WHILE, MP_WRITE,

    // Identifiers and Literals
    MP_IDENTIFIER, MP_INTEGER_LIT, MP_FIXED_LIT, MP_FLOAT_LIT, MP_STRING_LIT,

    // Symbols
    MP_PERIOD, MP_COMMA, MP_SCOLON, MP_LPAREN, MP_RPAREN, MP_EQUAL, MP_GTHAN, MP_GEQUAL, MP_LTHAN, MP_LEQUAL, MP_NEQUAL, MP_ASSIGN, MP_PLUS, MP_MINUS, MP_TIMES, MP_COLON,

    // End of File
    MP_EOF,

    // Errors
    MP_RUN_COMMENT,
    MP_RUN_STRING,
    MP_ERROR,

    //Whitespace
    MP_WHITESPACE,

    //Comment
    MP_COMMENT
    // A comment in MP starts with { and ends with }
    // I am not sure if they are tokens or not?
}

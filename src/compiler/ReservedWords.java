package compiler;

import java.util.HashMap;

public class ReservedWords {

    private ReservedWords() { //uninstantiable utility class
    }

    private static HashMap<String, TokenType> tokenMap = new HashMap<String, TokenType>();

    static {
        //add all reserved words in all lowercase

        tokenMap.put("and", TokenType.MP_AND);
        tokenMap.put("begin", TokenType.MP_BEGIN);
        tokenMap.put("div", TokenType.MP_DIV_INT);
        tokenMap.put("do", TokenType.MP_DO);
        tokenMap.put("downto", TokenType.MP_DOWNTO);
        tokenMap.put("else", TokenType.MP_ELSE);
        tokenMap.put("end", TokenType.MP_END);
        tokenMap.put("fixed", TokenType.MP_FIXED);
        tokenMap.put("float", TokenType.MP_FLOAT);
        tokenMap.put("for", TokenType.MP_FOR);
        tokenMap.put("function", TokenType.MP_FUNCTION);
        tokenMap.put("if", TokenType.MP_IF);
        tokenMap.put("integer", TokenType.MP_INTEGER);
        tokenMap.put("mod", TokenType.MP_MOD);
        tokenMap.put("not", TokenType.MP_NOT);
        tokenMap.put("or", TokenType.MP_OR);
        tokenMap.put("procedure", TokenType.MP_PROCEDURE);
        tokenMap.put("program", TokenType.MP_PROGRAM);
        tokenMap.put("read", TokenType.MP_READ);
        tokenMap.put("repeat", TokenType.MP_REPEAT);
        tokenMap.put("then", TokenType.MP_THEN);
        tokenMap.put("to", TokenType.MP_TO);
        tokenMap.put("until", TokenType.MP_UNTIL);
        tokenMap.put("var", TokenType.MP_VAR);
        tokenMap.put("while", TokenType.MP_WHILE);
        tokenMap.put("write", TokenType.MP_WRITE);
        tokenMap.put("writeln", TokenType.MP_WRITELN);
        tokenMap.put("true", TokenType.MP_TRUE);
        tokenMap.put("false", TokenType.MP_FALSE);
        tokenMap.put("boolean", TokenType.MP_BOOLEAN);
        tokenMap.put("string", TokenType.MP_STRING);
    }

    /**
     * Checks if a string is a reserved word. Case-insensitive.
     * 
     * @param lexeme
     *            the word to check
     * @return true if the word is a reserved word, false otherwise
     */
    public static boolean isReserved(String lexeme) {
        return tokenMap.keySet().contains(lexeme.toLowerCase());
    }

    /**
     * Converts a reserved word to the correct TokenType
     * 
     * @param reservedWord
     *            the reserved word to convert
     * @return the TokenType of the reserved word
     * @throws NullPointerException
     *             if reservedWord isn't a reservedWord
     */
    public static TokenType convertToToken(String reservedWord) {
        return tokenMap.get(reservedWord.toLowerCase());
    }
}

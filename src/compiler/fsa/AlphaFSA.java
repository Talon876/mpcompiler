package compiler.fsa;

import compiler.Letters;
import compiler.ReservedWords;
import compiler.Token;
import compiler.TokenType;
import compiler.io.MPFile;

/**
 * Responsible for finding the following tokens: MP_IDENTIFIER, MP_AND, MP_BEGIN, MP_DIV, MP_DO, MP_DOWNTO, MP_ELSE, MP_END, MP_FIXED,
 * MP_FLOAT, MP_FOR, MP_FUNCTION, MP_IF, MP_INTEGER, MP_MOD, MP_NOT, MP_OR, MP_PROCEDURE, MP_PROGRAM, MP_READ, MP_REPEAT, MP_THEN, MP_TO,
 * MP_UNTIL, MP_VAR, MP_WHILE, MP_WRITE
 * 
 */
public class AlphaFSA implements FSA {

    //(letter | "_"(letter | digit)){["_"](letter | digit)}
    //before returning the identifier, check if it's a reserved word using ReservedWords class and if it is, update the token

    @Override
    public Token getToken(MPFile file) {
        Token t = null;
        int state = 1;
        String lexeme = "";
        char current = 0;

        while (state != 4) {
            if (!file.hasNextChar() && state == 2) {
                state = 4;
            } else {

                switch (state) {
                case 1:
                    current = file.getNextCharacter();
                    if (Letters.isLetter(current)) {
                        state = 2;
                        lexeme += current;
                    } else if (current == '_') {
                        state = 3;
                        lexeme += current;
                    } else {
                        return new Token(TokenType.MP_ERROR, "" + current, file.getLineIndex(),
                                file.getColumnIndex() - 1);
                    }
                    break;
                case 2:
                    current = file.getCurrentCharacter();
                    if (Letters.isLetterOrDigit(current)) {
                        current = file.getNextCharacter();
                        state = 2;
                        lexeme += current;
                    } else if (current == '_') {
                        current = file.getNextCharacter();
                        state = 3;
                        lexeme += current;
                    } else {
                        state = 4;
                    }
                    break;
                case 3:
                    current = file.getNextCharacter();
                    if (Letters.isLetterOrDigit(current)) {
                        state = 2;
                        lexeme += current;
                    } else {
                        return new Token(TokenType.MP_ERROR, "" + current, file.getLineIndex(),
                                file.getColumnIndex() - 1);
                    }
                    break;
                }
            }

        }
        if (ReservedWords.isReserved(lexeme)) {
            t = new Token(ReservedWords.convertToToken(lexeme), lexeme, file.getLineIndex(), file.getColumnIndex()
                    - lexeme.length());
        } else {
            t = new Token(TokenType.MP_IDENTIFIER, lexeme, file.getLineIndex(), file.getColumnIndex() - lexeme.length());
        }

        return t;
    }

}

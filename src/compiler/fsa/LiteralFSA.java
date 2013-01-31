package compiler.fsa;

import compiler.Token;
import compiler.TokenType;
import compiler.io.MPFile;

/**
 * Responsible for finding MP_STRING_LIT
 */
public class LiteralFSA implements FSA {

    //"'" {"''" | AnyCharacterExceptApostropheOrEOL} "'"

    //EOL: currentColIdx = current line length
    @Override
    public Token getToken(MPFile file) {
        Token t = null;
        int state = 1;
        String lexeme = "";
        char current = 0;
        int startingLine = file.getLineIndex();
        int startingCol = file.getCurrentLine().getColumnIndex();

        while (state != 4) {
            if (!file.hasNextChar() && state == 3) {
                state = 4;
            } else {

                switch (state) {
                case 1:
                    current = file.getNextCharacter();
                    if (current == '\'') {
                        state = 2;
                    }
                    break;
                case 2:
                    if (file.hasNextChar()) {

                        current = file.getNextCharacter();

                        if (current != '\'' && file.hasNextChar()) {
                            state = 2;
                            lexeme += current;
                        } else if (current == '\'') {
                            state = 3;
                        }
                    } else { //no more characters on line
                        return new Token(TokenType.MP_RUN_STRING, lexeme, startingLine, startingCol);
                    }
                    break;
                case 3:
                    if (file.hasNextChar()) {

                        if (file.getCurrentCharacter() == '\'') {
                            current = file.getNextCharacter();
                            state = 2;
                            lexeme += current;
                        } else {
                            state = 4;
                        }
                    } else { //no more characters on line
                        return new Token(TokenType.MP_RUN_STRING, lexeme, startingLine, startingCol);
                    }

                    break;
                }
            }

        }
        t = new Token(TokenType.MP_STRING_LIT, lexeme, startingLine, startingCol);

        return t;
    }

}

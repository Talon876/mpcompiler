package compiler.fsa;

import compiler.Token;
import compiler.TokenType;
import compiler.WhiteSpace;
import compiler.io.MPFile;

public class WhitespaceFSA implements FSA {

    @Override
    public Token getToken(MPFile file) {
        Token t = null;
        int state = 1;
        String lexeme = "";
        char current = 0;

        while (state != 3) {

            if (!file.hasNextChar() && state == 2) {
                state = 3;
            } else {

                switch (state) {
                case 1:
                    current = file.getNextCharacter();
                    if (WhiteSpace.isWhitespace(current)) {
                        state = 2;
                        lexeme += current;
                    }
                    break;
                case 2:
                    current = file.getCurrentCharacter();
                    if (WhiteSpace.isWhitespace(current)) {
                        current = file.getNextCharacter();
                        state = 2;
                        lexeme += current;
                    } else {
                        state = 3;
                    }
                    break;

                }
            }

        }
        t = new Token(TokenType.MP_WHITESPACE, lexeme, file.getLineIndex(), file.getColumnIndex() - lexeme.length());

        return t;
    }

}

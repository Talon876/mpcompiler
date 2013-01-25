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
                        //System.out.println("going from state 1 to state 2");
                        state = 2;
                        lexeme += current;
                    }
                    break;
                case 2:
                    current = file.getCurrentCharacter();
                    if (WhiteSpace.isWhitespace(current)) {
                        current = file.getNextCharacter();
                        //System.out.println("going from state 2 to state 2");
                        state = 2;
                        lexeme += current;
                    } else {
                        //System.out.println("going from state 2 to state 3");
                        state = 3;
                    }
                    //TODO EOL error state thing
                    break;

                }
            }

        }
        t = new Token(TokenType.MP_WHITESPACE, lexeme, file.getLineIndex(), file.getColumnIndex() - lexeme.length());
        //System.out.println("leaving thing at " + file.getColumnIndex());
        System.out.println("found token: " + t.toString());

        return t;
    }

}

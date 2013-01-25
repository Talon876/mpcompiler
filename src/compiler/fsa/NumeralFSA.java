package compiler.fsa;

import compiler.Letters;
import compiler.Token;
import compiler.TokenType;
import compiler.io.MPFile;

/**
 * Responsible for finding the following tokens: MP_INTEGER_LIT, MP_FIXED_LIT, MP_FLOAT_LIT
 */
public class NumeralFSA implements FSA {

    @Override
    public Token getToken(MPFile file) {
        Token t = null;
        int state = 1;
        String lexeme = "";
        char current = 0;
        int acceptedAt = 0;

        while (state != 8) {
            if (!file.hasNextChar() && (state == 2 || state == 5 || state == 7)) {
                acceptedAt = state;
                state = 8;
            } else {

                switch (state) {
                case 1:
                    current = file.getNextCharacter();
                    if (Letters.isDigit(current)) {
                        state = 2;
                        lexeme += current;
                    }
                    break;
                case 2:
                    current = file.getCurrentCharacter();
                    if (Letters.isDigit(current)) {
                        current = file.getNextCharacter();
                        state = 2;
                        lexeme += current;
                    } else if (current == '.') {
                        current = file.getNextCharacter();
                        state = 4;
                        lexeme += current;
                    } else if (current == 'e' || current == 'E') {
                        current = file.getNextCharacter();
                        state = 3;
                        lexeme += current;
                    } else {
                        acceptedAt = 2;
                        state = 8;
                    }
                    break;
                case 3:
                    current = file.getNextCharacter();
                    if (current == '+' || current == '-') {
                        state = 6;
                        lexeme += current;
                    } else if (Letters.isDigit(current)) {
                        state = 5;
                        lexeme += current;
                    }
                    break;
                case 4:
                    current = file.getNextCharacter();
                    if (Letters.isDigit(current)) {
                        state = 7;
                        lexeme += current;
                    }
                    break;
                case 5:
                    current = file.getCurrentCharacter();
                    if (Letters.isDigit(current)) {
                        current = file.getNextCharacter();
                        lexeme += current;
                        state = 5;
                    } else {
                        acceptedAt = 5;
                        state = 8;
                    }
                    break;
                case 6:
                    current = file.getNextCharacter();
                    if (Letters.isDigit(current)) {
                        state = 5;
                        lexeme += current;
                    }
                    break;
                case 7:
                    current = file.getCurrentCharacter();
                    if (Letters.isDigit(current)) {
                        current = file.getNextCharacter();
                        state = 7;
                        lexeme += current;
                    } else if (current == 'e' || current == 'E') {
                        current = file.getNextCharacter();
                        state = 3;
                        lexeme += current;
                    }
                    else {

                        acceptedAt = 7;
                        state = 8;
                    }
                    break;
                }
            }

        }

        TokenType type = null;
        switch (acceptedAt) {
        case 2:
            type = TokenType.MP_INTEGER_LIT;
            break;
        case 7:
            type = TokenType.MP_FIXED_LIT;
            break;
        case 5:
            type = TokenType.MP_FLOAT_LIT;
            break;
        }

        System.out.println("type: " + type);
        System.out.println("lexeme: " + lexeme);
        System.out.println("line: " + file.getLineIndex());
        System.out.println("col: " + (file.getColumnIndex() - lexeme.length()));
        t = new Token(type, lexeme, file.getLineIndex(), file.getColumnIndex() - lexeme.length());

        // System.out.println("leaving thing at " + file.getColumnIndex());
        System.out.println("found token: " + t.toString());

        return t;
    }

}

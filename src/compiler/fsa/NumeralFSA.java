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
        boolean hasError = false;

        while (state != 8) {

            switch (state) {
            case 1:
                current = file.getNextCharacter();
                if (Letters.isDigit(current)) {
                    state = 2;
                    lexeme += current;

                    t = new Token(TokenType.MP_INTEGER_LIT, lexeme, file.getLineIndex(), file.getColumnIndex()
                            - lexeme.length());
                } else {
                    return new Token(TokenType.MP_ERROR, "" + current, file.getLineIndex(),
                            file.getColumnIndex() - 1);
                }
                break;
            case 2: //accept
                if (file.hasNextChar()) {
                    current = file.getCurrentCharacter();

                    if (Letters.isDigit(current)) {
                        current = file.getNextCharacter();
                        state = 2;
                        lexeme += current;

                        t = new Token(TokenType.MP_INTEGER_LIT, lexeme, file.getLineIndex(), file.getColumnIndex()
                                - lexeme.length());
                    } else if (current == '.') {
                        t = new Token(TokenType.MP_INTEGER_LIT, lexeme, file.getLineIndex(), file.getColumnIndex()
                                - lexeme.length());

                        current = file.getNextCharacter();
                        state = 4;
                        lexeme += current;

                    } else if (current == 'e' || current == 'E') {

                        t = new Token(TokenType.MP_INTEGER_LIT, lexeme, file.getLineIndex(), file.getColumnIndex()
                                - lexeme.length());

                        current = file.getNextCharacter();
                        state = 3;
                        lexeme += current;
                    } else {
                        acceptedAt = 2;
                        state = 8;
                        t = new Token(TokenType.MP_INTEGER_LIT, lexeme, file.getLineIndex(), file.getColumnIndex()
                                - lexeme.length());
                    }
                }
                else {
                    acceptedAt = 2;
                    state = 8;
                }

                break;
            case 3:
                if (file.hasNextChar()) {
                    current = file.getNextCharacter();
                    if (current == '+' || current == '-') {
                        state = 6;
                        lexeme += current;
                    } else if (Letters.isDigit(current)) {
                        state = 5;
                        lexeme += current;
                    } else {
                        file.setColumnIndex(t.getColumnNumber() + lexeme.length() - 1);
                        return t;
                    }
                } else {
                    file.setColumnIndex(t.getColumnNumber() + lexeme.length() - 1);
                    return t;
                }
                break;
            case 4:
                if (file.hasNextChar()) {
                    current = file.getNextCharacter();
                    if (Letters.isDigit(current)) {
                        state = 7;
                        lexeme += current;
                    } else {
                        file.setColumnIndex(t.getColumnNumber() + lexeme.length() - 1);
                        return t;
                    }
                } else {
                    file.setColumnIndex(t.getColumnNumber() + lexeme.length() - 1);
                    return t;
                }
                break;
            case 5: //accept
                if (file.hasNextChar()) {
                    current = file.getCurrentCharacter();

                    if (Letters.isDigit(current)) {
                        current = file.getNextCharacter();
                        lexeme += current;
                        state = 5;
                    } else {
                        acceptedAt = 5;
                        state = 8;
                    }

                    t = new Token(TokenType.MP_FLOAT_LIT, lexeme, file.getLineIndex(), file.getColumnIndex()
                            - lexeme.length());
                } else {
                    acceptedAt = 5;
                    state = 8;
                }
                break;
            case 6:
                if (file.hasNextChar()) {

                    current = file.getNextCharacter();
                    if (Letters.isDigit(current)) {
                        state = 5;
                        lexeme += current;
                    } else {
                        file.setColumnIndex(t.getColumnNumber() + lexeme.length() - 1);
                        return t;
                    }
                } else {
                    file.setColumnIndex(t.getColumnNumber() + lexeme.length() - 1);
                    return t;
                }
                break;
            case 7: //accept
                if (file.hasNextChar()) {
                    current = file.getCurrentCharacter();
                    if (Letters.isDigit(current)) {
                        current = file.getNextCharacter();
                        state = 7;
                        lexeme += current;

                        t = new Token(TokenType.MP_FIXED_LIT, lexeme, file.getLineIndex(), file.getColumnIndex()
                                - lexeme.length());
                    } else if (current == 'e' || current == 'E') {
                        t = new Token(TokenType.MP_FIXED_LIT, lexeme, file.getLineIndex(), file.getColumnIndex()
                                - lexeme.length());

                        current = file.getNextCharacter();
                        state = 3;
                        lexeme += current;
                    }
                    else {
                        acceptedAt = 7;
                        state = 8;
                        t = new Token(TokenType.MP_FIXED_LIT, lexeme, file.getLineIndex(), file.getColumnIndex()
                                - lexeme.length());
                    }
                } else {
                    acceptedAt = 7;
                    state = 8;
                }
                break;
            }

            if (state == 8) {

                TokenType type = null;
                switch (acceptedAt) {
                case 2:
                    type = TokenType.MP_INTEGER_LIT;
                    break;
                case 7:
                    type = TokenType.MP_FLOAT_LIT;
                    break;
                case 5:
                    type = TokenType.MP_FLOAT_LIT;
                    break;
                default:
                    break;
                }

                t = new Token(type, lexeme, file.getLineIndex(), file.getColumnIndex() - lexeme.length());
            }

        }
        return t;
    }
}
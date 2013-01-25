package compiler.fsa;

import compiler.Token;
import compiler.TokenType;
import compiler.io.MPFile;

/**
 * Responsible for finding the following tokens: MP_PERIOD, MP_COMMA, MP_SCOLON, MP_LPAREN, MP_RPAREN, MP_EQUAL, MP_GTHAN, MP_GEQUAL,
 * MP_LTHAN, MP_LEQUAL, MP_NEQUAL, MP_ASSIGN, MP_PLUS, MP_MINUS, MP_TIMES, MP_COLON
 */
public class SymbolFSA implements FSA {

    @Override
    public Token getToken(MPFile file) {
        Token t = null;
        int state = 1;
        String lexeme = "";
        char current = 0;

        while (state != 18) {
            if (!file.hasNextChar() && (state >= 2 && state <= 17)) {
                state = 18;
            } else {

                switch (state) {
                case 1:
                    current = file.getNextCharacter();
                    switch (current) {
                    case '.':
                        state = 2;
                        break;
                    case ',':
                        state = 3;
                        break;
                    case ';':
                        state = 4;
                        break;
                    case '(':
                        state = 5;
                        break;
                    case ')':
                        state = 6;
                        break;
                    case '=':
                        state = 7;
                        break;
                    case '+':
                        state = 8;
                        break;
                    case '-':
                        state = 9;
                        break;
                    case '*':
                        state = 10;
                        break;
                    case ':':
                        state = 11;
                        break;
                    case '<':
                        state = 13;
                        break;
                    case '>':
                        state = 16;
                        break;
                    }
                    lexeme += current;

                    break;
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                    state = 18;
                    break;
                case 11: //:
                    current = file.getCurrentCharacter();
                    if (current == '=') {
                        current = file.getNextCharacter();
                        lexeme += current;
                        state = 12;
                    } else {
                        state = 18;
                    }
                    break;
                case 12: //:=
                    state = 18;
                    break;
                case 13: //<
                    current = file.getCurrentCharacter();
                    if (current == '>') {
                        current = file.getNextCharacter();
                        lexeme += current;
                        state = 14;
                    } else if (current == '=') {
                        current = file.getNextCharacter();
                        lexeme += current;
                        state = 15;
                    } else {
                        state = 18;
                    }
                    break;
                case 14: // <>
                    state = 18;
                    break;
                case 15: // <=
                    state = 18;
                    break;
                case 16: //>
                    current = file.getCurrentCharacter();
                    if (current == '=') {
                        current = file.getNextCharacter();
                        lexeme += current;
                        state = 17;
                    } else {
                        state = 18;
                    }
                    break;
                case 17: //>=
                    state = 18;
                    break;
                }
            }

        }
        System.out.println("lexeme: " + lexeme);

        TokenType type = null;
        switch (lexeme) {
        case ".":
            type = TokenType.MP_PERIOD;
            break;
        case ",":
            type = TokenType.MP_COMMA;
            break;
        case ";":
            type = TokenType.MP_SCOLON;
            break;
        case "(":
            type = TokenType.MP_LPAREN;
            break;
        case ")":
            type = TokenType.MP_RPAREN;
            break;
        case "=":
            type = TokenType.MP_EQUAL;
            break;
        case ">":
            type = TokenType.MP_GTHAN;
            break;
        case ">=":
            type = TokenType.MP_GEQUAL;
            break;
        case "<":
            type = TokenType.MP_LTHAN;
            break;
        case "<=":
            type = TokenType.MP_LEQUAL;
            break;
        case "<>":
            type = TokenType.MP_NEQUAL;
            break;
        case ":=":
            type = TokenType.MP_ASSIGN;
            break;
        case "+":
            type = TokenType.MP_PLUS;
            break;
        case "-":
            type = TokenType.MP_MINUS;
            break;
        case "*":
            type = TokenType.MP_TIMES;
            break;
        case ":":
            type = TokenType.MP_COLON;
            break;
        }

        t = new Token(type, lexeme, file.getLineIndex(), file.getColumnIndex() - lexeme.length());

        // System.out.println("leaving thing at " + file.getColumnIndex());
        System.out.println("found token: " + t.toString());

        return t;
    }

}

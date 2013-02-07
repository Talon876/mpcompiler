package compiler.parser;

import compiler.Token;
import compiler.TokenType;

public class RuleS {
    Token globalLookAhead;

    public void match(TokenType tokenInput)
    {

    }

    public void error()
    {

    }

    public void formalParameterSectionTail()
    {
        switch (globalLookAhead.getType())
        {
        case MP_FUNCTION: //21 FormalParameterSectionTail → mp_scolon FormalParameterSectionFormalParameterSectionTail
            match(TokenType.MP_SCOLON);
            formalParameterSection();
            formalParameterSectionTail();
            break;
        case MP_RPAREN: //22 FormalParameterSectionTail → &epsilon
            break;
        default:
            error();
        }
    }

    public void formalParameterSection()
    {
        switch (globalLookAhead.getType())
        {
        case MP_IDENTIFIER: //23 FormalParameterSection → ValueParameterSection
            valueParameterSection();
            break;
        case MP_VAR: //24 FormalParameterSection → VariableParameterSection
            variableParameterSection();
            break;
        default:
            error();
        }
    }

    public void valueParameterSection()
    {
        switch (globalLookAhead.getType())
        {
        case MP_IDENTIFIER: //25 ValueParameterSection → IdentifierList mp_colon Type
            identifierList();
            match(TokenType.MP_COLON);
            type();
            break;
        default:
            error();
        }
    }

    public void variableParameterSection()
    {
        switch (globalLookAhead.getType())
        {
        case MP_VAR: //26 VariableParameterSection → mp_var IdentifierList mp_colon Type
            match(TokenType.MP_VAR);
            identifierList();
            match(TokenType.MP_COLON);
            type();
            break;
        default:
            error();
        }
    }

    public void statementPart()
    {
        switch (globalLookAhead.getType())
        {
        case MP_BEGIN: //27 StatementPart → CompoundStatement
            compoundStatement();
            break;
        default:
            error();
        }
    }

    public void compoundStatement()
    {
        switch (globalLookAhead.getType())
        {
        case MP_BEGIN: //28 CompoundStatement → mp_begin StatementSequence mp_end
            match(TokenType.MP_BEGIN);
            statementSequence();
            match(TokenType.MP_END);
            break;
        default:
            error();
        }
    }

    public void statementSequence()
    {
        switch (globalLookAhead.getType())
        {
        case MP_IDENTIFIER: //29 StatementSequence → Statement StatementTail
            statement();
            statementTail();
            break;
        case MP_FOR:
        case MP_WHILE:
        case MP_UNTIL:
        case MP_REPEAT:
        case MP_IF:
        case MP_WRITE:
        case MP_READ:
        case MP_SCOLON:
        case MP_END:
        case MP_BEGIN:
            break;
        default:
            error();
        }
    }

    public void statementTail()
    {
        switch (globalLookAhead.getType())
        {
        case MP_SCOLON: //30 StatementTail → mp_scolon Statement StatementTail
            match(TokenType.MP_SCOLON);
            statement();
            statementTail();
            break;
        case MP_UNTIL: //31 StatementTail → &epsilon
        case MP_END:
            break;
        default:
            error();
        }
    }

    public void statement()
    {
        switch (globalLookAhead.getType())
        {
        case MP_UNTIL: //32 Statement → EmptyStatement
        case MP_ELSE:
        case MP_SCOLON:
        case MP_END:
            emptyStatement();
            break;
        case MP_BEGIN: //33 Statement → CompoundStatement
            compoundStatement();
            break;
        case MP_READ: //34 Statement → ReadStatement
            readStatement();
            break;
        case MP_WRITE: //35 Statement → WriteStatement
            writeStatement();
            break;
        case MP_IDENTIFIER:
            assignmentStatement(); //TODO: Fix Ambiguity //36 Statement → AssigmentStatement
            procedureStatement(); //TODO: Fix Ambiguity //41 Statement → ProcedureStatement
            break;
        case MP_IF:
            ifStatement(); //37 Statement → IfStatement
            break;
        case MP_WHILE:
            whileStatement(); //38 Statement → WhileStatement
            break;
        case MP_REPEAT:
            repeatStatement(); //39 Statement → RepeatStatement
            break;
        case MP_FOR:
            forStatement(); //40 Statement → ForStatement
            break;
        default:
            error();
        }
    }

    public void emptyStatement()
    {
        switch (globalLookAhead.getType())
        {
        case MP_UNTIL: //42 EmptyStatement → &epsilon
        case MP_ELSE:
        case MP_SCOLON:
        case MP_END:
            break;
        default:
            error();
        }
    }

    public void readStatement()
    {
        switch (globalLookAhead.getType())
        {
        case MP_READ: //43 ReadStatement → mp_read mp_lparen ReadParameter ReadParameterTail mp_rparen
            match(TokenType.MP_READ);
            match(TokenType.MP_LPAREN);
            readParameter();
            readParameterTail();
            match(TokenType.MP_RPAREN);
            break;
        default:
            error();
        }
    }

    public void readParameterTail()
    {
        switch (globalLookAhead.getType())
        {
        case MP_READ: //44 ReadParameterTail → mp_comma ReadParameter ReadParameterTail
            match(TokenType.MP_COMMA);
            readParameter();
            readParameterTail();
            break;
        case MP_RPAREN: //45 ReadParameterTail → &epsilon
            break;
        default:
            error();
        }
    }

    public void readParameter()
    {
        switch (globalLookAhead.getType())
        {
        case MP_IDENTIFIER: //46 ReadParameter → VariableIdentifier
            variableIdentifier();
            break;
        default:
            error();
        }
    }

    public void writeStatement()
    {
        switch (globalLookAhead.getType())
        {
        case MP_WRITE: //47 WriteStatement → mp_write mp_lparen WriteParameter WriteParameterTail mp_rparen
            match(TokenType.MP_WRITE);
            match(TokenType.MP_LPAREN);
            writeParameter();
            writeParameterTail();
            match(TokenType.MP_RPAREN);
            break;
        default:
            error();
        }
    }

    public void writeParameterTail()
    {
        switch (globalLookAhead.getType())
        {
        case MP_COMMA: //48 WriteParameterTail → mp_comma WriteParameter
            match(TokenType.MP_COMMA);
            writeParameter();
            break;
        case MP_RPAREN: //49 WriteParameterTail → &epsilon
            break;
        default:
            error();
        }
    }

    public void writeParameter()
    {
        switch (globalLookAhead.getType())
        {
        case MP_IDENTIFIER: //50 WriteParameter → OrdinalExpression
        case MP_LPAREN:
        case MP_NOT:
        case MP_INTEGER_LIT:
        case MP_MINUS:
        case MP_PLUS:
            ordinalExpression();
            break;
        default:
            error();
        }
    }

    public void assignmentStatement()
    {
        switch (globalLookAhead.getType())
        {
        case MP_IDENTIFIER: { //51 AssignmentStatement → VariableIdentifier mp_assign Expression //TODO:Fix Ambiguity
            variableIdentifier();
            match(TokenType.MP_ASSIGN);
            expression();
        }
            { //52 AssignmentStatement → FunctionIdentifier mp_assign Expression //TODO:Fix Ambiguity
                functionIdentifier();
                match(TokenType.MP_ASSIGN);
                expression();
            }
            break;
        default:
            error();
        }
    }
    
    //Now starting at Rule line 103
    public void functionIdentifier()
    {
        switch (globalLookAhead.getType())
        {
        case MP_IDENTIFIER: //103 FunctionIdentifier → mp_identifier
            match(TokenType.MP_IDENTIFIER);
            break;
        default:
            error();
        }
    }
    public void booleanExpression()
    {
        switch (globalLookAhead.getType())
        {
        case MP_IDENTIFIER: //104 BooleanExpression → Expression
        case MP_LPAREN:
        case MP_NOT:
        case MP_INTEGER_LIT:
        case MP_MINUS:
        case MP_PLUS:
            expression();
            break;
        default:
            error();
        }
    }
    public void ordinalExpression()
    {
        switch (globalLookAhead.getType())
        {
        case MP_IDENTIFIER: //105 BooleanExpression → Expression
        case MP_LPAREN:
        case MP_NOT:
        case MP_INTEGER_LIT:
        case MP_MINUS:
        case MP_PLUS:
            expression();
            break;
        default:
            error();
        }
    }
    public void identifierList()
    {
        switch (globalLookAhead.getType())
        {
        case MP_IDENTIFIER: //106 IdentifierList → mp_identifier IdentifierTail
            match(TokenType.MP_IDENTIFIER);
            identifierTail();
            break;
        default:
            error();
        }
    }
    public void identifierTail()
    {
        switch (globalLookAhead.getType())
        {
        case MP_IDENTIFIER: //107 IdentifierTail → mp_comma mp_identifier IdentifierTail
            match(TokenType.MP_COMMA);
            match(TokenType.MP_IDENTIFIER);
            identifierTail();
            break;
        case MP_COLON: //108 IdentifierTail → &epsilon
            break;
        default:
            error();
        }
    }
}

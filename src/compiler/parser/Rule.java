package compiler.parser;

import compiler.Token;
import compiler.TokenType;

public class Rule {

    Token globalLookAhead;

    //    public void applyRule()
    //    {
    //        
    //        switch(globalLookAhead)
    //        {
    //            case :
    //            default :
    //                error();
    //        }
    //        
    //            
    //    }

    public void match(TokenType tokenInput)
    {

    }
    
    public void error()
    {

    }

    /**
     * 
     */
    public void systemGoal()
    {
        switch (globalLookAhead.getType()) {
        case MP_PROGRAM:
            program();
            match(TokenType.MP_EOF);
            break;
        default:
            error();
        }
    }

    public void program()
    {
        switch (globalLookAhead.getType()) {
        case MP_PROGRAM:
            programHeading();
            match(TokenType.MP_SCOLON);
            block();
            match(TokenType.MP_PERIOD);
            break;
        default:
            error();
        }
    }

    public void programHeading()
    {
        switch (globalLookAhead.getType()) {
        case MP_PROGRAM:
            match(TokenType.MP_PROGRAM);
            programIdentifier();
            break;
        default:
            error();
        }
    }

    public void block()
    {
        switch (globalLookAhead.getType()) {
        case MP_PROCEDURE:
        case MP_VAR:
        case MP_FUNCTION:
            variableDeclarationPart();
            procedureAndFunctionDeclarationPart();
            statementPart();
            break;
        default:
            error();
        }
    }

    public void variableDeclarationPart()
    {
        switch (globalLookAhead.getType()) {
        case MP_VAR:
            match(TokenType.MP_PROGRAM);
            variableDeclaration();
            match(TokenType.MP_SCOLON);
            variableDeclarationTail();
            break;
        default:
            error();
        }
    }

    public void variableDeclarationTail()
    {
        switch (globalLookAhead.getType()) {
        case MP_IDENTIFIER:
            variableDeclaration();
            match(TokenType.MP_SCOLON);
            variableDeclarationTail();
            break;
        case MP_BEGIN:
        case MP_PROCEDURE:
        case MP_FUNCTION:
            break;
        default:
            error();
        }
    }

    public void variableDeclaration()
    {
        switch (globalLookAhead.getType()) {
        case MP_IDENTIFIER:
            identifierList();
            match(TokenType.MP_SCOLON);
            type();
            break;
        default:
            error();
        }
    }

    public void procedureDeclaration()
    {
        switch (globalLookAhead.getType()) {
        case MP_PROCEDURE:
            procedureHeading();
            match(TokenType.MP_SCOLON);
            block();
            match(TokenType.MP_SCOLON);
            break;
        default:
            error();
        }
    }

    public void functionDeclaration()
    {
        switch (globalLookAhead.getType()) {
        case MP_FUNCTION:
            functionHeading();
            match(TokenType.MP_SCOLON);
            block();
            match(TokenType.MP_SCOLON);
            break;
        default:
            error();
        }
    }

    public void procedureHeading()
    {
        switch (globalLookAhead.getType()) {
        case MP_PROCEDURE:
            match(TokenType.MP_PROCEDURE);
            procedureIdentifier();
            optimalFormalParameterList();
            break;
        default:
            error();
        }
    }

    public void functionHeading()
    {
        switch (globalLookAhead.getType()) {
        case MP_FUNCTION:
            match(TokenType.MP_FUNCTION);
            functionIdentifier();
            optimalFormalParameterList();
            match(TokenType.MP_SCOLON);
            type();
            break;
        default:
            error();
        }
    }

    public void optionalFormalParameterList()
    {
        switch (globalLookAhead.getType())
        {
        case MP_LPAREN:
            match(TokenType.MP_LPAREN);
            formalParameterSection();
            formalParameterSectionTail();
            match(TokenType.MP_RPAREN);
            break;
        case MP_SCOLON:
        case MP_COLON:
            break;
        default:
            error();
        }
    }

    public void type()
    {
        switch (globalLookAhead.getType())
        {
        case MP_INTEGER:
            match(TokenType.MP_INTEGER);
            break;
        case MP_FIXED:
            match(TokenType.MP_FIXED); //change that to a boolean that we need to create
            break;
        case MP_FLOAT:
            match(TokenType.MP_FLOAT);
            break;
        default:
            error();
        }
    }

    public void procedureAndFunctionDeclarationPart()
    {
        switch (globalLookAhead.getType()) {
        case MP_PROCEDURE:
            procedureDeclaration();
            procedureAndFunctionDeclarationPart();
            break;
        case MP_FUNCTION:
            functionDeclaration();
            procedureAndFunctionDeclarationPart();
            break;
        case MP_BEGIN:
            break;
        default:
            error();
        }
    }

}

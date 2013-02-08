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
            optionalFormalParameterList();
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
            optionalFormalParameterList();
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

    //Starting at 35

    public void ifStatement()
    {
        switch (globalLookAhead.getType())
        {
        case MP_IF:
            match(TokenType.MP_IF);
            booleanExpression();
            match(TokenType.MP_THEN);
            statement();
            optionalElsePart();
            break;
        default:
            error();
        }
    }

    public void optionalElsePart()
    {
        switch (globalLookAhead.getType())
        {
        case MP_ELSE:
            match(TokenType.MP_ELSE);
            statement();
            match(TokenType.MP_END); //TODO fix this so that the epsilon isn't ambiguous
            break;
        case MP_UNTIL:
        case MP_SCOLON:
        case MP_END:
            break;
        default:
            error();
        }
    }

    public void repeatStatement()
    {
        switch (globalLookAhead.getType())
        {
        case MP_REPEAT:
            match(TokenType.MP_REPEAT);
            statementSequence();
            match(TokenType.MP_UNTIL);
            booleanExpression();
            break;
        default:
            error();
        }
    }

    public void whileStatement()
    {
        switch (globalLookAhead.getType())
        {
        case MP_WHILE:
            match(TokenType.MP_WHILE);
            booleanExpression();
            match(TokenType.MP_DO);
            statement();
            break;
        default:
            error();
        }
    }

    public void forStatement()
    {
        switch (globalLookAhead.getType())
        {
        case MP_FOR:
            match(TokenType.MP_FOR);
            controlVariable();
            match(TokenType.MP_ASSIGN);
            initialValue();
            stepValue();
            finalValue();
            match(TokenType.MP_DO);
            statement();
            break;
        default:
            error();
        }
    }

    public void controlVariable()
    {
        switch (globalLookAhead.getType())
        {
        case MP_IDENTIFIER:
            variableIdentifier();
            break;
        default:
            error();
        }
    }

    public void initialValue()
    {
        switch (globalLookAhead.getType())
        {
        case MP_IDENTIFIER:
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

    public void stepValue()
    {
        switch (globalLookAhead.getType())
        {
        case MP_TO:
            match(TokenType.MP_TO);
            break;
        case MP_DOWNTO:
            match(TokenType.MP_DOWNTO);
            break;
        default:
            error();
        }
    }

    public void finalValue() {
        switch (globalLookAhead.getType()) {
        case MP_IDENTIFIER:
        case MP_LPAREN:
        case MP_NOT:
        case MP_INTEGER_LIT:
        case MP_MINUS:
        case MP_PLUS:
            ordinalExpression(); //FinalValue -> OrdinalExpression
            break;
        default:
            error();
        }
    }

    public void procedureStatement() {
        switch (globalLookAhead.getType()) {
        case MP_IDENTIFIER:
            procedureIdentifier(); //ProcedureStatement -> ProcedureIdentifier OptionalActualParameterList
            optionalActualParameterList();
            break;
        default:
            error();
        }
    }

    public void optionalActualParameterList() {
        switch (globalLookAhead.getType()) {
        case MP_COMMA:
        case MP_RPAREN:
        case MP_AND:
        case MP_MOD:
        case MP_DIV:
        case MP_TIMES:
        case MP_OR:
        case MP_MINUS:
        case MP_PLUS:
        case MP_NEQUAL:
        case MP_GEQUAL:
        case MP_LEQUAL:
        case MP_GTHAN:
        case MP_LTHAN:
        case MP_EQUAL:
        case MP_DOWNTO:
        case MP_TO:
        case MP_DO:
        case MP_UNTIL:
        case MP_ELSE:
        case MP_THEN:
        case MP_SCOLON:
        case MP_END:
            break;
        case MP_LPAREN:
            match(TokenType.MP_LPAREN); //OptionalActualParameterList -> mp_lparen ActualParameter ActualParameterTail mp_rparen
            actualParameter();
            actualParameterTail();
            match(TokenType.MP_RPAREN);
            break;
        default:
            error();
        }
    }

    public void actualParameterTail() {
        switch (globalLookAhead.getType()) {
        case MP_COMMA:
            match(TokenType.MP_COMMA); //ActualParameterTail -> mp_comma ActualParameter ActualParameterTail
            actualParameter();
            actualParameterTail();
            break;
        case MP_RPAREN:
            break;
        default:
            error();
        }
    }

    public void actualParameter() {
        switch (globalLookAhead.getType()) {
        case MP_IDENTIFIER:
        case MP_LPAREN:
        case MP_NOT:
        case MP_INTEGER_LIT:
        case MP_MINUS:
        case MP_PLUS:
            ordinalExpression(); //ActualParameter -> OrdinalExpression
            break;
        default:
            error();
        }
    }

    public void expression() {
        switch (globalLookAhead.getType()) {
        case MP_IDENTIFIER:
        case MP_LPAREN:
        case MP_NOT:
        case MP_INTEGER_LIT:
        case MP_MINUS:
        case MP_PLUS:
            simpleExpression(); //Expression -> SimpleExpression OptionalRelationalPart
            optionalRelationalPart();
            break;
        default:
            error();
        }
    }

    public void optionalRelationalPart() {
        switch (globalLookAhead.getType()) {
        case MP_COMMA:
        case MP_RPAREN:
        case MP_DOWNTO:
        case MP_TO:
        case MP_DO:
        case MP_UNTIL:
        case MP_ELSE:
        case MP_THEN:
        case MP_SCOLON:
        case MP_END:
            break;
        case MP_NEQUAL:
        case MP_GEQUAL:
        case MP_LEQUAL:
        case MP_GTHAN:
        case MP_LTHAN:
        case MP_EQUAL:
            relationalOperator(); //OptionalRelationalPart -> RelationalOperator SimpleExpression
            simpleExpression();
        default:
            error();
        }
    }

    public void relationalOperator() {
        switch (globalLookAhead.getType()) {
        case MP_NEQUAL:
            match(TokenType.MP_NEQUAL);
            break;
        case MP_GEQUAL:
            match(TokenType.MP_GEQUAL);
            break;
        case MP_LEQUAL:
            match(TokenType.MP_LEQUAL);
            break;
        case MP_GTHAN:
            match(TokenType.MP_GTHAN);
            break;
        case MP_LTHAN:
            match(TokenType.MP_LTHAN);
            break;
        case MP_EQUAL:
            match(TokenType.MP_EQUAL);
            break;
        default:
            error();
        }
    }

    public void simpleExpression() {
        switch (globalLookAhead.getType()) {
        case MP_IDENTIFIER:
        case MP_LPAREN:
        case MP_NOT:
        case MP_INTEGER_LIT:
        case MP_MINUS:
        case MP_PLUS:
            optionalSign(); //SimpleExpression -> OptionalSign Term TermTail
            term();
            termTail();
            break;
        default:
            error();
        }
    }

    public void termTail() {
        switch (globalLookAhead.getType()) {
        case MP_COMMA:
        case MP_RPAREN:
        case MP_NEQUAL:
        case MP_GEQUAL:
        case MP_LEQUAL:
        case MP_GTHAN:
        case MP_LTHAN:
        case MP_EQUAL:
        case MP_DOWNTO:
        case MP_TO:
        case MP_DO:
        case MP_UNTIL:
        case MP_ELSE:
        case MP_THEN:
        case MP_SCOLON:
        case MP_END:
            break;
        case MP_OR:
        case MP_MINUS:
        case MP_PLUS:
            addingOperator(); //TermTail -> AddingOperator Term TermTail
            term();
            termTail();
        default:
            error();
        }
    }

    public void optionalSign() {
        switch (globalLookAhead.getType()) {
        case MP_IDENTIFIER:
        case MP_LPAREN:
        case MP_NOT:
        case MP_INTEGER_LIT:
            break;
        case MP_MINUS:
            match(TokenType.MP_MINUS); //OptionalSign -> mp_minus
            break;
        case MP_PLUS:
            match(TokenType.MP_PLUS); //OptionalSign -> mp_plus
            break;
        default:
            error();
        }
    }

    public void addingOperator() {
        switch (globalLookAhead.getType()) {
        case MP_OR:
            match(TokenType.MP_OR); //AddingOperator -> mp_or
            break;
        case MP_MINUS:
            match(TokenType.MP_MINUS); //AddingOperator -> mp_minus
            break;
        case MP_PLUS:
            match(TokenType.MP_PLUS); //AddingOperator -> mp_plus
            break;
        default:
            error();
        }
    }

    public void term() {
        switch (globalLookAhead.getType()) {
        case MP_IDENTIFIER:
        case MP_LPAREN:
        case MP_NOT:
        case MP_INTEGER_LIT:
            factor(); //Term -> Factor FactorTail
            factorTail();
            break;
        default:
            error();
        }
    }

    public void factorTail() {
        switch (globalLookAhead.getType()) {
        case MP_COMMA:
        case MP_RPAREN:
        case MP_OR:
        case MP_MINUS:
        case MP_PLUS:
        case MP_NEQUAL:
        case MP_GEQUAL:
        case MP_LEQUAL:
        case MP_GTHAN:
        case MP_LTHAN:
        case MP_EQUAL:
        case MP_DOWNTO:
        case MP_TO:
        case MP_DO:
        case MP_UNTIL:
        case MP_ELSE:
        case MP_THEN:
        case MP_SCOLON:
        case MP_END:
            break;
        case MP_AND:
        case MP_MOD:
        case MP_DIV:
        case MP_TIMES:
            multiplyingOperator(); //FactorTail -> MultiplyingOperator Factor FactorTail
            factor();
            factorTail();
            break;
        default:
            error();
        }
    }

    public void multiplyingOperator() {
        switch (globalLookAhead.getType()) {
        case MP_AND:
            match(TokenType.MP_AND);
            break;
        case MP_MOD:
            match(TokenType.MP_MOD);
            break;
        case MP_DIV:
            match(TokenType.MP_DIV);
            break;
        case MP_TIMES:
            match(TokenType.MP_TIMES);
            break;
        default:
            error();
        }
    }

    public void factor() {
        switch (globalLookAhead.getType()) {
        case MP_IDENTIFIER:
            variableIdentifier(); //Factor -> VariableIdentifier
            //TODO make this not ambiguous
            functionIdentifier(); //Factor -> FunctionIdentifier OptionalActualParameterList
            optionalActualParameterList();
            break;
        case MP_LPAREN:
            match(TokenType.MP_LPAREN); //Factor -> mp_lparen Expression mp_rparen
            expression();
            match(TokenType.MP_RPAREN);
            break;
        case MP_NOT:
            match(TokenType.MP_NOT); //Factor -> mp_not Factor
            factor();
            break;
        case MP_INTEGER_LIT:
            match(TokenType.MP_INTEGER_LIT); //Factor -> mp_integer_lit
            break;
        default:
            error();
        }
    }

    public void programIdentifier() {
        switch (globalLookAhead.getType()) {
        case MP_IDENTIFIER:
            match(TokenType.MP_IDENTIFIER);
            break;
        default:
            error();
        }
    }

    public void variableIdentifier() {
        switch (globalLookAhead.getType()) {
        case MP_IDENTIFIER:
            match(TokenType.MP_IDENTIFIER);
        default:
            error();
        }
    }

    public void procedureIdentifier() {
        switch (globalLookAhead.getType()) {
        case MP_IDENTIFIER:
            match(TokenType.MP_IDENTIFIER);
        default:
            error();
        }
    }

    public void formalParameterSectionTail()
    {
        switch (globalLookAhead.getType())
        {
        case MP_FUNCTION: //21 FormalParameterSectionTail �����mp_scolon FormalParameterSectionFormalParameterSectionTail
            match(TokenType.MP_SCOLON);
            formalParameterSection();
            formalParameterSectionTail();
            break;
        case MP_RPAREN: //22 FormalParameterSectionTail ��� &epsilon
            break;
        default:
            error();
        }
    }

    public void formalParameterSection()
    {
        switch (globalLookAhead.getType())
        {
        case MP_IDENTIFIER: //23 FormalParameterSection����� ValueParameterSection
            valueParameterSection();
            break;
        case MP_VAR: //24 FormalParameterSection����� VariableParameterSection
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
        case MP_IDENTIFIER: //25 ValueParameterSection����� IdentifierList��mp_colon��Type
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
        case MP_VAR: //26 VariableParameterSection�������mp_var IdentifierList��mp_colon��Type
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
        case MP_BEGIN: //27 StatementPart����� CompoundStatement
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
        case MP_BEGIN: //28 CompoundStatement�������mp_begin StatementSequence��mp_end
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
        case MP_IDENTIFIER: //29 StatementSequence����� Statement��StatementTail
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
        case MP_SCOLON: //30 StatementTail�������mp_scolon Statement��StatementTail
            match(TokenType.MP_SCOLON);
            statement();
            statementTail();
            break;
        case MP_UNTIL: //31 StatementTail�������&epsilon
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
        case MP_UNTIL: //32 Statement�������EmptyStatement
        case MP_ELSE:
        case MP_SCOLON:
        case MP_END:
            emptyStatement();
            break;
        case MP_BEGIN: //33 Statement�������CompoundStatement
            compoundStatement();
            break;
        case MP_READ: //34 Statement�������ReadStatement
            readStatement();
            break;
        case MP_WRITE: //35 Statement�������WriteStatement
            writeStatement();
            break;
        case MP_IDENTIFIER:
            assignmentStatement(); //TODO: Fix Ambiguity //36 Statement �����AssigmentStatement
            procedureStatement(); //TODO: Fix Ambiguity //41 Statement �����ProcedureStatement
            break;
        case MP_IF:
            ifStatement(); //37 Statement �����IfStatement
            break;
        case MP_WHILE:
            whileStatement(); //38 Statement �����WhileStatement
            break;
        case MP_REPEAT:
            repeatStatement(); //39 Statement �����RepeatStatement
            break;
        case MP_FOR:
            forStatement(); //40 Statement �����ForStatement
            break;
        default:
            error();
        }
    }

    public void emptyStatement()
    {
        switch (globalLookAhead.getType())
        {
        case MP_UNTIL: //42 EmptyStatement�������&epsilon
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
        case MP_READ: //43 ReadStatement����� mp_read mp_lparen ReadParameter ReadParameterTail mp_rparen
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
        case MP_READ: //44 ReadParameterTail����� mp_comma��ReadParameter ReadParameterTail
            match(TokenType.MP_COMMA);
            readParameter();
            readParameterTail();
            break;
        case MP_RPAREN: //45 ReadParameterTail����� &epsilon
            break;
        default:
            error();
        }
    }

    public void readParameter()
    {
        switch (globalLookAhead.getType())
        {
        case MP_IDENTIFIER: //46 ReadParameter����� VariableIdentifier
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
        case MP_WRITE: //47 WriteStatement����� mp_write mp_lparen WriteParameter WriteParameterTail mp_rparen
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
        case MP_COMMA: //48 WriteParameterTail����� mp_comma��WriteParameter
            match(TokenType.MP_COMMA);
            writeParameter();
            break;
        case MP_RPAREN: //49 WriteParameterTail����� &epsilon
            break;
        default:
            error();
        }
    }

    public void writeParameter()
    {
        switch (globalLookAhead.getType())
        {
        case MP_IDENTIFIER: //50 WriteParameter����� OrdinalExpression
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
        case MP_IDENTIFIER: { //51 AssignmentStatement����� VariableIdentifier��mp_assign Expression //TODO:Fix Ambiguity
            variableIdentifier();
            match(TokenType.MP_ASSIGN);
            expression();
        }
        { //52 AssignmentStatement����� FunctionIdentifier��mp_assign Expression //TODO:Fix Ambiguity
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
        case MP_IDENTIFIER: //103 FunctionIdentifier����� mp_identifier
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
        case MP_IDENTIFIER: //104 BooleanExpression����� Expression
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
        case MP_IDENTIFIER: //105 BooleanExpression����� Expression
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
        case MP_IDENTIFIER: //106 IdentifierList����� mp_identifier��IdentifierTail
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
        case MP_IDENTIFIER: //107 IdentifierTail�������mp_comma mp_identifier��IdentifierTail
            match(TokenType.MP_COMMA);
            match(TokenType.MP_IDENTIFIER);
            identifierTail();
            break;
        case MP_COLON: //108 IdentifierTail�������&epsilon
            break;
        default:
            error();
        }
    }
}

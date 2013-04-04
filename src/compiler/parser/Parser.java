package compiler.parser;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Stack;

import compiler.Scanner;
import compiler.Token;
import compiler.TokenType;
import compiler.parser.symbol.SymbolTable;

public class Parser {
    private static final boolean DEBUG = false;

    Token lookAhead;
    Token lookAhead2;
    Scanner scanner;
    PrintWriter out;
    Stack<SymbolTable> symboltables;

    public Parser(Scanner scanner) {
        this.scanner = scanner;
        this.symboltables = new Stack<SymbolTable>();
        lookAhead = scanner.getToken();
        try {
            out = new PrintWriter(new FileWriter("parse-tree"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        systemGoal();
        out.flush();
        out.close();
        System.out.println("The input program parses!");
    }

    public void match(TokenType tokenInput) {
        if (lookAhead.getType() == tokenInput) {
            if (DEBUG) {
                System.out.println("Matched token: " + lookAhead.getType() + " with " + tokenInput);
            }
            //get next lookahead
            lookAhead = scanner.getToken();
        } else {
            matchError(tokenInput.toString());
        }
    }

    public void matchError(String expectedToken) {
        System.out.println("Match error found on line " + lookAhead.getLineNumber() + ", column "
                + lookAhead.getColumnNumber() + ": expected '" + expectedToken
                + "', but found '" + lookAhead.getLexeme() + "'");
    }

    public void syntaxError(String expectedToken) {
        System.out.println("Syntax error found on line " + lookAhead.getLineNumber() + ", column "
                + lookAhead.getColumnNumber() + ": expected one of the following tokens {" + expectedToken
                + "}, but found '" + lookAhead.getLexeme() + "'");
        if (DEBUG) {
            System.out.println("Current lookahead token: " + lookAhead.toString());
        }
        out.flush();
        out.close();
        System.exit(1);
    }

    private boolean addSymbolTable(String scopeName)
    {
        boolean exists = false;
        for (SymbolTable t : symboltables)
        {
            if (t.getScopeName().equalsIgnoreCase(scopeName))
            {
                exists = true;
                break;
            }
        }
        if (!exists)
        {
            symboltables.push(new SymbolTable(scopeName));
            return true;
        }
        return false;
    }

    private void removeSymbolTable(String scopeName)
    {
        symboltables.pop();
    }

    public void printSymbolTables()
    {
        for(SymbolTable st : symboltables)
        {
            st.print();
        }
    }
    public void debug() {
        if (DEBUG) {
            System.out.println("\tExpanding nonterminal: " + Thread.currentThread().getStackTrace()[2].getMethodName()
                    + "() and current lookahead: " + lookAhead.getType());
        }
    }

    public void lambda() {
        if (DEBUG) {
            System.out.println("\tExpanding lambda rule in "
                    + Thread.currentThread().getStackTrace()[2].getMethodName()
                    + "()");
        }
    }

    public void systemGoal()
    {
        debug();
        switch (lookAhead.getType()) {
        case MP_PROGRAM: //1 SystemGoal -> Program mp_eof
            out.println("1");
            program();
            match(TokenType.MP_EOF);
            break;
        default:
            syntaxError("program");
        }
    }

    public void program()
    {
        debug();
        switch (lookAhead.getType()) {
        case MP_PROGRAM: //2 Program -> Programheading mp_scolon Block mp_period
            out.println("2");
            programHeading();
            match(TokenType.MP_SCOLON);
            block();
            match(TokenType.MP_PERIOD);
            break;
        default:
            syntaxError("program");
        }
    }

    public void programHeading()
    {
        debug();
        switch (lookAhead.getType()) {
        case MP_PROGRAM: //3 ProgramHeading -> mp_program ProgramIdentifier
            out.println("3");
            match(TokenType.MP_PROGRAM);
            programIdentifier();
            break;
        default:
            syntaxError("program");
        }
    }

    public void block()
    {
        debug();
        switch (lookAhead.getType()) {
        case MP_VAR: //4 Block -> VariableDeclarationPart ProcedureAndFunctionDeclarationPart StatementPart
            out.println("4");
            variableDeclarationPart();
            procedureAndFunctionDeclarationPart();
            statementPart();
            break;
        default:
            syntaxError("var");
        }
    }

    public void variableDeclarationPart()
    {
        debug();
        switch (lookAhead.getType()) {
        case MP_VAR: //5 VariableDeclarationPart -> mp_var VariableDeclaration mp_scolon VariableDeclarationTail
            out.println("5");
            match(TokenType.MP_VAR);
            variableDeclaration();
            match(TokenType.MP_SCOLON);
            variableDeclarationTail();
            break;
        default:
            syntaxError("var");
        }
    }

    public void variableDeclarationTail()
    {
        debug();
        switch (lookAhead.getType()) {
        case MP_IDENTIFIER: //6 VariableDeclarationTail -> VariableDeclaration mp_scolon VariableDeclarationTail
            out.println("6");
            variableDeclaration();
            match(TokenType.MP_SCOLON);
            variableDeclarationTail();
            break;
        case MP_BEGIN:
        case MP_PROCEDURE:
        case MP_FUNCTION: //7 VariableDeclarationTail -> lambda
            out.println("7");
            lambda();
            break;
        default:
            syntaxError("identifier, begin, procedure, function");
        }
    }

    public void variableDeclaration()
    {
        debug();
        switch (lookAhead.getType()) {
        case MP_IDENTIFIER: //8 VariableDeclaration -> IdentifierList mp_colon Type #insert
            out.println("8");
            identifierList();
            match(TokenType.MP_COLON);
            type();
            break;
        default:
            syntaxError("identifier");
        }
    }

    public void type()
    {
        debug();
        switch (lookAhead.getType())
        {
        case MP_INTEGER: //9 Type -> mp_integer
            out.println("9");
            match(TokenType.MP_INTEGER);
            break;
        case MP_BOOLEAN: //110 Type -> mp_boolean
            out.println("110");
            match(TokenType.MP_BOOLEAN);
            break;
        case MP_FLOAT: //108 Type -> mp_float
            out.println("108");
            match(TokenType.MP_FLOAT);
            break;
        case MP_STRING: //109 Type -> mp_string
            out.println("109");
            match(TokenType.MP_STRING);
            break;
        default:
            syntaxError("Integer, Float, Boolean, String");
        }
    }

    public void procedureAndFunctionDeclarationPart()
    {
        debug();
        switch (lookAhead.getType()) {
        case MP_PROCEDURE: //10 ProcedureAndFunctionDeclarationPart -> ProcedureDeclaration ProcedureAndFunctionDeclarationPart
            out.println("10");
            procedureDeclaration();
            procedureAndFunctionDeclarationPart();
            break;
        case MP_FUNCTION: //11 ProcedureAndFunctionDeclarationPart -> FunctionDeclaration ProcedureAndFunctionDeclarationPart
            out.println("11");
            functionDeclaration();
            procedureAndFunctionDeclarationPart();
            break;
        case MP_BEGIN: //12 ProcedureAndFunctionDeclarationPart -> lambda
            out.println("12");
            lambda();
            break;
        default:
            syntaxError("procedure, function, begin");
        }
    }

    public void procedureDeclaration()
    {
        debug();
        switch (lookAhead.getType()) {
        case MP_PROCEDURE: //13 ProcedureDeclaration -> ProcedureHeading mp_scolon Block mp_scolon #destroy
            out.println("13");
            procedureHeading();
            match(TokenType.MP_SCOLON);
            block();
            match(TokenType.MP_SCOLON);
            break;
        default:
            syntaxError("procedure");
        }
    }

    public void functionDeclaration()
    {
        debug();
        switch (lookAhead.getType()) {
        case MP_FUNCTION: //14 FunctionDeclaration -> FunctionHeading mp_scolon Block mp_scolon #Destroy
            out.println("14");
            functionHeading();
            match(TokenType.MP_SCOLON);
            block();
            match(TokenType.MP_SCOLON);
            break;
        default:
            syntaxError("function");
        }
    }

    public void procedureHeading()
    {
        debug();
        switch (lookAhead.getType()) {
        case MP_PROCEDURE: //15 ProcedureHeading -> mp_procedure ProcedureIdentifier #create OptionalFormalParameterList #insert
            out.println("15");
            match(TokenType.MP_PROCEDURE);
            procedureIdentifier();
            optionalFormalParameterList();
            break;
        default:
            syntaxError("procedure");
        }
    }

    public void functionHeading()
    {
        debug();
        switch (lookAhead.getType()) {
        case MP_FUNCTION: //16 FunctionHeading -> mp_function FunctionIdentifier OptionalFormalParameterList mp_colon Type
            out.println("16");
            match(TokenType.MP_FUNCTION);
            functionIdentifier();
            optionalFormalParameterList();
            match(TokenType.MP_SCOLON);
            type();
            break;
        default:
            syntaxError("function");
        }
    }

    public void optionalFormalParameterList()
    {
        debug();
        switch (lookAhead.getType())
        {
        case MP_LPAREN: //17 OptionalFormalParameterList -> mp_lparen FormalParameterSection FormalParameterSectionTail mp_rparen
            out.println("17");
            match(TokenType.MP_LPAREN);
            formalParameterSection();
            formalParameterSectionTail();
            match(TokenType.MP_RPAREN);
            break;
        case MP_SCOLON:
        case MP_COLON: //18 OptionalFormalParameterList -> lambda
            out.println("18");
            lambda();
            break;
        default:
            syntaxError("(, ;, :");
        }
    }

    public void ifStatement()
    {
        debug();
        switch (lookAhead.getType())
        {
        case MP_IF: //51 IfStatement -> mp_if BooleanExpression mp_then Statement OptionalElsePart
            out.println("51");
            match(TokenType.MP_IF);
            booleanExpression();
            match(TokenType.MP_THEN);
            statement();
            optionalElsePart();
            break;
        default:
            syntaxError("if");
        }
    }

    public void optionalElsePart()
    {
        debug();
        switch (lookAhead.getType())
        {
        case MP_ELSE: //52 OptionalElsePart -> mp_else Statement
            out.println("52");
            match(TokenType.MP_ELSE);
            statement();
            match(TokenType.MP_END); //TODO fix this so that the epsilon isn't ambiguous
            break;
        case MP_UNTIL:
        case MP_SCOLON:
        case MP_END: //53 OptionalElsePart -> lambda
            out.println("53");
            lambda();
            break;
        default:
            syntaxError("else, until, ;, end");
        }
    }

    public void repeatStatement()
    {
        debug();
        switch (lookAhead.getType())
        {
        case MP_REPEAT: //54 RepeatStatement -> mp_repeat StatementSequence mp_until BooleanExpression
            out.println("54");
            match(TokenType.MP_REPEAT);
            statementSequence();
            match(TokenType.MP_UNTIL);
            booleanExpression();
            break;
        default:
            syntaxError("repeat");
        }
    }

    public void whileStatement()
    {
        debug();
        switch (lookAhead.getType())
        {
        case MP_WHILE: //55 WhileStatement -> mp_while BooleanExpression mp_do Statement
            out.println("55");
            match(TokenType.MP_WHILE);
            booleanExpression();
            match(TokenType.MP_DO);
            statement();
            break;
        default:
            syntaxError("while");
        }
    }

    public void forStatement()
    {
        debug();
        switch (lookAhead.getType())
        {
        case MP_FOR: //56 ForStatement -> mp_for ControlVariable mp_assign InitialValue StepValue FinalValue mp_do Statement
            out.println("56");
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
            syntaxError("for");
        }
    }

    public void controlVariable()
    {
        debug();
        switch (lookAhead.getType())
        {
        case MP_IDENTIFIER: //57 ControlVariable -> VariableIdentifier
            out.println("57");
            variableIdentifier();
            break;
        default:
            syntaxError("identifier");
        }
    }

    public void initialValue()
    {
        debug();
        switch (lookAhead.getType())
        {
        case MP_IDENTIFIER:
        case MP_LPAREN:
        case MP_NOT:
        case MP_INTEGER_LIT:
        case MP_MINUS:
        case MP_PLUS: //58 InitialValue -> OrdinalExpression
            out.println("58");
            ordinalExpression();
            break;
        default:
            syntaxError("identifier, (, not, Integer, -, +");
        }
    }

    public void stepValue()
    {
        debug();
        switch (lookAhead.getType())
        {
        case MP_TO: //59 StepValue -> mp_to
            out.println("59");
            match(TokenType.MP_TO);
            break;
        case MP_DOWNTO: //60 StepValue -> mp_downto
            out.println("60");
            match(TokenType.MP_DOWNTO);
            break;
        default:
            syntaxError("to, downto");
        }
    }

    public void finalValue() {
        debug();
        switch (lookAhead.getType()) {
        case MP_IDENTIFIER:
        case MP_LPAREN:
        case MP_NOT:
        case MP_INTEGER_LIT:
        case MP_MINUS:
        case MP_PLUS: //61 FinalValue -> OrdinalExpression
            out.println("61");
            ordinalExpression();
            break;
        default:
            syntaxError("identifier, (, not, Integer, -, +");
        }
    }

    public void procedureStatement() {
        debug();
        switch (lookAhead.getType()) {
        case MP_IDENTIFIER: //62 ProcedureStatement -> ProcedureIdentifier OptionalActualParameterList
            out.println("62");
            procedureIdentifier();
            optionalActualParameterList();
            break;
        default:
            syntaxError("identifier");
        }
    }

    public void optionalActualParameterList() {
        debug();
        switch (lookAhead.getType()) {
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
        case MP_END: //64 OptionalActualParameterList -> lambda
            out.println("64");
            lambda();
            break;
        case MP_LPAREN: //63 OptionalActualParameterList -> mp_lparen ActualParameter ActualParameterTail mp_rparen
            out.println("63");
            match(TokenType.MP_LPAREN);
            actualParameter();
            actualParameterTail();
            match(TokenType.MP_RPAREN);
            break;
        default:
            syntaxError("',', ), and, mod, div, *, 'or', -, +, <>, >=, <=, <, >, =, downto, to, do, until, else, then, ;, end");
        }
    }

    public void actualParameterTail() {
        debug();
        switch (lookAhead.getType()) {
        case MP_COMMA: //65 ActualParameterTail -> mp_comma ActualParameter ActualParameterTail
            out.println("65");
            match(TokenType.MP_COMMA);
            actualParameter();
            actualParameterTail();
            break;
        case MP_RPAREN: //66 ActualParameterTail -> lambda
            out.println("66");
            lambda();
            break;
        default:
            syntaxError("',', )");
        }
    }

    public void actualParameter() {
        debug();
        switch (lookAhead.getType()) {
        case MP_IDENTIFIER:
        case MP_LPAREN:
        case MP_NOT:
        case MP_INTEGER_LIT:
        case MP_MINUS:
        case MP_PLUS: //67 ActualParameter -> OrdinalExpression
            out.println("67");
            ordinalExpression();
            break;
        default:
            syntaxError("identifier, (, not, Integer, -, +");
        }
    }

    public void expression() {
        debug();
        switch (lookAhead.getType()) {
        case MP_IDENTIFIER:
        case MP_LPAREN:
        case MP_NOT:
        case MP_INTEGER_LIT:
        case MP_MINUS:
        case MP_PLUS: //68 Expression -> SimpleExpression OptionalRelationalPart
            out.println("68");
            simpleExpression();
            optionalRelationalPart();
            break;
        default:
            syntaxError("identifier, (, not, Integer, -, +");
        }
    }

    public void optionalRelationalPart() {
        debug();
        switch (lookAhead.getType()) {
        case MP_COMMA:
        case MP_RPAREN:
        case MP_DOWNTO:
        case MP_TO:
        case MP_DO:
        case MP_UNTIL:
        case MP_ELSE:
        case MP_THEN:
        case MP_SCOLON:
        case MP_END: //70 OptionalRelationalPart -> lambda
            out.println("70");
            lambda();
            break;
        case MP_NEQUAL:
        case MP_GEQUAL:
        case MP_LEQUAL:
        case MP_GTHAN:
        case MP_LTHAN:
        case MP_EQUAL: //69 OptionalRelationalPart -> RelationalOperator SimpleExpression
            out.println("69");
            relationalOperator();
            simpleExpression();
            break;
        default:
            syntaxError("',', ), downto, to, do, until, else, then, ;, end, <>, >=, <=, >, <, =");
        }
    }

    public void relationalOperator() {
        debug();
        switch (lookAhead.getType()) {
        case MP_NEQUAL: //76 RelationalOperator -> mp_nequal
            out.println("76");
            match(TokenType.MP_NEQUAL);
            break;
        case MP_GEQUAL: //75 RelationalOperator -> mp_gequal
            out.println("75");
            match(TokenType.MP_GEQUAL);
            break;
        case MP_LEQUAL: //74 RelationalOperator -> mp_lequal
            out.println("74");
            match(TokenType.MP_LEQUAL);
            break;
        case MP_GTHAN: //73 RelationalOperator -> mp_gthan
            out.println("73");
            match(TokenType.MP_GTHAN);
            break;
        case MP_LTHAN: //72 RelationalOperator -> mp_lthan
            out.println("72");
            match(TokenType.MP_LTHAN);
            break;
        case MP_EQUAL: //71 RelationalOperator -> mp_equal
            out.println("71");
            match(TokenType.MP_EQUAL);
            break;
        default:
            syntaxError("<>, >=, <= , >, <, =");
        }
    }

    public void simpleExpression() {
        debug();
        switch (lookAhead.getType()) {
        case MP_IDENTIFIER:
        case MP_LPAREN:
        case MP_NOT:
        case MP_INTEGER_LIT:
        case MP_MINUS:
        case MP_PLUS: //77 SimpleExpression -> OptionalSign Term TermTail
            out.println("77");
            optionalSign();
            term();
            termTail();
            break;
        default:
            syntaxError("identifier, (, not, Integer, -, +");
        }
    }

    public void termTail() {
        debug();
        switch (lookAhead.getType()) {
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
        case MP_END: //79 TermTail -> lambda
            out.println("79");
            lambda();
            break;
        case MP_OR:
        case MP_MINUS:
        case MP_PLUS: //78 TermTail -> AddingOperator Term TermTail
            out.println("78");
            addingOperator();
            term();
            termTail();
            break;
        default:
            syntaxError("',', ), <>, >=, <=, >, <, =, downto, to, do, until, else, then, ;, end, or, -, +");
        }
    }

    public void optionalSign() {
        debug();
        switch (lookAhead.getType()) {
        case MP_IDENTIFIER:
        case MP_LPAREN:
        case MP_NOT:
        case MP_INTEGER_LIT: //82 OptionalSign -> lambda
            out.println("82");
            lambda();
            break;
        case MP_MINUS: //81 OptionalSign -> mp_minus
            out.println("81");
            match(TokenType.MP_MINUS);
            break;
        case MP_PLUS: //80 OptionalSign -> mp_plus
            out.println("80");
            match(TokenType.MP_PLUS);
            break;
        default:
            syntaxError("identifier, (, not, Integer, -, +");
        }
    }

    public void addingOperator() {
        debug();
        switch (lookAhead.getType()) {
        case MP_OR: //85 AddingOperator -> mp_or
            out.println("85");
            match(TokenType.MP_OR);
            break;
        case MP_MINUS: //84 AddingOperator -> mp_minus
            out.println("84");
            match(TokenType.MP_MINUS);
            break;
        case MP_PLUS: //83 AddingOperator -> mp_plus
            out.println("83");
            match(TokenType.MP_PLUS);
            break;
        default:
            syntaxError("or, -, +");
        }
    }

    public void term() {
        debug();
        switch (lookAhead.getType()) {
        case MP_IDENTIFIER:
        case MP_LPAREN:
        case MP_NOT:
        case MP_INTEGER_LIT: //86 Term -> Factor FactorTail
            out.println("86");
            factor();
            factorTail();
            break;
        default:
            syntaxError("identifier, (, not, Integer");
        }
    }

    public void factorTail() {
        debug();
        switch (lookAhead.getType()) {
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
        case MP_END: //88 FactorTail -> lambda
            out.println("88");
            lambda();
            break;
        case MP_AND:
        case MP_MOD:
        case MP_DIV:
        case MP_TIMES: //87 FactorTail -> MultiplyingOperator Factor FactorTail
            out.println("87");
            multiplyingOperator();
            factor();
            factorTail();
            break;
        default:
            syntaxError("',', ), or, -, +, <>, >=, <=, >, <, =, downto, to, do, until, else, then, ;, end, and, mod, div, *");
        }
    }

    public void multiplyingOperator() {
        debug();
        switch (lookAhead.getType()) {
        case MP_AND: //92 MultiplyingOperator -> mp_and
            out.println("92");
            match(TokenType.MP_AND);
            break;
        case MP_MOD: //91 MultiplyingOperator -> mp_mod
            out.println("91");
            match(TokenType.MP_MOD);
            break;
        case MP_DIV: //90 MultiplyingOperator -> mp_div
            out.println("90");
            match(TokenType.MP_DIV);
            break;
        case MP_TIMES: //89 MultiplyingOperator -> mp_times
            out.println("89");
            match(TokenType.MP_TIMES);
            break;
        default:
            syntaxError("and, mod, div, *");
        }
    }

    public void factor() {
        debug();
        switch (lookAhead.getType()) {
        case MP_IDENTIFIER: //94 Factor -> VariableIdentifier
            out.println("94");
            variableIdentifier();
            //TODO make this not ambiguous
            //functionIdentifier(); //97 Factor -> FunctionIdentifier OptionalActualParameterList
            //out.println("97");
            // optionalActualParameterList();
            break;
        case MP_LPAREN: //96 Factor -> mp_lparen Expression mp_rparen
            out.println("96");
            match(TokenType.MP_LPAREN);
            expression();
            match(TokenType.MP_RPAREN);
            break;
        case MP_NOT: //95 Factor -> mp_not Factor
            out.println("95");
            match(TokenType.MP_NOT);
            factor();
            break;
        case MP_INTEGER_LIT: //93 Factor -> mp_integer_lit
            out.println("93");
            match(TokenType.MP_INTEGER_LIT);
            break;
        default:
            syntaxError("identifier, (, not, Integer");
        }
    }

    public void programIdentifier() {
        debug();
        switch (lookAhead.getType()) {
        case MP_IDENTIFIER: //98 ProgramIdentifier -> mp_identifier
            out.println("98");
            match(TokenType.MP_IDENTIFIER);
            break;
        default:
            syntaxError("identifier");
        }
    }

    public void variableIdentifier() {
        debug();
        switch (lookAhead.getType()) {
        case MP_IDENTIFIER: //99 VariableIdentifier -> mp_identifier
            out.println("99");
            match(TokenType.MP_IDENTIFIER);
            break;
        default:
            syntaxError("identifier");
        }
    }

    public void procedureIdentifier() {
        debug();
        switch (lookAhead.getType()) {
        case MP_IDENTIFIER: //100 ProcedureIdentifier -> mp_identifier
            out.println("100");
            match(TokenType.MP_IDENTIFIER);
            break;
        default:
            syntaxError("identifier");
        }
    }

    public void formalParameterSectionTail()
    {
        debug();
        switch (lookAhead.getType())
        {
        case MP_FUNCTION: //19 FormalParameterSectionTail -> mp_scolon FormalParameterSection FormalParameterSectionTail
            out.println("19");
            match(TokenType.MP_SCOLON);
            formalParameterSection();
            formalParameterSectionTail();
            break;
        case MP_RPAREN: //20 FormalParameterSectionTail -> &epsilon
            out.println("20");
            lambda();
            break;
        default:
            syntaxError("function, )");
        }
    }

    public void formalParameterSection()
    {
        debug();
        switch (lookAhead.getType())
        {
        case MP_IDENTIFIER: //21 FormalParameterSection -> ValueParameterSection #insert
            out.println("21");
            valueParameterSection();
            break;
        case MP_VAR: //22 FormalParameterSection -> VariableParameterSection #insert
            out.println("22");
            variableParameterSection();
            break;
        default:
            syntaxError("identifier, var");
        }
    }

    public void valueParameterSection()
    {
        debug();
        switch (lookAhead.getType())
        {
        case MP_IDENTIFIER: //23 ValueParameterSection -> IdentifierList mp_colon Type
            out.println("23");
            identifierList();
            match(TokenType.MP_COLON);
            type();
            break;
        default:
            syntaxError("identifier");
        }
    }

    public void variableParameterSection()
    {
        debug();
        switch (lookAhead.getType())
        {
        case MP_VAR: //24 VariableParameterSection -> mp_var IdentifierList mp_colon Type
            out.println("24");
            match(TokenType.MP_VAR);
            identifierList();
            match(TokenType.MP_COLON);
            type();
            break;
        default:
            syntaxError("var");
        }
    }

    public void statementPart()
    {
        debug();
        switch (lookAhead.getType())
        {
        case MP_BEGIN: //25 StatementPart -> CompoundStatement
            out.println("25");
            compoundStatement();
            break;
        default:
            syntaxError("begin");
        }
    }

    public void compoundStatement()
    {
        debug();
        switch (lookAhead.getType())
        {
        case MP_BEGIN: //26 CompoundStatement -> mp_begin StatementSequence mp_end
            out.println("26");
            match(TokenType.MP_BEGIN);
            statementSequence();
            match(TokenType.MP_END);
            break;
        default:
            syntaxError("begin");
        }
    }

    public void statementSequence()
    {
        debug();
        switch (lookAhead.getType())
        {
        case MP_IDENTIFIER: //27 StatementSequence -> Statement StatementTail
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
            out.println("27");
            statement();
            statementTail();
            break;
        default:
            syntaxError("identifier, for, while, until, repeat, if, write, read, ;, end, begin");
        }
    }

    public void statementTail()
    {
        debug();
        switch (lookAhead.getType())
        {
        case MP_SCOLON: //28 StatementTail -> mp_scolon Statement StatementTail
            out.println("28");
            match(TokenType.MP_SCOLON);
            statement();
            statementTail();
            break;
        case MP_UNTIL: //29 StatementTail -> &epsilon
        case MP_END:
            out.println("29");
            lambda();
            break;
        default:
            syntaxError(";, until, end");
        }
    }

    public void statement()
    {
        debug();
        switch (lookAhead.getType())
        {
        case MP_UNTIL: //30 Statement -> EmptyStatement
        case MP_ELSE:
        case MP_SCOLON:
        case MP_END:
            out.println("30");
            emptyStatement();
            break;
        case MP_BEGIN: //31 Statement -> CompoundStatement
            out.println("31");
            compoundStatement();
            break;
        case MP_READ: //32 Statement -> ReadStatement
            out.println("32");
            readStatement();
            break;
        case MP_WRITE: //33 Statement -> WriteStatement
            out.println("33");
            writeStatement();
            break;
        case MP_IDENTIFIER:
            out.println("34");
            assignmentStatement(); //TODO: Fix Ambiguity //34 Statement  -> AssigmentStatement
            //procedureStatement(); //TODO: Fix Ambiguity //39 Statement  -> ProcedureStatement
            //out.println("39");
            break;
        case MP_IF:
            ifStatement(); //35 Statement  -> IfStatement
            out.println("35");
            break;
        case MP_WHILE:
            whileStatement(); //36 Statement  -> WhileStatement
            out.println("36");
            break;
        case MP_REPEAT:
            repeatStatement(); //37 Statement  -> RepeatStatement
            out.println("37");
            break;
        case MP_FOR:
            forStatement(); //38 Statement  -> ForStatement
            out.println("38");
            break;
        default:
            syntaxError("until, else, ;, end, begin, Read, Write, identifier, if, while, repeat, for");
        }
    }

    public void emptyStatement()
    {
        debug();
        switch (lookAhead.getType())
        {
        case MP_UNTIL: //40 EmptyStatement -> &epsilon
        case MP_ELSE:
        case MP_SCOLON:
        case MP_END:
            out.println("40");
            lambda();
            break;
        default:
            syntaxError("until, else, ;, end");
        }
    }

    public void readStatement()
    {
        debug();
        switch (lookAhead.getType())
        {
        case MP_READ: //41 ReadStatement ->  mp_read mp_lparen ReadParameter ReadParameterTail mp_rparen
            out.println("41");
            match(TokenType.MP_READ);
            match(TokenType.MP_LPAREN);
            readParameter();
            readParameterTail();
            match(TokenType.MP_RPAREN);
            break;
        default:
            syntaxError("Read");
        }
    }

    public void readParameterTail()
    {
        debug();
        switch (lookAhead.getType())
        {
        case MP_READ: //42 ReadParameterTail -> mp_comma ReadParameter ReadParameterTail
            out.println("42");
            match(TokenType.MP_COMMA);
            readParameter();
            readParameterTail();
            break;
        case MP_RPAREN: //43 ReadParameterTail -> &epsilon
            out.println("43");
            lambda();
            break;
        default:
            syntaxError("Read, )");
        }
    }

    public void readParameter()
    {
        debug();
        switch (lookAhead.getType())
        {
        case MP_IDENTIFIER: //44 ReadParameter -> VariableIdentifier
            out.println("44");
            variableIdentifier();
            break;
        default:
            syntaxError("identifier");
        }
    }

    public void writeStatement()
    {
        debug();
        switch (lookAhead.getType())
        {
        case MP_WRITE: //45 WriteStatement -> mp_write mp_lparen WriteParameter WriteParameterTail mp_rparen
            out.println("45");
            match(TokenType.MP_WRITE);
            match(TokenType.MP_LPAREN);
            writeParameter();
            writeParameterTail();
            match(TokenType.MP_RPAREN);
            break;
        default:
            syntaxError("Write");
        }
    }

    public void writeParameterTail()
    {
        debug();
        switch (lookAhead.getType())
        {
        case MP_COMMA: //46 WriteParameterTail -> mp_comma WriteParameter
            out.println("46");
            match(TokenType.MP_COMMA);
            writeParameter();
            break;
        case MP_RPAREN: //47 WriteParameterTail -> &epsilon
            out.println("47");
            lambda();
            break;
        default:
            syntaxError("',', )");
        }
    }

    public void writeParameter()
    {
        debug();
        switch (lookAhead.getType())
        {
        case MP_IDENTIFIER: //48 WriteParameter -> OrdinalExpression
        case MP_LPAREN:
        case MP_NOT:
        case MP_INTEGER_LIT:
        case MP_MINUS:
        case MP_PLUS:
            out.println("48");
            ordinalExpression();
            break;
        default:
            syntaxError("identifier, (, not, Integer, -, +");
        }
    }

    public void assignmentStatement()
    {
        debug();
        switch (lookAhead.getType())
        {
        case MP_IDENTIFIER: { //49 AssignmentStatement -> VariableIdentifier mp_assign Expression //TODO:Fix Ambiguity
            out.println("49");
            variableIdentifier();
            match(TokenType.MP_ASSIGN);
            expression();
        }
            //        { //50 AssignmentStatement -> FunctionIdentifier mp_assign Expression //TODO:Fix Ambiguity
            //            out.println("50");
            //            functionIdentifier();
            //            match(TokenType.MP_ASSIGN);
            //            expression();
            //        }
            break;
        default:
            syntaxError("identifier");
        }
    }

    //Now starting at Rule line 103
    public void functionIdentifier()
    {
        debug();
        switch (lookAhead.getType())
        {
        case MP_IDENTIFIER: //101 FunctionIdentifier -> mp_identifier
            out.println("101");
            match(TokenType.MP_IDENTIFIER);
            break;
        default:
            syntaxError("identifier");
        }
    }

    public void booleanExpression()
    {
        debug();
        switch (lookAhead.getType())
        {
        case MP_IDENTIFIER: //102 BooleanExpression ->  Expression
        case MP_LPAREN:
        case MP_NOT:
        case MP_INTEGER_LIT:
        case MP_MINUS:
        case MP_PLUS:
            out.println("102");
            expression();
            break;
        default:
            syntaxError("identifier, (, not, Integer, -, +");
        }
    }

    public void ordinalExpression()
    {
        debug();
        switch (lookAhead.getType())
        {
        case MP_IDENTIFIER: //103 OrdinalExpression ->  Expression
        case MP_LPAREN:
        case MP_NOT:
        case MP_INTEGER_LIT:
        case MP_MINUS:
        case MP_PLUS:
            out.println("103");
            expression();
            break;
        default:
            syntaxError("identifier, (, not, Integer, -, +");
        }
    }

    public void identifierList()
    {
        debug();
        switch (lookAhead.getType())
        {
        case MP_IDENTIFIER: //104 IdentifierList -> mp_identifier IdentifierTail
            out.println("104");
            match(TokenType.MP_IDENTIFIER);
            identifierTail();
            break;
        default:
            syntaxError("identifier");
        }
    }

    public void identifierTail()
    {
        debug();
        switch (lookAhead.getType())
        {
        case MP_COMMA: //105 IdentifierTail -> mp_comma mp_identifier IdentifierTail
            out.println("105");
            match(TokenType.MP_COMMA);
            match(TokenType.MP_IDENTIFIER);
            identifierTail();
            break;
        case MP_COLON: //106 IdentifierTail -> &epsilon
            out.println("106");
            lambda();
            break;
        default:
            syntaxError("',', :");
        }
    }
}

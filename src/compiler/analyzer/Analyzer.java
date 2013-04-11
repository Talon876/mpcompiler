package compiler.analyzer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Stack;

import compiler.TokenType;
import compiler.parser.Parser;
import compiler.parser.SemanticRec;
import compiler.parser.symbol.Classification;
import compiler.parser.symbol.DataRow;
import compiler.parser.symbol.Row;
import compiler.parser.symbol.SymbolTable;
import compiler.parser.symbol.Type;
import compiler.parser.RecordType;

public class Analyzer {
    Stack<SymbolTable> symboltables;
    PrintWriter out;

    public Analyzer(Stack<SymbolTable> tables, File output) throws IOException
    {
        symboltables = tables;
        out = new PrintWriter(new FileWriter(output), true);
    }

    /**
     * 
     * @param sr
     *            {@link RecordType#IDENTIFIER}
     * @return
     */
    private Row getIdRowFromSR(SemanticRec sr)
    {
        String lex = sr.getDatum(1);
        Classification classify = Classification.valueOf(sr.getDatum(0));
        Row r = findSymbol(lex, classify);
        if (r == null)
        {
            Parser.semanticError("Identifier : type " + classify.toString() + " lexeme " + lex
                    + " not declared in current scope");
        }
        return r;
    }

    /**
     * 
     * @param sr
     *            with {@link RecordType#LITERAL} or {@link RecordType#IDENTIFIER}
     * @return
     */
    private Type getTypeFromSR(SemanticRec sr)
    {
        Type t = null;
        if (sr.getRecType() == RecordType.IDENTIFIER)
        {
            Row r = getIdRowFromSR(sr);
            t = r.getType();
        }
        else if (sr.getRecType() == RecordType.LITERAL) //Literal
        {
            t = Type.valueOf(sr.getDatum(0));
        }
        else //TODO: add Function rows here
        {
            Parser.semanticError("RecordType" + sr.getRecType() + " does not have a Type");
        }
        return t;
    }

    private String getRegisterFromNL(String nestLvl) {
        return "D" + nestLvl;
    }

    private String generateOffset(SymbolTable tbl, DataRow data) {
        String nesting = "" + tbl.getNestingLevel();
        String memOffset = "" + data.getMemOffset();
        nesting = getRegisterFromNL(nesting);
        return memOffset + "(" + nesting + ")";
    }

    /*
     * VM ASM methods section begin
     */

    /**
     * Pushes the value at the memory location to the stack
     * 
     * @param memLoc
     *            string with format from VM definition e.g (0(D0), #"string", etc)
     */
    private void push(String memLoc)
    {
        out.println("push " + memLoc);
    }

    /**
     * Pops the value from the stack into the memory location
     * 
     * @param memLoc
     *            string with format from VM definition e.g (0(D0), #"string", etc)
     */
    private void pop(String memLoc)
    {
        out.println("pop " + memLoc);
    }

    /**
     * 
     * @param src
     * @param dst
     */
    private void move(String src, String dst)
    {
        out.println("mov " + src + " " + dst);
    }

    /**
     * 
     * @param src1
     * @param src2
     * @param dst
     */
    private void add(String src1, String src2, String dst)
    {
        out.println("add " + src1 + " " + src2 + " " + dst);
    }

    /**
     * 
     * @param src1
     * @param src2
     * @param dst
     */
    private void sub(String src1, String src2, String dst) {
        out.println("sub " + src1 + " " + src2 + " " + dst);
    }

    /**
     * 
     */
    private void not()
    {
        out.println("nots");
    }

    /**
     * 
     */
    private void and()
    {
        out.println("ands");
    }

    /**
     * 
     */
    private void or()
    {
        out.println("ors");
    }

    private void mulStackI()
    {
        out.println("muls");
    }

    private void divStackI()
    {
        out.println("divs");
    }

    private void modStackI()
    {
        out.println("mods");
    }

    private void mulStackF()
    {
        out.println("mulsf");
    }

    private void divStackF()
    {
        out.println("divsf");
    }

    private void modStackF()
    {
        out.println("modsf");
    }

    private void subStackI()
    {
        out.println("subs");
    }

    private void addStackI()
    {
        out.println("adds");
    }

    private void subStackF()
    {
        out.println("subsf");
    }

    private void addStackF()
    {
        out.println("addsf");
    }

    private void negateStackI()
    {
        out.println("negs");
    }

    private void negateStackF()
    {
        out.println("negsf");
    }

    private void notEqualI()
    {
        out.println("cmpnes");
    }

    private void greaterEqualI()
    {
        out.println("cmpges");
    }

    private void lessEqualI()
    {
        out.println("cmples");
    }

    private void greaterThanI()
    {
        out.println("cmpgts");
    }

    private void lessThanI()
    {
        out.println("cmplts");
    }

    private void equalI()
    {
        out.println("cmpeqs");
    }

    private void notEqualF()
    {
        out.println("cmpnesf");
    }

    private void greaterEqualF()
    {
        out.println("cmpgesf");
    }

    private void lessEqualF()
    {
        out.println("cmplesf");
    }

    private void greaterThanF()
    {
        out.println("cmpgtsf");
    }

    private void lessThanF()
    {
        out.println("cmpltsf");
    }

    private void equalF()
    {
        out.println("cmpeqsf");
    }

    private void castStackToI()
    {
        out.println("castsi");
    }

    private void castStackToF()
    {
        out.println("castsf");
    }

    private void writeStack()
    {
        out.println("wrts");
    }
    
    private void writelnStack()
    {
        out.println("wrtlns");
    }
    
    private void readI(String offset)
    {
        out.println("rd " + offset);
    }
    
    private void readF(String offset)
    {
        out.println("rdf " + offset);
    }
    
    private void readS(String offset)
    {
        out.println("rds " + offset);
    }
    /**
     * Halts program execution
     */
    private void halt()
    {
        out.println("hlt");
    }

    /**
     * Prints a comment to the VM code (for human readability)
     * 
     * @param comment
     */
    private void comment(String comment)
    {
        out.println("; " + comment);
    }

    /*
     * VM ASM methods section end
     */

    /**
     * 
     * @param name_rec
     *            {@link SemanticRec} from {@link compiler.parser.Parser#program()} with {@link RecordType#SYM_TBL}
     */
    public void gen_activation_rec(SemanticRec name_rec) {
        comment(name_rec.getDatum(0) + " start"); //; Program1 start
        String register = getRegisterFromNL(name_rec.getDatum(1));
        push(register); //push D0
        String dataSize = name_rec.getDatum(2);
        move("SP", register); //store stack pointer into nesting register
        add("SP", "#" + dataSize, "SP"); //reserve needed space
    }

    /**
     * 
     * @param name_rec
     *            {@link SemanticRec} from {@link compiler.parser.Parser#program()} with {@link RecordType#SYM_TBL}
     */
    public void gen_deactivation_rec(SemanticRec name_rec)
    {

        String dataSize = name_rec.getDatum(2);
        sub("SP", "#" + dataSize, "SP");
        String register = getRegisterFromNL(name_rec.getDatum(1));
        pop(register);
        comment(name_rec.getDatum(0) + " end"); //; Program1 end
    }

    /**
     * 
     */
    public void gen_halt()
    {
        halt();
    }

    /**
     * 
     * @param factor
     *            {@link SemanticRec} from {@link compiler.parser.Parser#factor()} with {@link RecordType#IDENTIFIER}
     */
    public SemanticRec gen_push_id(SemanticRec factor)
    {
        DataRow data = (DataRow) getIdRowFromSR(factor); //variableIdentifier is either parameter or variable
        SymbolTable tbl = findSymbolTable(data);
        String offset = generateOffset(tbl, data);
        comment("push class: " + data.getClassification() + " lexeme: " + data.getLexeme() + " type: " + data.getType()
                + " offset: " + offset);
        push(offset);
        return new SemanticRec(RecordType.LITERAL, data.getType().toString());
    }

    /**
     * 
     * @param lit
     *            {@link SemanticRec} from {@link compiler.parser.Parser#factor()} with {@link RecordType#LITERAL}
     */
    public void gen_push_lit(SemanticRec lit, String lexeme)
    {
        String type = lit.getDatum(0);
        Type tt = Type.valueOf(type);
        comment("push lexeme: " + lexeme + " type: " + type);
        switch (tt)
        {
        case STRING:
            push("#\"" + lexeme + "\"");
            break;
        case BOOLEAN:
            if(lexeme.equalsIgnoreCase("true"))
            {
                push("#1");
            }
            else
            {
                push("#0");
            }
            break;
        case INTEGER:
            push("#" + lexeme);
            break;
        case FLOAT:
            if (lexeme.toLowerCase().contains("e") && !lexeme.contains(".")) //Workaround to convert from micropascal float to C scanf float
            {
                /*
                 * If the float has an "e" in it but does not have a "." add a ".0" before the "e"
                 * In the current micropascal definition if you have an "e" and a "." it must have a digit after the "." which would be legal in C also
                 */
                int split = lexeme.toLowerCase().indexOf("e");
                String start = lexeme.substring(0, split);
                String end = lexeme.substring(split);
                lexeme = start + ".0" + end;
            }
            push("#" + lexeme);
            break;
        default:
            Parser.semanticError(lexeme + " of type " + tt.toString() + " cannot be pushed onto the stack");

        }
    }

    /**
     * 
     * @param factor
     *            {@link SemanticRec} from {@link compiler.parser.Parser#factor()} with {@link RecordType#LITERAL} or
     *            {@link RecordType#IDENTIFIER}
     */
    public void gen_not_bool(SemanticRec factor)
    {
        Type factorType = getTypeFromSR(factor);
        switch (factorType)
        {
        case BOOLEAN:
            not();
            break;
        default:
            Parser.semanticError("Not operator expected BOOLEAN found " + factorType.toString());
        }
    }

    /**
     * 
     * @param opSign
     *            {@link SemanticRec} from {@link compiler.parser.Parser#optionalSign()} with {@link RecordType#OPT_SIGN}
     * @param term
     *            {@link SemanticRec} from {@link compiler.parser.Parser#term()} with {@link RecordType#LITERAL} or
     *            {@link RecordType#IDENTIFIER}
     */
    public void gen_opt_sim_negate(SemanticRec opSign, SemanticRec term)
    {
        if (opSign != null && term != null)
        {
            Type termType = getTypeFromSR(term);

            String op = opSign.getDatum(0);
            switch (termType)
            {
            case INTEGER:
                switch (op)
                {
                case "MP_MINUS":
                    negateStackI();
                    break;
                case "MP_PLUS":
                    break;
                default:
                    Parser.semanticError(opSign + " is not a negation operator for type " + termType);
                }
                break;
            case FLOAT:
                switch (op)
                {
                case "MP_MINUS":
                    negateStackF();
                    break;
                case "MP_PLUS":
                    break;
                default:
                    Parser.semanticError(opSign + " is not a negation operator for type " + termType);
                }
                break;
            default:
                Parser.semanticError(termType + "does not have a negation operation");
            }
        }
    }

    /**
     * 
     * @param left
     *            {@link SemanticRec} from {@link compiler.parser.Parser#factor()} with {@link RecordType#LITERAL} or
     *            {@link RecordType#IDENTIFIER}
     * @param mulOp
     *            {@link SemanticRec} from {@link compiler.parser.Parser#multiplyingOperator()} with {@link RecordType#REL_OP}
     * @param right
     *            {@link SemanticRec} from {@link compiler.parser.Parser#factor()} with {@link RecordType#LITERAL} or
     *            {@link RecordType#IDENTIFIER}
     * @return {@link RecordType#LITERAL} record from the operation
     */
    public SemanticRec gen_mulOp(SemanticRec left, SemanticRec mulOp, SemanticRec right)
    {

        SemanticRec[] results = gen_cast(left, right); //generates casting operations if needed
        Type resultType = getTypeFromSR(results[0]); //the types are equal at this point
        String op = mulOp.getDatum(0);
        switch (resultType)
        {
        case INTEGER:
            switch (op)
            {
            case "MP_MOD":
                modStackI();
                break;
            case "MP_DIV":
                divStackI();
                break;
            case "MP_TIMES":
                mulStackI();
                break;
            default:
                Parser.semanticError(op + " is not a multiplication operator for type " + resultType);
            }
            break;
        case FLOAT:
            switch (op)
            {
            case "MP_MOD":
                modStackF();
                break;
            case "MP_DIV":
                divStackF();
                break;
            case "MP_TIMES":
                mulStackF();
                break;
            default:
                Parser.semanticError(op + " is not a multiplication operator for type " + resultType);
            }
            break;
        case BOOLEAN:
            switch (op)
            {
            case "MP_AND":
                and();
                break;
            default:
                Parser.semanticError(op + " is not a multiplication operator for type " + resultType);
            }
            break;
        default:
            Parser.semanticError(resultType + "does not have a multiplication operation");
        }
        return new SemanticRec(RecordType.LITERAL, resultType.toString());
    }

    /**
     * 
     * @param left
     *            {@link SemanticRec} from {@link compiler.parser.Parser#term()} with {@link RecordType#LITERAL} or
     *            {@link RecordType#IDENTIFIER}
     * @param mulOp
     *            {@link SemanticRec} from {@link compiler.parser.Parser#addingOperatorOperator()} with {@link RecordType#REL_OP}
     * @param right
     *            {@link SemanticRec} from {@link compiler.parser.Parser#term()} with {@link RecordType#LITERAL} or
     *            {@link RecordType#IDENTIFIER}
     * @return {@link RecordType#LITERAL} record from the operation
     */
    public SemanticRec gen_addOp(SemanticRec left, SemanticRec addOp, SemanticRec right)
    {

        SemanticRec[] results = gen_cast(left, right); //generates casting operations if needed
        Type resultType = getTypeFromSR(results[0]); //both sides should have equal types
        String op = addOp.getDatum(0);
        switch (resultType)
        {
        case INTEGER:
            switch (op)
            {
            case "MP_MINUS":
                subStackI();
                break;
            case "MP_PLUS":
                addStackI();
                break;
            default:
                Parser.semanticError(op + " is not a adding operator for type " + resultType);
            }
            break;
        case FLOAT:
            switch (op)
            {
            case "MP_MINUS":
                subStackF();
                break;
            case "MP_PLUS":
                addStackF();
                break;
            default:
                Parser.semanticError(op + " is not a adding operator for type " + resultType);
            }
            break;
        case BOOLEAN:
            switch (op)
            {
            case "MP_OR":
                or();
                break;
            default:
                Parser.semanticError(op + " is not a adding operator for type " + resultType);
            }
            break;
        default:
            Parser.semanticError(resultType + "does not have an adding operation");
        }
        return new SemanticRec(RecordType.LITERAL, resultType.toString());
    }

    /**
     * 
     * @param id
     *            {@link SemanticRec} from {@link compiler.parser.Parser#variableIdentifier()} with {@link RecordType#LITERAL} or
     *            {@link RecordType#IDENTIFIER}
     * @param exp
     *            {@link SemanticRec} from {@link compiler.parser.Parser#expression()} with {@link RecordType#LITERAL} or
     *            {@link RecordType#IDENTIFIER}
     */
    public void gen_assign(SemanticRec id, SemanticRec exp) {

        SemanticRec result = gen_assign_cast(id, exp);

        Row leftRow = getIdRowFromSR(id);
        if (leftRow.getClassification() == Classification.VARIABLE)
        {
            SymbolTable leftTable = findSymbolTable(leftRow);
            String leftOffset = generateOffset(leftTable, (DataRow) leftRow);
            //rightside is ontop of the stack pop into destination
            pop(leftOffset); //pop into left
        }
        else if (leftRow.getClassification() == Classification.FUNCTION) //TODO:Function stuff here
        {

        }
    }

    /**
     * 
     * @param left
     *            {@link SemanticRec} from {@link compiler.parser.Parser#simpleExpression()} with {@link RecordType#LITERAL} or
     *            {@link RecordType#IDENTIFIER}
     * @param op
     *            {@link SemanticRec} from {@link compiler.parser.Parser#optionalRelationalPart(SemanticRec)} with {@link RecordType#REL_OP}
     * @param right
     *            {@link SemanticRec} from {@link compiler.parser.Parser#simpleExpression()} with {@link RecordType#LITERAL} or
     *            {@link RecordType#IDENTIFIER}
     */
    public void gen_opt_rel_part(SemanticRec left, SemanticRec op, SemanticRec right)
    {
        if (left != null && right != null && op != null)
        {

            SemanticRec[] results = gen_cast(left, right); //generates casting operations if needed
            Type resultType = getTypeFromSR(results[0]); //They will both be equal at this point
            String relOp = op.getDatum(0); //MP_NEQUAL, MP_GEQUAL, MP_LEQUAL, MP_GTHAN, MP_LTHAN, MP_EQUAL
            switch (resultType)
            {
            case INTEGER:
                switch (relOp)
                {
                case "MP_NEQUAL":
                    notEqualI();
                    break;
                case "MP_GEQUAL":
                    greaterEqualI();
                    break;
                case "MP_LEQUAL":
                    lessEqualI();
                    break;
                case "MP_GTHAN":
                    greaterThanI();
                    break;
                case "MP_LTHAN":
                    lessThanI();
                    break;
                case "MP_EQUAL":
                    equalI();
                    break;
                default:
                    Parser.semanticError(relOp + " is not a relational operator for type " + resultType);
                }
                break;
            case FLOAT:
                switch (relOp)
                {
                case "MP_NEQUAL":
                    notEqualF();
                    break;
                case "MP_GEQUAL":
                    greaterEqualF();
                    break;
                case "MP_LEQUAL":
                    lessEqualF();
                    break;
                case "MP_GTHAN":
                    greaterThanF();
                    break;
                case "MP_LTHAN":
                    lessThanF();
                    break;
                case "MP_EQUAL":
                    equalF();
                    break;
                default:
                    Parser.semanticError(relOp + " is not a relational operator for type " + resultType);
                }
            default:
                Parser.semanticError(resultType + " does not have relational operators");
            }
        }
    }

    /**
     * Casts Ids or Literals to the less restrictive type to preserve precision Int, Float the Int is cast to a Float
     * 
     * @param left
     *            {@link RecordType#LITERAL} or {@link RecordType#IDENTIFIER}
     * @param right
     *            {@link RecordType#LITERAL} or {@link RecordType#IDENTIFIER}
     * @return resulting SemanticRec idx 0 is left idx 1 is right
     */
    public SemanticRec[] gen_cast(SemanticRec left, SemanticRec right)
    {
        Type leftType = getTypeFromSR(left);
        Type rightType = getTypeFromSR(right);

        SemanticRec[] arrRec = new SemanticRec[2];
        if (leftType == rightType)
        {
            //no cast needed
            arrRec[0] = left;
            arrRec[1] = right;
        }
        else if ((leftType == Type.INTEGER) && rightType == Type.FLOAT)
        {
            //cast left to float
            /*
             * The following cast works because of the way the stack in the VM is initialized
             * when the stack pointer is moved back the value that use to be on top is not lost just not currently in "scope"
             * once the cast happens on the new "top" the pointer is moved back and it was like nothing happened to the value
             */
            comment("start cast left to float");
            sub("SP", "#1", "SP"); //move to the left var on the stack
            castStackToF();
            add("SP", "#1", "SP"); //move back to original place
            comment("end cast left to float");
            arrRec[0] = new SemanticRec(RecordType.LITERAL, Type.FLOAT.toString());
            arrRec[1] = right;
        }
        else if (leftType == Type.FLOAT && (rightType == Type.INTEGER))
        {
            //cast right to float
            castStackToF(); //top value
            arrRec[0] = left;
            arrRec[1] = new SemanticRec(RecordType.LITERAL, Type.FLOAT.toString());
        }
        else
        {
            //invalid cast
            Parser.semanticError("Invalid cast from " + leftType + " to " + rightType);
            return null;
        }
        return arrRec;
    }

    /**
     * 
     * @param leftType
     *            {@link RecordType#IDENTIFIER}
     * @param rightType
     *            {@link RecordType#LITERAL} or {@link RecordType#IDENTIFIER}
     * @return expression's resulting SemanticRec if it is a {@link RecordType#LITERAL}
     */
    public SemanticRec gen_assign_cast(SemanticRec left, SemanticRec right)
    {
        Type leftType = getTypeFromSR(left);
        Type rightType = getTypeFromSR(right);

        SemanticRec returnRec = null;
        if (leftType == rightType)
        {
            //no cast needed
            returnRec = right; //the expressions RecordType is unchanged
        }
        else if ((leftType == Type.INTEGER) && rightType == Type.FLOAT)
        {
            //cast to integer
            castStackToI();
            returnRec = new SemanticRec(RecordType.LITERAL, Type.INTEGER.toString()); //now an integer on the stack
        }
        else if (leftType == Type.FLOAT && (rightType == Type.INTEGER))
        {
            //cast to float
            castStackToF();
            returnRec = new SemanticRec(RecordType.LITERAL, Type.FLOAT.toString()); //now a float on the stack
        }
        else
        {
            //invalid cast
            Parser.semanticError("Invalid cast from " + leftType + " to " + rightType);
        }

        return returnRec;
    }

    public void gen_writeStmt(SemanticRec writeStmt, SemanticRec exp)
    {
        String writeType = writeStmt.getDatum(0);
        if(writeType.equalsIgnoreCase(TokenType.MP_WRITE.toString()))
        {
            writeStack();
        }
        else
        {
            writelnStack();
        }
    }
    
    public void gen_readStmt(SemanticRec readStmt)
    {
        DataRow row = (DataRow) getIdRowFromSR(readStmt);
        Type type = row.getType();
        SymbolTable table = findSymbolTable(row);
        String offset = generateOffset(table, row);
        
        switch(type)
        {
        case INTEGER:
            readI(offset);
            break;
        case FLOAT:
            readF(offset);
            break;
        case STRING:
            readS(offset);
            break;
        default:
            Parser.semanticError("Cannot read from console into variable of type: " + type.toString());
        }
        
    }
    
    public Row findSymbol(String lexeme) {
        for (int i = symboltables.size() - 1; i >= 0; i--) {
            SymbolTable st = symboltables.get(i);
            Row s = st.findSymbol(lexeme);
            if (s != null) {
                return s;
            }
        }
        return null;
    }

    public Row findSymbol(String lexeme, Classification c) {
        for (int i = symboltables.size() - 1; i >= 0; i--) {
            SymbolTable st = symboltables.get(i);
            Row s = st.findSymbol(lexeme, c);
            if (s != null) {
                return s;
            }
        }
        return null;
    }

    public SymbolTable findSymbolTable(Row symbol)
    {
        for (int i = symboltables.size() - 1; i >= 0; i--) {
            SymbolTable st = symboltables.get(i);
            boolean s = st.contains(symbol);
            if (s == true) {
                return st;
            }
        }
        return null;
    }
}

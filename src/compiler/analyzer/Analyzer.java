package compiler.analyzer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
    public void gen_push_id(SemanticRec factor)
    {
        DataRow data = (DataRow) getIdRowFromSR(factor); //variableIdentifier is either parameter or variable
        SymbolTable tbl = findSymbolTable(data);
        String offset = generateOffset(tbl, data);
        comment("push class: " + data.getClassification() + " lexeme: " + data.getLexeme() + " type: " + data.getType()
                + " offset: " + offset);
        push(offset);
    }

    /**
     * 
     * @param lit
     *            {@link SemanticRec} from {@link compiler.parser.Parser#factor()} with {@link RecordType#LITERAL}
     */
    public void gen_push_lit(SemanticRec lit, String lexeme)
    {
        String type = lit.getDatum(0);
        TokenType tt = TokenType.valueOf(type);
        comment("push lexeme: " + lexeme + " type: " + type);
        switch (tt)
        {
        case MP_STRING_LIT:
            push("#\"" + lexeme + "\"");
            break;
        case MP_TRUE:
            push("#1");
            break;
        case MP_FALSE:
            push("#0");
            break;
        case MP_INTEGER_LIT:
            push("#" + lexeme);
            break;
        case MP_FLOAT_LIT:
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
        switch(factorType)
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
        Type leftType = getTypeFromSR(left);
        Type rightType = getTypeFromSR(right);

        Type resultType = gen_cast(leftType, rightType); //generates casting operations if needed
        String op = mulOp.getDatum(0);
        switch(resultType)
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
                Parser.semanticError(mulOp + " is not a relational operator for type " + resultType);
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
                Parser.semanticError(mulOp + " is not a relational operator for type " + resultType);
            }
            break;
        case BOOLEAN:
            switch (op)
            {
            case "MP_AND":
                and();
                break;
            default:
                Parser.semanticError(mulOp + " is not a relational operator for type " + resultType);
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
    public SemanticRec gen_addOp(SemanticRec left, SemanticRec mulOp, SemanticRec right)
    {
        Type leftType = getTypeFromSR(left);
        Type rightType = getTypeFromSR(right);

        Type resultType = gen_cast(leftType, rightType); //generates casting operations if needed
        String op = mulOp.getDatum(0);
        switch(resultType)
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
                Parser.semanticError(mulOp + " is not a relational operator for type " + resultType);
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
                Parser.semanticError(mulOp + " is not a relational operator for type " + resultType);
            }
            break;
        case BOOLEAN:
            switch (op)
            {
            case "MP_OR":
                or();
                break;
            default:
                Parser.semanticError(mulOp + " is not a relational operator for type " + resultType);
            }
            break;
        default:
            Parser.semanticError(resultType + "does not have a multiplication operation");  
        }
        return new SemanticRec(RecordType.LITERAL, resultType.toString());
    }
    
    /**
     * 
     */
    public void gen_assign() {

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
        Type leftType = getTypeFromSR(left);
        Type rightType = getTypeFromSR(right);

        Type resultType = gen_cast(leftType, rightType); //generates casting operations if needed
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

    /**
     * 
     * @param leftType
     *            {@link RecordType#LITERAL} or {@link RecordType#IDENTIFIER}
     * @param rightType
     *            {@link RecordType#LITERAL} or {@link RecordType#IDENTIFIER}
     * @return resulting Type
     */
    public Type gen_cast(Type leftType, Type rightType)
    {
        if (leftType == rightType)
        {
            //no cast needed
            return leftType;
        }
        else if ((leftType == Type.INTEGER) && rightType == Type.FLOAT)
        {
            //cast to integer
            return leftType;
        }
        else if (leftType == Type.FLOAT && (rightType == Type.INTEGER))
        {
            //cast to float
            return leftType;
        }
        else
        {
            //invalid cast
            Parser.semanticError("Invalid cast from " + leftType + " to " + rightType);
            return null;
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

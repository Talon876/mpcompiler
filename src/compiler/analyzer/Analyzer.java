package compiler.analyzer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Stack;

import compiler.parser.Parser;
import compiler.parser.SemanticRec;
import compiler.parser.symbol.Classification;
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
        return t;
    }

    private String getRegisterFromNL(String nestLvl) {
        return "D" + nestLvl;
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
        switch (relOp)
        {
        case "MP_NEQUAL":
            out.print("CMPNES");
            break;
        case "MP_GEQUAL":
            out.print("CMPGES");
            break;
        case "MP_LEQUAL":
            out.print("CMPLES");
            break;
        case "MP_GTHAN":
            out.print("CMPGTS");
            break;
        case "MP_LTHAN":
            out.print("CMPLTS");
            break;
        case "MP_EQUAL":
            out.print("CMPEQS");
            break;
        }
        if (resultType == Type.FLOAT)
        {
            out.print("F"); //Add F to the line for Float/Fixed operation
        }
        out.println();

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

}

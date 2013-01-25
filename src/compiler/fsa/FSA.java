package compiler.fsa;

import compiler.Token;
import compiler.io.MPFile;

public interface FSA {

    public Token getToken(MPFile file);
}

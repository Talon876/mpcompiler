package compiler.parser.symbol;

public class Attribute {
    Type type;
    Mode mode;

    public Attribute(Type type, Mode mode)
    {
        this.type = type;
        this.mode = mode;
    }
}

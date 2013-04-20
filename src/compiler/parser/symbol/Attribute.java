package compiler.parser.symbol;

public class Attribute implements Printable {
    private Type type;
    private Mode mode;

    public Attribute(Type type, Mode mode)
    {
        this.type = type;
        this.mode = mode;
    }

    public Type getType() {
        return type;
    }

    public Mode getMode() {
        return mode;
    }

    @Override
    public void print() {
        System.out.print("(Type: " + type.toString() + ", Mode: " + mode.toString() + ")");
    }
}

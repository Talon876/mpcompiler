package compiler.parser.symbol;

public class Attribute implements Printable {
    Type type;
    Mode mode;

    public Attribute(Type type, Mode mode)
    {
        this.type = type;
        this.mode = mode;
    }

    @Override
    public void print() {
        System.out.print("(" + type.toString() + ", " + mode.toString() + ")");
    }
}

package compiler;

import java.io.IOException;

import compiler.parser.Parser;

public class MP {

    public static void main(String[] args) throws IOException {

        if (args.length < 1) {
            System.out.println("Usage: java MP <filename.up>");

        } else {
            String filename = args[0];

            if (filename.endsWith(".up") || filename.endsWith(".pas")) {
                System.out.println("Compiling: " + filename);
            } else {
                System.out.println("Warning: Expected filetype to be .up or .pas, attempting to compile anyway.");
            }
            Scanner s = new Scanner();
            s.openFile(filename);
            Parser p = new Parser(s, filename + ".vm");
        }
    }
}

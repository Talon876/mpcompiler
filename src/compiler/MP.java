package compiler;

import java.io.IOException;

import compiler.parser.Parser;

public class MP {

    public static void main(String[] args) throws IOException {

        if (args.length < 1) {
            System.out.println("Usage: java MP <filename.up>");

        } else {
            String filename = args[0];

            if (filename.endsWith(".up")) {
                System.out.println("Compiling: " + filename);

                Scanner s = new Scanner();
                s.openFile(filename);

                Parser p = new Parser(s, filename + ".vm");

                //                ArrayList<Token> tokens = s.getTokens();
                //                //print tokens to token file
                //                for (Token t : tokens) {
                //
                //                    System.out.println(t.toString());
                //
                //                }

            } else {
                System.out.println("Usage: java MP <filename.up>");
            }
        }

    }

}

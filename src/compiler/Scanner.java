package compiler;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import compiler.io.Line;
import compiler.io.MPFile;

public class Scanner {
    private MPFile file;

    /**
     * Opens the micropascal file
     * 
     * @param filename
     */
    public void openFile(String filename) {
        try {
            file = new MPFile();
            BufferedReader br = new BufferedReader(new FileReader(filename));
            String line = "";
            while ((line = br.readLine()) != null) {
                file.addLine(new Line(line));
            }

            file.print(true);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Is the dispatcher to a specialized FSA implementation
     * 
     * @return the next token in the file
     */
    public Token getNextToken() {
        return null;
    }
}

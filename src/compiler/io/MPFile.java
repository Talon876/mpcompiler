package compiler.io;

import java.util.HashMap;

/**
 * Represents the micropascal file by containing a mapping from line number to the actual line
 */
public class MPFile {

    private HashMap<Integer, Line> lines = new HashMap<Integer, Line>();

    /**
     * Adds a line to the file and maps it to the next line number
     * 
     * @param line
     */
    public void addLine(Line line) {
        lines.put(lines.size() + 1, line);
    }

    public Line getLine(int number) {
        return lines.get(number);
    }
}

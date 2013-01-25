package compiler.io;

import java.util.HashMap;

/**
 * Represents the micropascal file by containing a mapping from line number to the actual line
 */
public class MPFile {

    private HashMap<Integer, Line> lines = new HashMap<Integer, Line>();

    private int lineIndex = 1; // 1-based

    /**
     * Adds a line to the file and maps it to the next line number
     * 
     * @param line
     */
    public void addLine(Line line) {
        lines.put(lines.size() + 1, line);
    }

    public Line getLineAt(int number) {
        return lines.get(number);
    }

    public char getCurrentCharacter() {
        return getCurrentLine().getCurrentCharacter();
    }

    //    /**
    //     * Finds a useful character by skipping whitespace but not new lines
    //     *
    //     * @return current character if its useful, else the next useful character
    //     */
    //    public char getUsefulCharacter() {
    //        char usefulChar = 0;
    //
    //        if (!WhiteSpace.isWhitespace(getCurrentCharacter())) { //if not whitespace, return self
    //            usefulChar = getCurrentCharacter();
    //        } else { //current character is whitespace (not useful), find next useful character
    //            while (WhiteSpace.isWhitespace(getCurrentCharacter())) {
    //                //getCurrentCharacter()
    //            }
    //        }
    //
    //        return usefulChar;
    //    }

    public boolean hasNextChar() {
        //        boolean lastline = lineIndex > lines.size();
        //        System.out.println("lineIndex: " + lineIndex + " > " + lines.size() + " = " + lastline);
        //        if (lastline) {
        //            return false;
        //        }
        //        if (getCurrentLine() == null) {
        //            System.out.println("current line is null");
        //        }
        return getCurrentLine().hasNextChar();
    }


    public boolean hasNextLine() {
        return lineIndex < lines.size();
    }

    public char getNextCharacter() {
        return getCurrentLine().getNextChar();
    }

    public void increaseLineIndex() {
        lineIndex++;
    }

    /**
     * 
     * @return true if this is the last line; false otherwise
     */
    public boolean isOnLastLine() {
        return lineIndex >= lines.size();
    }

    /**
     * Prints the file
     * 
     * @param lineNums
     *            whether or not to include line numbers
     */
    public void print(boolean lineNums) {
        for (int i = 1; i <= lines.size(); i++) {
            if (lineNums) {
                System.out.println(i + ": " + lines.get(i).getText());
            } else {
                System.out.println(lines.get(i).getText());
            }

        }
    }

    /**
     * 
     * @return the line at the current line index
     */
    public Line getCurrentLine() {
        return lines.get(lineIndex);
    }

    public int getColumnIndex() {
        return getCurrentLine().getColumnIndex();
    }

    public int getLineIndex() {
        return lineIndex;
    }

    public int numberOfLines() {
        return lines.size();
    }
}

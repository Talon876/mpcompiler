package compiler.io;

/**
 * A line in the micropascal file
 */
public class Line {
    private char[] chars;
    int columnIndex = 1;

    /**
     * @param fullLine
     *            the entire line from the file (without the newline character at the end)
     */
    public Line(String fullLine) {
        chars = fullLine.toCharArray();
    }

    /**
     * Gets character at 0 based index
     * 
     * @param idx
     *            the 0 based index to obtain
     * @return the character at the index
     */
    public char getCharAt(int idx) {
        return chars[idx];
    }

    /**
     * 
     * @return the current character
     */
    public char getCurrentCharacter() {
        return getCharAt(columnIndex - 1);
    }

    /**
     * 
     * @return the full line as a string without a newline character
     */
    public String getText() {
        return new String(chars);
    }

    /**
     * 
     * @return length of the line
     */
    public int getLength() {
        return chars.length;
    }

    public int getColumnIndex() {
        return columnIndex;
    }

    public void setColumnIndex(int newIndex) {
        columnIndex = newIndex;
    }

    public boolean isEmpty() {
        return chars.length == 0;
    }

    public boolean hasNextChar() {
        return columnIndex <= chars.length;
    }

    public char getNextChar() {
        char next = getCurrentCharacter();
        columnIndex++;
        return next;
    }

    public void printLineWithCaret(int lineNum, int caretIndex) {
        System.out.println("\t" + lineNum + ": " + getText());
        System.out.print("\t"); //offset tab
        for (int i = 0; i < ("" + lineNum).length(); i++) {
            System.out.print(" "); //offset by line number length
        }
        System.out.print("  "); //offset colon and space
        for (int i = 0; i < caretIndex - 1; i++) {
            System.out.print(" "); //add spaces up to the caretIndex - 1
        }
        System.out.println("^");
    }

    @Override
    public String toString() {
        return getText();
    }
}

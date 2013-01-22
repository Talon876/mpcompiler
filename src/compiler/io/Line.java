package compiler.io;

/**
 * A line in the micropascal file
 */
public class Line {
	private char[] chars;

	/**
	 * @param fullLine
	 *            the entire line from the file (without the newline character
	 *            at the end)
	 */
	public Line(String fullLine) {
		chars = fullLine.toCharArray();
	}

	/**
	 * Gets the index of the next character in the line from the current index.
	 * 
	 * @param curIdx
	 *            starting index
	 * @param skipWhiteSpace
	 *            if true ignores whitespace while determining the next
	 *            character index
	 * @return index of the next character
	 * @throws NoCharException
	 *             if the line does not have any characters left matching the
	 *             parameters
	 */
	public int getNextCharIdx(int curIdx, boolean skipWhiteSpace)
			throws NoCharException {
		return -1;
	}

	public char getChar(int idx) {
		return chars[idx];
	}
}

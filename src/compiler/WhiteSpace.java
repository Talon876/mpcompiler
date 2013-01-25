package compiler;

public class WhiteSpace {

    /**
     * Checks if a character is a whitespace character (space or tab)
     * 
     * @param char_Binks
     *            the annoying character to check
     * @return true if whitespace, false otherwise
     */
    public static boolean isWhitespace(char char_Binks) {
        return char_Binks == ' ' || char_Binks == '\t';
    }
}

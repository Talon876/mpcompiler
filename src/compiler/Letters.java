package compiler;

public class Letters {

    public static final String letters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String digits = "0123456789";

    public static boolean isLetter(char c) {
        return letters.contains("" + c);
    }

    public static boolean isDigit(char c) {
        return digits.contains("" + c);
    }

    public static boolean isLetterOrDigit(char c) {
        return isLetter(c) || isDigit(c);
    }
}

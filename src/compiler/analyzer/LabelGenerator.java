package compiler.analyzer;

public class LabelGenerator {

    private LabelGenerator() {
    }; //cannot instantiate class

    private static int labelCounter = 1;

    /**
     * Generates the next label
     * 
     * @return the next label in the format Ln
     */
    public static String getNextLabel() {
        String label = "L" + labelCounter;
        labelCounter++;
        return label;
    }

    /**
     * Generates a specific label
     * 
     * @param c
     *            the label to generate
     * @return the label with the format Lc
     */
    public static String getLabel(int c) {
        return "L" + c;
    }
}

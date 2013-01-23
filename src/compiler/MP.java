package compiler;

public class MP {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java MP <filename.mp>");

        } else {
            String filename = args[0];

            if (filename.endsWith(".pas")) {
                System.out.println("Compiling: " + filename);
                Scanner s = new Scanner();
                s.openFile(filename);

            } else {
                System.out.println("Usage: java MP <filename.pas>");
            }
        }

    }

}

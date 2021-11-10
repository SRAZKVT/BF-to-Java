import java.io.*;
import java.util.Scanner;

public class BFtoJava {
    public static void main(String[] args) {
        try {
            Scanner reader = new Scanner(new File("input.bf"));
            FileWriter writer = new FileWriter("output.java");
            compile(reader, writer);
            writer.close();
        } catch (FileNotFoundException e) {
            System.out.println("Please input a file named 'input.bf'");
        } catch (IOException e) {
            System.out.println("Error while initializing the writer");
        }
    }

    private static void compile(Scanner reader, FileWriter writer) throws IOException {
        writer.write(
                """
                        import java.util.Scanner;
                        
                        public class output {
                            private static int ptr = 0;
                            private static int[] memArray = new int[65536];
                            private static Scanner scanner = new Scanner(System.in);
                            
                            private static void ptrMoveLeft() {
                                if (ptr != 0) ptr--;
                                else ptr = memArray.length;
                            }
                            
                            private static void ptrMoveRight() {
                                if (ptr != memArray.length) ptr++;
                                else ptr = 0;
                            }
                            
                            private static void incMemCell() {
                                if (memArray[ptr] != 255) memArray[ptr]++;
                                else memArray[ptr] = 0;
                            }
                            
                            private static void decMemCell() {
                                if (memArray[ptr] != 0) memArray[ptr]--;
                                else memArray[ptr] = 255;
                            }
                            
                            public static void main(String[] args) {
                        """
        );
        writer.write('\n');
        writer.flush();
        int indent = 2;
        while (reader.hasNextLine()) {
            String line = reader.next();
            char[] array = line.toCharArray();
            for (char c : array) {
                for (int i = 0; i < indent; i++) {
                    writer.write('\t');
                }
                switch (c) {
                    case '<' -> writer.write("ptrMoveLeft();");
                    case '>' -> writer.write("ptrMoveRight();");
                    case '+' -> writer.write("incMemCell();");
                    case '-' -> writer.write("decMemCell();");
                    case '[' -> {
                        writer.write("while (memArray[ptr] != 0) {");
                        indent++;
                    }
                    case ']' -> {
                        writer.write('}');
                        indent--;
                    }
                    case '.' -> writer.write("System.out.print((char) memArray[ptr]);");
                    case ',' -> writer.write("memArray[ptr] = scanner.nextLine().charAt(0);");
                }
                writer.write('\n');
                writer.flush();
            }
        }
        writer.write(
                """
                    }
                }
                """
        );
    }
}

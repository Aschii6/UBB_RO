import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        /*List<String> whitespaces = Arrays.asList(" ", "\t", "\n");
        List<String> separators = Arrays.asList("{", "}", "(", ")", ";", ",");
        List<String> keywords = Arrays.asList("main", "int", "float", "struct", "cin", "cout",
                "if", "while");

        List<String> assignmentOperators = List.of("=");
        List<String> arithmeticOperators = Arrays.asList("+", "-", "*", "/", "%");
        List<String> comparators = Arrays.asList("==", "!=", "<", "<=", ">", ">=");
        List<String> logicalOperators = Arrays.asList("&&", "||");
        List<String> ioOperators = Arrays.asList("<<", ">>");

        String idRegex = "[a-zA-Z][a-zA-Z0-9]*";
        String intRegex = "[+-]?[0-9]+";
        String floatRegex = "[+-]?[0-9]+.[0-9]+";*/

        // TODO: lexer sa iti zica linia la care este eroare + string handling

        String inputFilePath = "src/test.dan";
        try {
            String code = Files.readString(Path.of(inputFilePath), StandardCharsets.UTF_8);
            Lexer lexer = new Lexer(code);
            List<Token> tokens = lexer.tokenize();
            /*tokens.forEach(System.out::println);*/

            Parser parser = new Parser();
            parser.parse(tokens);
            List<FipElem> fip = parser.getFip();
            MyHashTable<String, String> symbolTable = parser.getSymbolTable();

            String outputFilePath = "src/fip.txt";
            StringBuilder fipContent = new StringBuilder();
            for (FipElem fipElem : fip) {
                fipContent.append(fipElem.getCode()).append(" ").append(fipElem.getTSIndex()).append("\n");
            }
            Files.writeString(Path.of(outputFilePath), fipContent.toString(), StandardCharsets.UTF_8);

            outputFilePath = "src/symbol_table.txt";
            StringBuilder symbolTableContent = new StringBuilder();

            List<HashNode<String, String>> nodes = symbolTable.getItems();

            nodes.sort((n1, n2) -> {
                if (n1.position < n2.position) {
                    return -1;
                } else if (n1.position > n2.position) {
                    return 1;
                }
                return 0;
            });

            for (HashNode<String, String> node : nodes) {
                symbolTableContent.append(node.position).append(" ").append(node.key).append("\n");
            }

            Files.writeString(Path.of(outputFilePath), symbolTableContent.toString(), StandardCharsets.UTF_8);
        } catch (Exception e) {
           throw new RuntimeException(e);
        }
    }
}
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        FiniteAutomaton idFA;

        try {
            String input = Files.readString(Path.of("src/FiniteAutomatonFiles/FA_ID.txt"),
                    StandardCharsets.UTF_8);
            idFA = FiniteAutomatonCreator.createFromString(input);
        } catch (Exception e) {
            throw new RuntimeException("Error reading file");
        }

        FiniteAutomaton intFA;

        try {
            String input = Files.readString(Path.of("src/FiniteAutomatonFiles/FA_INT.txt"),
                    StandardCharsets.UTF_8);
            intFA = FiniteAutomatonCreator.createFromString(input);
        } catch (Exception e) {
            throw new RuntimeException("Error reading file");
        }

        FiniteAutomaton floatFA;

        try {
            String input = Files.readString(Path.of("src/FiniteAutomatonFiles/FA_FLOAT.txt"),
                    StandardCharsets.UTF_8);
            floatFA = FiniteAutomatonCreator.createFromString(input);
        } catch (Exception e) {
            throw new RuntimeException("Error reading file");
        }

        String inputFilePath = "src/test.dan";
        String code;
        try {
            code = Files.readString(Path.of(inputFilePath), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Error reading file");
        }

        Lexer lexer = new Lexer(idFA, intFA, floatFA);
        List<Token> tokens = lexer.tokenize(code);

        String outputFilePath = "src/output/tokens.txt";
        StringBuilder tokensContent = new StringBuilder();
        for (Token token : tokens) {
            tokensContent.append(token.getType()).append(" ").append(token.getValue()).append("\n");
        }
        try {
            Files.writeString(Path.of(outputFilePath), tokensContent.toString(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Error writing file");
        }

        Parser parser = new Parser();
        parser.parse(tokens);

        List<FipElem> fip = parser.getFip();
        MyHashTable<String, String> symbolTable = parser.getSymbolTable();

        outputFilePath = "src/output/fip.txt";
        StringBuilder fipContent = new StringBuilder();
        for (FipElem fipElem : fip) {
            fipContent.append(fipElem.getCode()).append(" ").append(fipElem.getTSIndex()).append("\n");
        }
        try {
            Files.writeString(Path.of(outputFilePath), fipContent.toString(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Error writing file");
        }

        outputFilePath = "src/output/symbol_table.txt";
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
        try {
            Files.writeString(Path.of(outputFilePath), symbolTableContent.toString(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Error writing file");
        }
    }
}
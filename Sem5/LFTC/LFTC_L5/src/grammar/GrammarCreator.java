package grammar;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class GrammarCreator {
    public static Grammar createGrammarFromFile(String inputFile) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(inputFile));

            String startSymbol = lines.get(0);
            if (!startSymbol.startsWith("_")) {
                throw new IllegalArgumentException("Start symbol must start with _");
            }

            startSymbol = startSymbol.substring(1);

            Grammar grammar = new Grammar(startSymbol);
            grammar.addNonTerminalSymbol(startSymbol);

            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i);
                if (line.isBlank()) {
                    continue;
                }

                String[] parts = line.split("->");
                if (parts.length != 2) {
                    throw new IllegalArgumentException("Invalid production: " + line);
                }

                String leftSide = parts[0].trim();

                if (!leftSide.startsWith("_")) {
                    throw new IllegalArgumentException("Non-terminal symbol must start with _");
                }

                leftSide = leftSide.substring(1);
                grammar.addNonTerminalSymbol(leftSide);

                String[] rightSide = parts[1].trim().split(" ");

                if (rightSide.length < 1) {
                    throw new IllegalArgumentException("Invalid production: " + line);
                }

                List<String> rightSideList = new ArrayList<>();

                for (String symbol : rightSide) {
                    if (symbol.startsWith("_")) {
                        grammar.addNonTerminalSymbol(symbol.substring(1));
                        rightSideList.add(symbol.substring(1));
                    } else {
                        grammar.addTerminalSymbol(symbol);
                        rightSideList.add(symbol);
                    }
                }

                grammar.addProduction(new Production(leftSide, rightSideList));
            }

            return grammar;
        } catch (Exception e) {
           throw new RuntimeException("Error reading file: " + e.getMessage());
        }
    }
}

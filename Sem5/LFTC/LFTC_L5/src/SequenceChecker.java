import grammar.*;
import ll_one_analyzer.AnalysisTable;
import ll_one_analyzer.AnalysisTableCreator;
import ll_one_analyzer.LL_One_Analyzer;

import java.util.List;
import java.util.Stack;

public class SequenceChecker {
    private final LL_One_Analyzer analyzer;

    public SequenceChecker(String inputFile) {
        Grammar grammar = GrammarCreator.createGrammarFromFile(inputFile);

        AnalysisTable analysisTable = AnalysisTableCreator.createAnalysisTable(grammar);

        System.out.println(analysisTable);

        analyzer = new LL_One_Analyzer(grammar, analysisTable);
    }

    public void checkSequence(List<String> input) {
        Stack<String> inputStack = new Stack<>();

        inputStack.add("$");

        for (int i = input.size() - 1; i >= 0; i--) {
            inputStack.push(input.get(i));
        }

        List<Integer> result = analyzer.analyze(inputStack);

        if (!result.isEmpty()) {
            System.out.println("Accepted");
            System.out.println(result);
        } else {
            System.out.println("Rejected");
        }
    }
}

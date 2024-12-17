package ll_one_analyzer;

import grammar.Grammar;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class LL_One_Analyzer {
    private final Grammar grammar;
    private final AnalysisTable analysisTable;

    public LL_One_Analyzer(Grammar grammar, AnalysisTable analysisTable) {
        this.grammar = grammar;
        this.analysisTable = analysisTable;
    }

    public List<Integer> analyze(Stack<String> inputStack) {
        List<Integer> productionIndexes = new ArrayList<>();

        Stack<String> workingStack = new Stack<>();
        workingStack.push("$");
        workingStack.push(grammar.getStartSymbol());

        while (!workingStack.isEmpty()) {
            String nextInputSymbol = inputStack.peek();
            String nextWorkingSymbol = workingStack.pop();

            // acc
            if (nextWorkingSymbol.equals("$") && nextInputSymbol.equals("$")) {
                break;
            }

            // pop
            if (nextWorkingSymbol.equals(nextInputSymbol)) {
                inputStack.pop();
                continue;
            }

            // err
            if (analysisTable.getProduction(nextWorkingSymbol, nextInputSymbol) == null) {
                String message = "Input stack at crash: " + inputStack;
                message += "\nWorking stack at crash: " + workingStack;
                message += "\nProduction indexes at crash: " + productionIndexes;
                message += "\nCurrent working symbol: " + nextWorkingSymbol;
                throw new RuntimeException(message);
//                return List.of();
            }

            AnalysisTableCell cell = analysisTable.getProduction(nextWorkingSymbol, nextInputSymbol);

            List<String> production = cell.productionRes();

            for (int i = production.size() - 1; i >= 0; i--) {
                if (production.get(i).equals("eps"))
                    continue;

                workingStack.push(production.get(i));
            }

            productionIndexes.add(cell.productionIndex());
        }

        return productionIndexes;
    }
}

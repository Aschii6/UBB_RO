package lexical_analysis;

import consts.AtomsCode;
import ll_one_analyzer.LL_One_Analyzer;

import java.util.List;
import java.util.Map;
import java.util.Stack;

public class SemanticAnalyzer {
    private final LL_One_Analyzer analyzer;

    private final Map<Integer, String> codeToAtomMap = AtomsCode.getCodeToAtomMap();

    public SemanticAnalyzer(LL_One_Analyzer analyzer) {
        this.analyzer = analyzer;
    }

    public void analyze(List<FipElem> fip) {
        Stack<String> inputStack = new Stack<>();

        inputStack.add("$");

        for (int i = fip.size() - 1; i >= 0; i--) {
            inputStack.add(codeToAtomMap.get(fip.get(i).code));
        }

        try {
            List<Integer> result = analyzer.analyze(inputStack);

            if (!result.isEmpty()) {
                System.out.println("Accepted");
                System.out.println(result);
            } else {
                System.out.println("Rejected");
            }
        } catch (Exception e) {
            System.out.println("Rejected");
            System.out.println(e.getMessage());
        }
    }
}

package ll_one_analyzer;

import grammar.Grammar;
import grammar.Production;

import java.util.*;

public class AnalysisTableCreator {
    public static AnalysisTable createAnalysisTable(Grammar grammar) {
        List<String> terminalSymbols = grammar.getTerminalSymbols();
        List<String> nonTerminalSymbols = grammar.getNonTerminalSymbols();
        List<Production> productions = grammar.getProductions();
        String startSymbol = grammar.getStartSymbol();

        Map<String, Set<String>> first1 = getFirst1(grammar);
        System.out.println(first1);

        Map<String, Set<String>> follow1 = getFollow1(grammar, first1);
        System.out.println(follow1);

        AnalysisTable analysisTable = new AnalysisTable();

        List<Production> productionsCopy = new ArrayList<>(productions);
        List<Production> toRemove = new ArrayList<>();

        for (String nt : nonTerminalSymbols) {
            productionsCopy.removeAll(toRemove);
            toRemove.clear();

            List<Production> productionsForNT = new ArrayList<>();
            for (Production production : productionsCopy) {
                if (production.leftSide().equals(nt)) {
                    productionsForNT.add(production);
                    toRemove.add(production);
                }
            }

            for (Production production : productionsForNT) {
                List<String> right = production.rightSide();
                Set<String> first = new HashSet<>();

                for (String symbol : right) {
                    if (grammar.getTerminalSymbols().contains(symbol)) {
                        first.add(symbol);
                        break;
                    }

                    first.addAll(first1.get(symbol));
                    if (!first.contains("eps")) {
                        break;
                    }
                }

                if (first.contains("eps")) {
                    first.remove("eps");
                    first.addAll(follow1.get(nt));
                }

                for (String terminal : first) {
                    AnalysisTableCell cell = new AnalysisTableCell(right, productions.indexOf(production));
                    analysisTable.addEntry(nt, terminal, cell);
                }
            }
        }

        return analysisTable;
    }

    private static Map<String, Set<String>> getFirst1(Grammar grammar) {
        Map<String, Set<String>> F = new HashMap<>();

        for (Production production : grammar.getProductions()) {
            String left = production.leftSide();
            List<String> right = production.rightSide();

            if (!F.containsKey(left)) {
                F.put(left, new HashSet<>());
            }

            if (grammar.getTerminalSymbols().contains(right.get(0))) {
                F.get(left).add(right.get(0));
            }
        }

        Map<String, Set<String>> nextF = new HashMap<>();

        int i = 0;
        do {
            if (i > 0) F = nextF;

            nextF = new HashMap<>();

            for (Production production : grammar.getProductions()) {
                String left = production.leftSide();
                List<String> right = production.rightSide();

                if (!nextF.containsKey(left)) {
                    nextF.put(left, new HashSet<>());
                }

                for (String symbol : right) {
                    if (grammar.getTerminalSymbols().contains(symbol)) {
                        nextF.get(left).add(symbol);
                        break;
                    }

                    if (F.containsKey(symbol)) {
                        if (F.get(symbol).contains("eps")) {
                            Set<String> temp = new HashSet<>(F.get(symbol));
                            temp.remove("eps");

                            nextF.get(left).addAll(temp);
                        } else {
                            nextF.get(left).addAll(F.get(symbol));
                            break;
                        }
                    }
                }

                Map<String, Set<String>> finalF = F;

                if (right.stream().allMatch(symbol -> symbol.equals("eps") || (finalF.containsKey(symbol) && finalF.get(symbol).contains("eps")))) {
                    nextF.get(left).add("eps");
                }
            }
            i++;
        } while (!F.equals(nextF));

        return nextF;
    }

    private static Map<String, Set<String>> getFollow1(Grammar grammar, Map<String, Set<String>> first1) {
        Map<String, Set<String>> FOLL = new HashMap<>();

        for (String nonTerminal : grammar.getNonTerminalSymbols()) {
            FOLL.put(nonTerminal, new HashSet<>());
        }

        FOLL.get(grammar.getStartSymbol()).add("$");

        // =)
        boolean changed = true;
        while (changed) {
            changed = false;

            for (Production production : grammar.getProductions()) {
                String left = production.leftSide();
                List<String> right = production.rightSide();

                for (int i = 0; i < right.size(); i++) {
                    String symbol = right.get(i);

                    if (grammar.getNonTerminalSymbols().contains(symbol)) {
                        if (i == right.size() - 1) {
                            if (FOLL.get(symbol).addAll(FOLL.get(left))) {
                                changed = true;
                            }
                        } else {
                            if (grammar.getTerminalSymbols().contains(right.get(i + 1))) {
                                if (FOLL.get(symbol).add(right.get(i + 1))) {
                                    changed = true;
                                }
                            } else {

                                Set<String> temp = new HashSet<>(first1.get(right.get(i + 1)));

                                if (temp.contains("eps")) {
                                    temp.remove("eps");
                                    temp.addAll(FOLL.get(left));
                                }

                                if (FOLL.get(symbol).addAll(temp)) {
                                    changed = true;
                                }
                            }
                        }
                    }
                }
            }
        }

        return FOLL;
    }
}

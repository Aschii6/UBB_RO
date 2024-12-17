package grammar;

import java.util.ArrayList;
import java.util.List;

public class Grammar {
    private final List<String> terminalSymbols = new ArrayList<>();
    private final List<String> nonTerminalSymbols = new ArrayList<>();
    private final List<Production> productions = new ArrayList<>();
    private final String startSymbol;

    public Grammar(String startSymbol) {
        this.startSymbol = startSymbol;
    }

    public void addTerminalSymbol(String terminalSymbol) {
        if (!terminalSymbols.contains(terminalSymbol)) {
            terminalSymbols.add(terminalSymbol);
        }
    }

    public void addNonTerminalSymbol(String nonTerminalSymbol) {
        if (!nonTerminalSymbols.contains(nonTerminalSymbol)) {
            nonTerminalSymbols.add(nonTerminalSymbol);
        }
    }

    public void addProduction(Production production) {
        productions.add(production);
    }

    public List<String> getTerminalSymbols() {
        return terminalSymbols;
    }

    public List<String> getNonTerminalSymbols() {
        return nonTerminalSymbols;
    }

    public List<Production> getProductions() {
        return productions;
    }

    public String getStartSymbol() {
        return startSymbol;
    }
}

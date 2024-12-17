import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FiniteAutomaton {
    private boolean isDeterministic;
    private final List<String> states = new ArrayList<>();
    private final List<String> alphabet = new ArrayList<>();
    private final List<String> finalStates;
    private final String initialState;
    private final Map<String, Map<String, List<String>>> transitions = new HashMap<>();


    public FiniteAutomaton(String initialState, List<String> finalStates) {
        this.initialState = initialState;
        this.finalStates = finalStates;

        this.isDeterministic = true;
    }

    public void addTransition(String stateFrom, String symbol, String stateTo) {
        if (!states.contains(stateFrom)) {
            states.add(stateFrom);
        }
        if (!states.contains(stateTo)) {
            states.add(stateTo);
        }
        if (!alphabet.contains(symbol)) {
            alphabet.add(symbol);
        }

        if (!transitions.containsKey(stateFrom)) {
            transitions.put(stateFrom, new HashMap<>());
        }

        Map<String, List<String>> symbolMap = transitions.get(stateFrom);

        if (symbolMap.containsKey(symbol)) {
            isDeterministic = false;
        } else {
            symbolMap.put(symbol, new ArrayList<>());
        }

        symbolMap.get(symbol).add(stateTo);
    }

    public boolean isSequenceAccepted(String sequence) {
        String currentState = initialState;

        for (int i = 0; i < sequence.length(); i++) {
            String symbol = String.valueOf(sequence.charAt(i));

            Map<String, List<String>> symbolMap = transitions.get(currentState);

            if (symbolMap == null) {
                return false;
            }

            if (!symbolMap.containsKey(symbol)) {
                return false;
            }

            List<String> finalStates = symbolMap.get(symbol);
            currentState = finalStates.get(0); // only for DFA
        }

        return finalStates.contains(currentState);
    }

    public String determineLongestPrefix(String sequence) {
        String prefix = "";

        for (int i = sequence.length(); i > 0; i--) {
            String currentSequence = sequence.substring(0, i);
            if (isSequenceAccepted(currentSequence)) {
                prefix = currentSequence;
                break;
            }
        }

        return prefix;
    }

    public boolean isDeterministic() {
        return isDeterministic;
    }

    public List<String> getStates() {
        return states;
    }

    public List<String> getAlphabet() {
        return alphabet;
    }

    public List<String> getFinalStates() {
        return finalStates;
    }

    public String getInitialState() {
        return initialState;
    }
}

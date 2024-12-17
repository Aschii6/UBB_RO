import java.util.ArrayList;
import java.util.List;

public class FiniteAutomatonCreator {
    public static FiniteAutomaton createFromString(String input) {
        String initialState = "";
        List<String> finalStates = new ArrayList<>();
        List<String> transitions = new ArrayList<>();

        String[] lines = input.split("\n");

        boolean finalStatesReached = false;

        for (String line : lines) {
            line = line.trim();

            if (line.isEmpty()) {
                continue;
            }

            if (initialState.isEmpty()) {
                initialState = line;
            } else {
                if (line.equals(";")) {
                    finalStatesReached = true;
                    continue;
                }

                if (!finalStatesReached) {
                    transitions.add(line);
                } else {
                    finalStates.add(line);
                }
            }
        }

        FiniteAutomaton finiteAutomaton = new FiniteAutomaton(initialState, finalStates);

        for (String transition : transitions) {
            String[] parts = transition.split(", ");
            String stateFrom = parts[0];
            String stateTo = parts[parts.length - 1];

            for (int i = 1; i < parts.length - 1; i++) {
                finiteAutomaton.addTransition(stateFrom, parts[i], stateTo);
            }
        }

        return finiteAutomaton;
    }
}

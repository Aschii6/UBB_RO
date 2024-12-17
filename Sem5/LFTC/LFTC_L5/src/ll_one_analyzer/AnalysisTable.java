package ll_one_analyzer;

import java.util.HashMap;
import java.util.Map;

public class AnalysisTable {
    private final Map<String, Map<String, AnalysisTableCell>> table = new HashMap<>();

    public void addEntry(String lineSymbol, String columnSymbol, AnalysisTableCell cell) {
        if (!table.containsKey(lineSymbol)) {
            table.put(lineSymbol, new HashMap<>());
        } else if (table.get(lineSymbol).containsKey(columnSymbol)) {
            if (!table.get(lineSymbol).get(columnSymbol).equals(cell)) {
                System.out.println(lineSymbol + " " + columnSymbol);
                System.out.println(table.get(lineSymbol).get(columnSymbol));
                System.out.println(cell);

                throw new IllegalArgumentException("Conflict in the table");
            }
        }

        table.get(lineSymbol).put(columnSymbol, cell);
    }

    public AnalysisTableCell getProduction(String lineSymbol, String columnSymbol) {
        if (!table.containsKey(lineSymbol)) {
            return null;
        }

        return table.get(lineSymbol).get(columnSymbol);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (String lineSymbol : table.keySet()) {
            sb.append(lineSymbol).append(" -> ");

            for (String columnSymbol : table.get(lineSymbol).keySet()) {
                sb.append(columnSymbol).append(": ").append(table.get(lineSymbol).get(columnSymbol)).append(", ");
            }

            sb.append("\n");
        }

        return sb.toString();
    }
}

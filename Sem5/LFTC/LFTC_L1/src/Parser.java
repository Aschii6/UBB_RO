import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Parser {
    Map<String, Integer> atomsCode = new HashMap<>() {{
        put("ID", 0);
        put("CONST", 1);
        put("int main", 2);
        put("int", 3);
        put("float", 4);
        put("struct", 5);
        put("cin", 6);
        put("cout", 7);
        put("if", 8);
        put("while", 9);
        put("{", 10);
        put("}", 11);
        put("(", 12);
        put(")", 13);
        put(";", 14);
        put(",", 15);
        put("+", 16);
        put("-", 17);
        put("*", 18);
        put("/", 19);
        put("%", 20);
        put("<<", 21);
        put(">>", 22);
        put("==", 23);
        put("!=", 24);
        put("<=", 25);
        put("<", 26);
        put(">=", 27);
        put(">", 28);
        put("&&", 29);
        put("||", 30);
        put("=", 31);
    }};

    private final List<FipElem> fip = new ArrayList<>();
    private final MyHashTable<String, String> symbolTable = new MyHashTable<>();

    public void parse(List<Token> tokens) {
        for (Token token : tokens) {
            String value = token.getValue();

            if (token.getType() == TokenType.IDENTIFIER || token.getType() == TokenType.CONSTANT) {
                if (symbolTable.getNode(value) == null) {
                    symbolTable.add(value, "");
                }

                HashNode<String, String> node = symbolTable.getNode(value);
                int position = node.position;

                int code = token.getType() == TokenType.IDENTIFIER ? 1 : 0;

                fip.add(new FipElem(code, position));
            } else {
                fip.add(new FipElem(atomsCode.get(token.getValue())));
            }
        }
    }

    public List<FipElem> getFip() {
        return fip;
    }

    public MyHashTable<String, String> getSymbolTable() {
        return symbolTable;
    }
}

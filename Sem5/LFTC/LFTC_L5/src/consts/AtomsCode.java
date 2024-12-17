package consts;

import java.util.HashMap;
import java.util.Map;

public class AtomsCode {
    static Map<String, Integer> atomToCodeMap = new HashMap<>() {{
        put("ID", 0);
        put("CONST", 1);
        put("main", 2);
        put("int", 3);
        put("float", 4);
        put("struct", 5);
        put("cin", 6);
        put("cout", 7);
        put("if", 8);
        put("else", 9);
        put("while", 10);
        put("{", 11);
        put("}", 12);
        put("(", 13);
        put(")", 14);
        put(";", 15);
        put(",", 16);
        put("+", 17);
        put("-", 18);
        put("*", 19);
        put("/", 20);
        put("%", 21);
        put("=", 22);
        put("<<", 23);
        put(">>", 24);
        put("==", 25);
        put("!=", 26);
        put("<=", 27);
        put("<", 28);
        put(">=", 29);
        put(">", 30);
        put("&&", 31);
        put("||", 32);
    }};

    public static Map<String, Integer> getAtomToCodeMap() {
        return atomToCodeMap;
    }

    public static Map<Integer, String> getCodeToAtomMap() {
        Map<Integer, String> reversedMap = new HashMap<>();
        for (Map.Entry<String, Integer> entry : atomToCodeMap.entrySet()) {
            reversedMap.put(entry.getValue(), entry.getKey());
        }
        return reversedMap;
    }
}

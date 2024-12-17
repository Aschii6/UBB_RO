package lexical_analysis;

import consts.AtomsCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Parser {
    Map<String, Integer> atomToCodeMap = AtomsCode.getAtomToCodeMap();

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

                int code = token.getType() == TokenType.IDENTIFIER ? 0 : 1;

                fip.add(new FipElem(code, position));
            } else {
                fip.add(new FipElem(atomToCodeMap.get(token.getValue())));
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

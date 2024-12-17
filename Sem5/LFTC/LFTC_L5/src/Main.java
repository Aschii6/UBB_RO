import lexical_analysis.*;
import lexical_analysis.Compiler;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        /*tring inputFile = "src/inputs/testGrammar.txt";

        SequenceChecker sequenceChecker = new SequenceChecker(inputFile);

        List<String> input1 = List.of("a", "a", "i", "b", "i", "c", "c");
        List<String> input2 = List.of("(", "id", "+", "id", ")");

        sequenceChecker.checkSequence(input2);*/

        Compiler compiler = new Compiler();
        compiler.compile();
    }
}
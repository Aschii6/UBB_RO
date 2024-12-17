package lexical_analysis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Lexer {
    private String input;
    private int index;
    private int line;

    List<Token> tokens;

    private final FiniteAutomaton idFA;
    private final FiniteAutomaton intFA;
    private final FiniteAutomaton floatFA;

    private final List<String> keywords = Arrays.asList("main", "int", "float", "struct", "cin",
            "cout", "if", "else", "while");

    List<String> separators = Arrays.asList("{", "}", "(", ")", ";", ",");
    List<String> arithmeticOperators = Arrays.asList("+", "-", "*", "/", "%");
    List<String> ioOperators = Arrays.asList("<<", ">>");
    List<String> comparators = Arrays.asList("==", "!=", "<=", "<", ">=", ">");
    List<String> logicalOperators = Arrays.asList("&&", "||");
    List<String> assignmentOperators = List.of("=");
//    List<String> memberAccessOperators = List.of(".");

    public Lexer(FiniteAutomaton idFA, FiniteAutomaton intFA, FiniteAutomaton floatFA) {
        this.idFA = idFA;
        this.intFA = intFA;
        this.floatFA = floatFA;

        this.line = 1;
    }

    public List<Token> tokenize(String input) {
        this.input = input;
        this.index = 0;

        tokens = new ArrayList<>();

        while (index < input.length()) {
            char currentChar = input.charAt(index);

            if (Character.isWhitespace(currentChar)) {
                if (currentChar == '\n') {
                    line++;
                }
                index++;
                continue;
            }

            Token token = nextToken();
            if (token != null) {
                tokens.add(token);
            } else {
                throw new RuntimeException("Unknown character: " + currentChar + " on line " + line);
            }
        }

        return tokens;
    }

    private Token nextToken() {
        if (index >= input.length())
            return null;

        String tokenValue = tryExtractKeyword();
        if (tokenValue != null) {
            return new Token(TokenType.KEYWORD, tokenValue);
        }

        tokenValue = tryExtractIdentifier();
        if (tokenValue != null) {
            return new Token(TokenType.IDENTIFIER, tokenValue);
        }

        tokenValue = tryExtractFloat();
        if (tokenValue != null) {
            return new Token(TokenType.CONSTANT, tokenValue);
        }

        tokenValue = tryExtractInt();
        if (tokenValue != null) {
            return new Token(TokenType.CONSTANT, tokenValue);
        }

        tokenValue = tryExtractSeparator();
        if (tokenValue != null) {
            return new Token(TokenType.SEPARATOR, tokenValue);
        }

        tokenValue = tryExtractArithmeticOperator();
        if (tokenValue != null) {
            return new Token(TokenType.ARITHMETIC_OPERATOR, tokenValue);
        }

        tokenValue = tryExtractIoOperator();
        if (tokenValue != null) {
            return new Token(TokenType.IO_OPERATOR, tokenValue);
        }

        tokenValue = tryExtractComparator();
        if (tokenValue != null) {
            return new Token(TokenType.COMPARATOR, tokenValue);
        }

        tokenValue = tryExtractLogicalOperator();
        if (tokenValue != null) {
            return new Token(TokenType.LOGICAL_OPERATOR, tokenValue);
        }

        tokenValue = tryExtractAssignmentOperator();
        if (tokenValue != null) {
            return new Token(TokenType.ASSIGNMENT_OPERATOR, tokenValue);
        }

        return null;
    }

    private String tryExtractKeyword() {
        for (String keyword : keywords) {
            if (input.startsWith(keyword, index)) {
                int end = index + keyword.length();
                if (end == input.length() || !Character.isLetterOrDigit(input.charAt(end))) { // mm
                    index = end;
                    return keyword;
                }
            }
        }
        return null;
    }

    private String tryExtractIdentifier() {
        String longestPrefix = idFA.determineLongestPrefix(input.substring(index));

        index += longestPrefix.length();
        return longestPrefix.isEmpty() ? null : longestPrefix;
    }

    private String tryExtractInt() {
        // probably an arithmetic operation is separating them
        if (!tokens.isEmpty() && (tokens.get(tokens.size() - 1).getType() == TokenType.IDENTIFIER ||
                tokens.get(tokens.size() - 1).getType() == TokenType.CONSTANT)) {
            return null;
        }

        String longestPrefix = intFA.determineLongestPrefix(input.substring(index));

        index += longestPrefix.length();
        return longestPrefix.isEmpty() ? null : longestPrefix;
    }

    private String tryExtractFloat() {
        if (!tokens.isEmpty() && (tokens.get(tokens.size() - 1).getType() == TokenType.IDENTIFIER ||
                tokens.get(tokens.size() - 1).getType() == TokenType.CONSTANT)) {
            return null;
        }

        String longestPrefix = floatFA.determineLongestPrefix(input.substring(index));

        index += longestPrefix.length();
        return longestPrefix.isEmpty() ? null : longestPrefix;
    }

    private String tryExtractSeparator() {
        for (String separator : separators) {
            if (input.startsWith(separator, index)) {
                index += separator.length();
                return separator;
            }
        }
        return null;
    }

    private String tryExtractArithmeticOperator() {
        for (String operator : arithmeticOperators) {
            if (input.startsWith(operator, index)) {
                index += operator.length();
                return operator;
            }
        }
        return null;
    }

    private String tryExtractIoOperator() {
        for (String operator : ioOperators) {
            if (input.startsWith(operator, index)) {
                index += operator.length();
                return operator;
            }
        }
        return null;
    }

    private String tryExtractComparator() {
        for (String operator : comparators) {
            if (input.startsWith(operator, index)) {
                index += operator.length();
                return operator;
            }
        }
        return null;
    }

    private String tryExtractLogicalOperator() {
        for (String operator : logicalOperators) {
            if (input.startsWith(operator, index)) {
                index += operator.length();
                return operator;
            }
        }
        return null;
    }

    private String tryExtractAssignmentOperator() {
        for (String operator : assignmentOperators) {
            if (input.startsWith(operator, index)) {
                index += operator.length();
                return operator;
            }
        }
        return null;
    }
}

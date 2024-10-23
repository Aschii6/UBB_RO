import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
    private String input;

    private int index;

    public Lexer(String input) {
        this.input = input;
        this.index = 0;
    }

    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();

        while (index < input.length()) {
            char currentChar = input.charAt(index);

            if (Character.isWhitespace(currentChar)) {
                index++;
                continue;
            }

            Token token = nextToken();
            if (token != null) {
                tokens.add(token);
            } else {
                throw new RuntimeException("Unknown character: " + currentChar);
            }
        }

        return tokens;
    }

    private Token nextToken() {
        if (index >= input.length())
            return null;

        String[] tokenPatterns = {
                "(int main|int|float|struct|cin|cout|if|while)", // KEYWORD
                "[a-zA-Z][a-zA-Z0-9]*", // ID
                "[+-]?[0-9]+\\.[0-9]+", // FLOAT
                "[+-]?[0-9]+", // INT
                "[{}();,]", // SEPARATORS
                "[+\\-*/%]", // ARITHMETIC_OPERATOR
                "(<<|>>)", // IO_OPERATOR
                "(==|!=|<=|<|>=|>)", // COMPARATOR
                "(&&|\\|\\|)", // LOGICAL_OPERATOR
                "=", // ASSIGNMENT_OPERATOR
        };

        TokenType[] tokenTypes = {
                TokenType.KEYWORD,
                TokenType.IDENTIFIER,
                TokenType.CONSTANT,
                TokenType.CONSTANT,
                TokenType.SEPARATOR,
                TokenType.ARITHMETIC_OPERATOR,
                TokenType.IO_OPERATOR,
                TokenType.COMPARATOR,
                TokenType.LOGICAL_OPERATOR,
                TokenType.ASSIGNMENT_OPERATOR,
        };

        for (int i = 0; i < tokenPatterns.length; i++) {
            Pattern pattern = Pattern.compile("^" + tokenPatterns[i]);
            Matcher matcher = pattern.matcher(input.substring(index));

            if (matcher.find()) {
                String value = matcher.group();
                index += value.length();
                return new Token(tokenTypes[i], value);
            }
        }

        return null;
    }
}

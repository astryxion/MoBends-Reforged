package goblinbob.mobends.core.expression;

import java.util.ArrayList;
import java.util.List;

public class Tokenizer {
    private final String source;
    private int position;
    private final List<Token> tokens;

    public Tokenizer(String source) {
        this.source = source;
        this.position = 0;
        this.tokens = new ArrayList<>();
    }

    public List<Token> tokenize() {
        while (!isAtEnd()) {
            skipWhitespace();
            if (isAtEnd()) break;

            int startPos = position;
            char c = advance();

            switch (c) {
case '+':
addToken(TokenType.PLUS, "+", startPos);
break;
case '-':
addToken(TokenType.MINUS, "-", startPos);
break;
case '*':
addToken(TokenType.STAR, "*", startPos);
break;
case '/':
addToken(TokenType.SLASH, "/", startPos);
break;
case '%':
addToken(TokenType.PERCENT, "%", startPos);
break;
case '^':
addToken(TokenType.CARET, "^", startPos);
break;
case '(':
addToken(TokenType.LPAREN, "(", startPos);
break;
case ')':
addToken(TokenType.RPAREN, ")", startPos);
break;
case ',':
addToken(TokenType.COMMA, ",", startPos);
break;
case '?':
addToken(TokenType.QUESTION, "?", startPos);
break;
case ':':
addToken(TokenType.COLON, ":", startPos);
break;
case '<':

                    if (match('=')) {
                        addToken(TokenType.LESS_EQUAL, "<=", startPos);
                    } else {
                        addToken(TokenType.LESS, "<", startPos);
                    }
break;
case '>':

                    if (match('=')) {
                        addToken(TokenType.GREATER_EQUAL, ">=", startPos);
                    } else {
                        addToken(TokenType.GREATER, ">", startPos);
                    }
break;
case '=':

                    if (match('=')) {
                        addToken(TokenType.EQUAL, "==", startPos);
                    } else {
                        throw new ExpressionException("Expected '=' after '='", source, startPos);
                    }
                    break;
case '!':

                    if (match('=')) {
                        addToken(TokenType.NOT_EQUAL, "!=", startPos);
                    } else {
                        addToken(TokenType.NOT, "!", startPos);
                    }
break;
case '&':

                    if (match('&')) {
                        addToken(TokenType.AND, "&&", startPos);
                    } else {
                        throw new ExpressionException("Expected '&' after '&'", source, startPos);
                    }
                    break;
case '|':

                    if (match('|')) {
                        addToken(TokenType.OR, "||", startPos);
                    } else {
                        throw new ExpressionException("Expected '|' after '|'", source, startPos);
                    }
                    break;
default:

                    if (isDigit(c) || (c == '.' && !isAtEnd() && isDigit(peek()))) {
                        position--;
                        scanNumber(startPos);
                    } else if (isIdentifierStart(c)) {
                        position--;
                        scanIdentifier(startPos);
                    } else {
                        throw new ExpressionException("Unexpected character: '" + c + "'", source, startPos);
                    }
}
        }

        tokens.add(new Token(TokenType.EOF, "", position));
        return tokens;
    }

    private void scanNumber(int startPos) {
        StringBuilder sb = new StringBuilder();
        boolean hasDecimal = false;
        boolean hasExponent = false;

        while (!isAtEnd()) {
            char c = peek();
            if (isDigit(c)) {
                sb.append(advance());
            } else if (c == '.' && !hasDecimal && !hasExponent) {
                if (position + 1 < source.length() && isDigit(source.charAt(position + 1))) {
                    hasDecimal = true;
                    sb.append(advance());
                } else if (sb.length() == 0) {
                    hasDecimal = true;
                    sb.append(advance());
                } else {
                    break;
                }
            } else if ((c == 'e' || c == 'E') && !hasExponent && sb.length() != 0) {
                hasExponent = true;
                sb.append(advance());
                if (!isAtEnd() && (peek() == '+' || peek() == '-')) {
                    sb.append(advance());
                }
            } else {
                break;
            }
        }

        addToken(TokenType.NUMBER, sb.toString(), startPos);
    }

    private void scanIdentifier(int startPos) {
        StringBuilder sb = new StringBuilder();
        while (!isAtEnd() && isIdentifierPart(peek())) {
            sb.append(advance());
        }
        addToken(TokenType.IDENTIFIER, sb.toString(), startPos);
    }

    private void skipWhitespace() {
        while (!isAtEnd() && Character.isWhitespace(peek())) {
            advance();
        }
    }

    private boolean isAtEnd() {
        return position >= source.length();
    }

    private char peek() {
        return source.charAt(position);
    }

    private char advance() {
        return source.charAt(position++);
    }

    private boolean match(char expected) {
        if (isAtEnd() || peek() != expected) {
            return false;
        }
        position++;
        return true;
    }

    private void addToken(TokenType type, String value, int position) {
        tokens.add(new Token(type, value, position));
    }

    private static boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private static boolean isIdentifierStart(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_';
    }

    private static boolean isIdentifierPart(char c) {
        return isIdentifierStart(c) || isDigit(c);
    }
}

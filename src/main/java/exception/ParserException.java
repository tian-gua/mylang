package exception;

import lexer.Token;

public class ParserException extends RuntimeException {
    public ParserException(Token token, String expect) {
        super("expect " + expect + " but " + token);
    }
}

package ast;

import lexer.Token;

public class StringLeaf extends AstLeaf {

    public StringLeaf(Token token) {
        super(token);
    }

    @Override
    public String eval() {
        String value = (String) getToken().value();
        return value.replace("\"", "");
    }
}

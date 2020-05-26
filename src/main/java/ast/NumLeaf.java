package ast;

import lexer.Token;

public class NumLeaf extends AstLeaf {

    public NumLeaf(Token token) {
        super(token);
    }

    public Integer eval() {
        return (Integer) getToken().value();
    }
}

package ast;

import lexer.Token;

import java.util.List;

public class AstLeaf extends AstTree {

    private Token token;

    public AstLeaf() {
    }

    public AstLeaf(Token token) {
        this.token = token;
    }

    public Token getToken() {
        return token;
    }

    @Override
    public int getNodeSize() {
        return -1;
    }

    @Override
    public AstTree child(int i) {
        return null;
    }

    @Override
    public List<AstTree> nodes() {
        return null;
    }

    @Override
    public Object eval() {
        return token.value();
    }
}

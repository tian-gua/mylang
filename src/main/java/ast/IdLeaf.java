package ast;

import lexer.IdToken;
import lexer.Token;

public class IdLeaf extends AstLeaf {

    public IdLeaf(Token token) {
        super(token);
    }

    public Object eval() {
        return Context.get().getId(this);
    }

    @Override
    public IdToken getToken() {
        return (IdToken) super.getToken();
    }
}

package ast;

import lexer.Token;

import java.util.List;

public abstract class AstTree {

    public abstract int getNodeSize();

    public abstract AstTree child(int i);

    public abstract List<AstTree> nodes();

    public abstract Token getToken();

    public abstract Object eval();
}

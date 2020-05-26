package ast;

import exception.EvalException;
import lexer.Token;

import java.util.List;

public class AstList extends AstTree {

    private List<AstTree> nodes;

    public AstList() {
    }

    public AstList(List<AstTree> nodes) {
        this.nodes = nodes;
    }

    @Override
    public int getNodeSize() {
        return nodes.size();
    }

    @Override
    public AstTree child(int i) {
        return nodes.get(i);
    }

    @Override
    public List<AstTree> nodes() {
        return nodes;
    }

    protected void setNodeList(List<AstTree> nodes) {
        this.nodes = nodes;
    }

    @Override
    public Token getToken() {
        return null;
    }

    @Override
    public Object eval() {
        throw new EvalException();
    }
}

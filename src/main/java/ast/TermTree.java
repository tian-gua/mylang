package ast;

import lexer.Token;

import java.util.ArrayList;
import java.util.List;

public class TermTree extends AstList {


    public TermTree(FactorTree factorTree) {
        List<AstTree> astTreeList = new ArrayList<>();
        astTreeList.add(factorTree);
        this.setNodeList(astTreeList);
    }

    public TermTree(FactorTree left, AstLeaf op, FactorTree right) {
        List<AstTree> astTreeList = new ArrayList<>();
        astTreeList.add(left);
        astTreeList.add(op);
        astTreeList.add(right);
        this.setNodeList(astTreeList);
    }

    public TermTree(TermTree termTree, AstLeaf op, FactorTree right) {
        List<AstTree> astTreeList = new ArrayList<>();
        astTreeList.add(termTree);
        astTreeList.add(op);
        astTreeList.add(right);
        this.setNodeList(astTreeList);
    }

    public Object eval() {
        if (getNodeSize() == 1) {
            return child(0).eval();
        } else {
            AstTree left  = child(0);
            AstTree right = child(2);
            Token   op    = child(1).getToken();
            if (op.value().equals("*"))
                return (Integer) (left.eval()) * (Integer) (right.eval());
            else {
                return (Integer) left.eval() / (Integer) (right.eval());
            }
        }
    }
}

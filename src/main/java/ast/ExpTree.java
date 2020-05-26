package ast;

import exception.EvalException;
import lexer.OperatorToken;
import lexer.Token;

import java.util.ArrayList;
import java.util.List;

public class ExpTree extends AstList {

    public ExpTree(List<AstTree> nodes) {
        super(nodes);
    }

    public ExpTree(AstTree astTree) {
        List<AstTree> astTreeList = new ArrayList<>();
        astTreeList.add(astTree);
        this.setNodeList(astTreeList);
    }

    public ExpTree(TermTree termTree, AstLeaf astLeaf, ExpTree expTree) {
        List<AstTree> astTreeList = new ArrayList<>();
        astTreeList.add(termTree);
        astTreeList.add(astLeaf);
        astTreeList.add(expTree);
        this.setNodeList(astTreeList);
    }

    public ExpTree(StringLeaf leaf, AstLeaf astLeaf, StringLeaf right) {
        List<AstTree> astTreeList = new ArrayList<>();
        astTreeList.add(leaf);
        astTreeList.add(astLeaf);
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
            if (left.eval() instanceof String || right.eval() instanceof String) {
                final StringBuilder stringBuilder = new StringBuilder();
                nodes().forEach(v -> {
                    if (!(v.getToken() instanceof OperatorToken)) {
                        stringBuilder.append(v.eval());
                    }
                });
                return stringBuilder.toString();

            }
            if (op.value().equals("+"))
                return (Integer) (left.eval()) + (Integer) (right.eval());
            else if (op.value().equals("-")) {
                return (Integer) (left.eval()) - (Integer) (right.eval());
            } else if (op.value().equals("==")) {
                return left.eval().equals(right.eval());
            } else if (op.value().equals(">")) {
                return (Integer) (left.eval()) > (Integer) (right.eval());
            } else if (op.value().equals("<")) {
                return (Integer) (left.eval()) < (Integer) (right.eval());
            } else if (op.value().equals("<=")) {
                return (Integer) (left.eval()) <= (Integer) (right.eval());
            } else if (op.value().equals(">=")) {
                return (Integer) (left.eval()) >= (Integer) (right.eval());
            } else {
                throw new EvalException();
            }
        }
    }
}

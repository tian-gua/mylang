package ast;

import java.util.ArrayList;
import java.util.List;

public class FactorTree extends AstList {

    public FactorTree(AstLeaf astLeaf) {
        List<AstTree> astTreeList = new ArrayList<>();
        astTreeList.add(astLeaf);
        this.setNodeList(astTreeList);
    }

    public FactorTree(AstLeaf leftBrace, ExpTree expTree, AstLeaf rightBrace) {
        List<AstTree> astTreeList = new ArrayList<>();
        astTreeList.add(leftBrace);
        astTreeList.add(expTree);
        astTreeList.add(rightBrace);
        this.setNodeList(astTreeList);
    }

    public Object eval() {
        if (getNodeSize() == 1) {
            return child(0).eval();
        } else {
            return child(1).eval();
        }
    }
}

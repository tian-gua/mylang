package ast;

import java.util.ArrayList;
import java.util.List;

public class WhileTree extends AstList {

    public WhileTree(ExpTree condition, BlockTree blockTree) {
        List<AstTree> astTreeList = new ArrayList<>();
        astTreeList.add(condition);
        astTreeList.add(blockTree);
        this.setNodeList(astTreeList);
        eval();
    }

    @Override
    public Class<Void> eval() {
        ExpTree condition = (ExpTree) child(0);
        Boolean b         = (Boolean) condition.eval();
        while (b) {
            BlockTree blockTree = (BlockTree) child(1);
            for (AstTree node : blockTree.nodes()) {
                if (node instanceof AssignTree) {
                    ((AssignTree) node).setHasEval(false);
                }
                node.eval();
            }
            b = (Boolean) condition.eval();
        }
        return Void.TYPE;
    }
}

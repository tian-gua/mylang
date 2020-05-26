package ast;

import java.util.ArrayList;
import java.util.List;

public class IFTree extends AstList {

    public IFTree(ExpTree condition, BlockTree blockTree) {
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
        if (b) {
            BlockTree blockTree = (BlockTree) child(1);
            for (AstTree node : blockTree.nodes()) {
                node.eval();
            }
        }
        return Void.TYPE;
    }
}

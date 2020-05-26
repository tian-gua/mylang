package ast;

import java.util.List;

public class BlockTree extends AstList {

    public BlockTree(List<AstTree> nodes) {
        super(nodes);
    }

    @Override
    public Class<Void> eval() {
        for (AstTree node : nodes()) {
            node.eval();
        }
        return Void.TYPE;
    }
}

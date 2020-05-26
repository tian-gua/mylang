package ast;

import java.util.ArrayList;
import java.util.List;

public class AssignTree extends AstList {

    private boolean hasEval = false;

    public AssignTree(IdLeaf idLeaf, AstLeaf opTree, ExpTree expTree) {
        List<AstTree> astTreeList = new ArrayList<>();
        astTreeList.add(idLeaf);
        astTreeList.add(opTree);
        astTreeList.add(expTree);
        this.setNodeList(astTreeList);
        eval();
        hasEval = true;
    }

    public void setHasEval(boolean hasEval) {
        this.hasEval = hasEval;
    }

    public Object eval() {
        Environment environment = Context.get();
        IdLeaf      left        = (IdLeaf) child(0);
        if (hasEval) {
            return environment.getId(left);
        }

        ExpTree right = (ExpTree) child(2);
        Object  value;
        if (environment.containsId(left)) {
            value = environment.updateId(left, right);
        } else {
            value = environment.createId(left, right);
        }
        return value;
    }
}

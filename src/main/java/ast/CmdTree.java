package ast;

import java.util.ArrayList;
import java.util.List;

public class CmdTree extends AstList {

    private final String cmd;

    public CmdTree(String cmd, ExpTree expTree) {
        this.cmd = cmd;
        List<AstTree> astTreeList = new ArrayList<>();
        astTreeList.add(expTree);
        this.setNodeList(astTreeList);
        eval();
    }


    public Class<Void> eval() {
        if (cmd.equals("print")) {
            System.out.println(child(0).eval());
        }
        return Void.TYPE;
    }
}

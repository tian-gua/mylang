package ast;

import java.util.HashMap;
import java.util.Map;

public class Environment {

    private Map<String, Object> env = new HashMap<>();

    public Object getId(IdLeaf idLeaf) {
        return env.get(idLeaf.getToken().getId());
    }

    public Object getId(String id) {
        return env.get(id);
    }

    public Object createId(IdLeaf idLeaf, ExpTree expTree) {
        Object value = expTree.eval();
        env.put(idLeaf.getToken().getId(), value);
        return value;
    }

    public Object updateId(IdLeaf idLeaf, ExpTree expTree) {
        Object value = expTree.eval();
        env.put(idLeaf.getToken().getId(), value);
        return value;
    }

    public boolean containsId(IdLeaf idLeaf) {
        return env.containsKey(idLeaf.getToken().getId());
    }
}

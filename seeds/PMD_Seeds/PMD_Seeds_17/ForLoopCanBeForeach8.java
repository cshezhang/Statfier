
import java.util.Iterator;
class Foo {
    void loop() {
        Iterable<DataFlowNode> path = null;
        for (Iterator<DataFlowNode> i = path.iterator(); i.hasNext();) {
            DataFlowNode inode = i.next();
            if (inode.getVariableAccess() == null) {
                continue;
            }
        }
    }
}
        
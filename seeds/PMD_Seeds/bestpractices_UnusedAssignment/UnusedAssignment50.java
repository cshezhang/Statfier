
public class Test {
    public void test(){
       ScopeData iter = acceptOpt(node.getBody(), before.fork()); // this assignment is unused
       iter = acceptOpt(node.getCondition(), before.fork());
       iter = acceptOpt(node.getBody(), iter);
    }
}
        
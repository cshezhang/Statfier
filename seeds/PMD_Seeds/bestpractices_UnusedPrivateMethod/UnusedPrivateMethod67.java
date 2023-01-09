
public class UnusedAssignmentRule {
    private static class ReachingDefsVisitor {
        private static String getVarIfUnaryAssignment(String node) {
            return null;
        }
    }
    public static class AssignmentEntry {
        final String var;
        final Object rhs;
        public boolean isUnaryReassign() {
            return rhs instanceof String
                && var.equals(ReachingDefsVisitor.getVarIfUnaryAssignment((String) rhs));
        }
    }
}
        
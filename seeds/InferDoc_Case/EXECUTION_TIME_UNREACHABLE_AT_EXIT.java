import static java.lang.System.exit;

import com.google.common.base.Preconditions;

public class EXECUTION_TIME_UNREACHABLE_AT_EXIT {
  void exit_unreachable() {
    exit(0); // modeled as unreachable
  }

  void infeasible_path_unreachable() {
    Preconditions.checkState(false); // like assert false, state pruned to bottom
  }
}





public class Continue {
  /* Expected 1000 * 1000 / 2 */
  int continue_outer_loop_constant() {
    outer:
    for (int i = 2; i < 1000; i++) {
      for (int j = 2; j < i; j++) {
        if (i % j == 0) continue outer;
      }
    }
    return 0;
  }
}

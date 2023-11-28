public class Foo {
  final boolean a1 = false;
  final boolean a2 = true;
  final boolean a3 = computed();

  final Boolean b1 = false;
  final Boolean b2 = true;
  final Boolean b3 = computed();

  static final boolean c1 = false;
  static final boolean c2 = true;
  static final boolean c3 = computed();

  static final Boolean d1 = false;
  static final Boolean d2 = true;
  static final Boolean d3 = computed();

  boolean e1;
  boolean e2 = false; // Bad
  boolean e3 = true;
  boolean e4 = computed();

  Boolean f1;
  Boolean f2 = false;
  Boolean f3 = true;
  Boolean f4 = computed();

  static boolean g1;
  static boolean g2 = false; // Bad
  static boolean g3 = true;
  static boolean g4 = computed();

  static Boolean h1;
  static Boolean h2 = false;
  static Boolean h3 = true;
  static Boolean h4 = computed();

  static boolean computed() {
    return false;
  }
}


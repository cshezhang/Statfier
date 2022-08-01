
public class Foo {
    final int a1 = 0;
    final int a2 = 1;
    final int a3 = (int) 0L;
    final int a4 = (int) 2L;
    final int a5 = 00;
    final int a6 = 0123;
    final int a7 = (int) 00L;
    final int a8 = (int) 0123L;
    final int a9 = 0x0;
    final int a10 = 0x1;
    final int a11 = (int) 0x0L;
    final int a12 = (int) 0x1L;
    final int a13 = '\u0000';
    final int a14 = 'a';
    final int a15 = computed();

    final Integer b1 = 0;
    final Integer b2 = 1;
    final Integer b3 = (int) 0L;
    final Integer b4 = (int) 2L;
    final Integer b5 = 00;
    final Integer b6 = 0123;
    final Integer b7 = (int) 00L;
    final Integer b8 = (int) 0123L;
    final Integer b9 = 0x0;
    final Integer b10 = 0x1;
    final Integer b11 = (int) 0x0L;
    final Integer b12 = (int) 0x1L;
    final Integer b13 = '\u0000';
    final Integer b14 = 'a';
    final Integer b15 = computed();

    static final int c1 = 0;
    static final int c2 = 1;
    static final int c3 = (int) 0L;
    static final int c4 = (int) 2L;
    static final int c5 = 00;
    static final int c6 = 0123;
    static final int c7 = (int) 00L;
    static final int c8 = (int) 0123L;
    static final int c9 = 0x0;
    static final int c10 = 0x1;
    static final int c11 = (int) 0x0L;
    static final int c12 = (int) 0x1L;
    static final int c13 = '\u0000';
    static final int c14 = 'a';
    static final int c15 = computed();

    static final Integer d1 = 0;
    static final Integer d2 = 1;
    static final Integer d3 = (int) 0L;
    static final Integer d4 = (int) 2L;
    static final Integer d5 = 00;
    static final Integer d6 = 0123;
    static final Integer d7 = (int) 00L;
    static final Integer d8 = (int) 0123L;
    static final Integer d9 = 0x0;
    static final Integer d10 = 0x1;
    static final Integer d11 = (int) 0x0L;
    static final Integer d12 = (int) 0x1L;
    static final Integer d13 = '\u0000';
    static final Integer d14 = 'a';
    static final Integer d15 = computed();

    int e1 = 0; // Bad
    int e2 = 1;
    int e3 = (int) 0L; // Bad
    int e4 = (int) 2L;
    int e5 = 00; // Bad
    int e6 = 0123;
    int e7 = (int) 00L; // Bad
    int e8 = (int) 0123L;
    int e9 = 0x0; // Bad
    int e10 = 0x1;
    int e11 = (int) 0x0L; // Bad
    int e12 = (int) 0x1L;
    int e13 = '\u0000'; // Bad
    int e14 = 'a';
    int e15 = computed();

    Integer f1 = 0;
    Integer f2 = 1;
    Integer f3 = (int) 0L;
    Integer f4 = (int) 2L;
    Integer f5 = 00;
    Integer f6 = 0123;
    Integer f7 = (int) 00L;
    Integer f8 = (int) 0123L;
    Integer f9 = 0x0;
    Integer f10 = 0x1;
    Integer f11 = (int) 0x0L;
    Integer f12 = (int) 0x1L;
    Integer f13 = '\u0000';
    Integer f14 = 'a';
    Integer f15 = computed();

    static int g1 = 0; // Bad
    static int g2 = 1;
    static int g3 = (int) 0L; // Bad
    static int g4 = (int) 2L;
    static int g5 = 00; // Bad
    static int g6 = 0123;
    static int g7 = (int) 00L; // Bad
    static int g8 = (int) 0123L;
    static int g9 = 0x0; // Bad
    static int g10 = 0x1;
    static int g11 = (int) 0x0L; // Bad
    static int g12 = (int) 0x1L;
    static int g13 = '\u0000'; // Bad
    static int g14 = 'a';
    static int g15 = computed();

    static Integer h1 = 0;
    static Integer h2 = 1;
    static Integer h3 = (int) 0L;
    static Integer h4 = (int) 2L;
    static Integer h5 = 00;
    static Integer h6 = 0123;
    static Integer h7 = (int) 00L;
    static Integer h8 = (int) 0123L;
    static Integer h9 = 0x0;
    static Integer h10 = 0x1;
    static Integer h11 = (int) 0x0L;
    static Integer h12 = (int) 0x1L;
    static Integer h13 = '\u0000';
    static Integer h14 = 'a';
    static Integer h15 = computed();

    static int computed() {
        return 0;
    }
}
        
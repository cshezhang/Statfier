
public class Foo {
    final char a1 = 0;
    final char a2 = 1;
    final char a3 = (char) 0L;
    final char a4 = (char) 2L;
    final char a5 = 00;
    final char a6 = 0123;
    final char a7 = (char) 00L;
    final char a8 = (char) 0123L;
    final char a9 = 0x0;
    final char a10 = 0x1;
    final char a11 = (char) 0x0L;
    final char a12 = (char) 0x1L;
    final char a13 = '\u0000';
    final char a14 = 'a';
    final char a15 = computed();

    final Character b1 = 0;
    final Character b2 = 1;
    final Character b3 = (char) 0L;
    final Character b4 = (char) 2L;
    final Character b5 = 00;
    final Character b6 = 0123;
    final Character b7 = (char) 00L;
    final Character b8 = (char) 0123L;
    final Character b9 = 0x0;
    final Character b10 = 0x1;
    final Character b11 = (char) 0x0L;
    final Character b12 = (char) 0x1L;
    final Character b13 = '\u0000';
    final Character b14 = 'a';
    final Character b15 = computed();

    static final char c1 = 0;
    static final char c2 = 1;
    static final char c3 = (char) 0L;
    static final char c4 = (char) 2L;
    static final char c5 = 00;
    static final char c6 = 0123;
    static final char c7 = (char) 00L;
    static final char c8 = (char) 0123L;
    static final char c9 = 0x0;
    static final char c10 = 0x1;
    static final char c11 = (char) 0x0L;
    static final char c12 = (char) 0x1L;
    static final char c13 = '\u0000';
    static final char c14 = 'a';
    static final char c15 = computed();

    static final Character d1 = 0;
    static final Character d2 = 1;
    static final Character d3 = (char) 0L;
    static final Character d4 = (char) 2L;
    static final Character d5 = 00;
    static final Character d6 = 0123;
    static final Character d7 = (char) 00L;
    static final Character d8 = (char) 0123L;
    static final Character d9 = 0x0;
    static final Character d10 = 0x1;
    static final Character d11 = (char) 0x0L;
    static final Character d12 = (char) 0x1L;
    static final Character d13 = '\u0000';
    static final Character d14 = 'a';
    static final Character d15 = computed();

    char e1 = 0; // Bad
    char e2 = 1;
    char e3 = (char) 0L; // Bad
    char e4 = (char) 2L;
    char e5 = 00; // Bad
    char e6 = 0123;
    char e7 = (char) 00L; // Bad
    char e8 = (char) 0123L;
    char e9 = 0x0; // Bad
    char e10 = 0x1;
    char e11 = (char) 0x0L; // Bad
    char e12 = (char) 0x1L;
    char e13 = '\u0000'; // Bad
    char e14 = 'a';
    char e15 = computed();

    Character f1 = 0;
    Character f2 = 1;
    Character f3 = (char) 0L;
    Character f4 = (char) 2L;
    Character f5 = 00;
    Character f6 = 0123;
    Character f7 = (char) 00L;
    Character f8 = (char) 0123L;
    Character f9 = 0x0;
    Character f10 = 0x1;
    Character f11 = (char) 0x0L;
    Character f12 = (char) 0x1L;
    Character f13 = '\u0000';
    Character f14 = 'a';
    Character f15 = computed();

    static char g1 = 0; // Bad
    static char g2 = 1;
    static char g3 = (char) 0L; // Bad
    static char g4 = (char) 2L;
    static char g5 = 00; // Bad
    static char g6 = 0123;
    static char g7 = (char) 00L; // Bad
    static char g8 = (char) 0123L;
    static char g9 = 0x0; // Bad
    static char g10 = 0x1;
    static char g11 = (char) 0x0L; // Bad
    static char g12 = (char) 0x1L;
    static char g13 = '\u0000'; // Bad
    static char g14 = 'a';
    static char g15 = computed();

    static Character h1 = 0;
    static Character h2 = 1;
    static Character h3 = (char) 0L;
    static Character h4 = (char) 2L;
    static Character h5 = 00;
    static Character h6 = 0123;
    static Character h7 = (char) 00L;
    static Character h8 = (char) 0123L;
    static Character h9 = 0x0;
    static Character h10 = 0x1;
    static Character h11 = (char) 0x0L;
    static Character h12 = (char) 0x1L;
    static Character h13 = '\u0000';
    static Character h14 = 'a';
    static Character h15 = computed();

    static char computed() {
        return 0;
    }
}
        
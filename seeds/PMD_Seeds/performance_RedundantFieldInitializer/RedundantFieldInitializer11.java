
public class Foo {
    final long a1 = 0;
    final long a2 = 1;
    final long a3 = (long) 0L;
    final long a4 = (long) 2L;
    final long a5 = 00;
    final long a6 = 0123;
    final long a7 = (long) 00L;
    final long a8 = (long) 0123L;
    final long a9 = 0x0;
    final long a10 = 0x1;
    final long a11 = (long) 0x0L;
    final long a12 = (long) 0x1L;
    final long a13 = '\u0000';
    final long a14 = 'a';
    final long a15 = computed();

    final Long b1 = 0L;
    final Long b2 = 2L;
    final Long b3 = 00L;
    final Long b4 = 0123L;
    final Long b5 = 0x0L;
    final Long b6 = 0x1L;
    final Long b7 = (long) '\u0000';
    final Long b8 = (long) 'a';
    final Long b9 = computed();

    static final long c1 = 0;
    static final long c2 = 1;
    static final long c3 = (long) 0L;
    static final long c4 = (long) 2L;
    static final long c5 = 00;
    static final long c6 = 0123;
    static final long c7 = (long) 00L;
    static final long c8 = (long) 0123L;
    static final long c9 = 0x0;
    static final long c10 = 0x1;
    static final long c11 = (long) 0x0L;
    static final long c12 = (long) 0x1L;
    static final long c13 = '\u0000';
    static final long c14 = 'a';
    static final long c15 = computed();

    static final Long d1 = 0L;
    static final Long d2 = 2L;
    static final Long d3 = 00L;
    static final Long d4 = 0123L;
    static final Long d5 = 0x0L;
    static final Long d6 = 0x1L;
    static final Long d7 = (long) '\u0000';
    static final Long d8 = (long) 'a';
    static final Long d9 = computed();

    long e1 = 0; // Bad
    long e2 = 1;
    long e3 = (long) 0L; // Bad
    long e4 = (long) 2L;
    long e5 = 00; // Bad
    long e6 = 0123;
    long e7 = (long) 00L; // Bad
    long e8 = (long) 0123L;
    long e9 = 0x0; // Bad
    long e10 = 0x1;
    long e11 = (long) 0x0L; // Bad
    long e12 = (long) 0x1L;
    long e13 = '\u0000'; // Bad
    long e14 = 'a';
    long e15 = computed();

    Long f1 = 0L;
    Long f2 = 2L;
    Long f3 = 00L;
    Long f4 = 0123L;
    Long f5 = 0x0L;
    Long f6 = 0x1L;
    Long f7 = (long) '\u0000';
    Long f8 = (long) 'a';
    Long f9 = computed();

    static long g1 = 0; // Bad
    static long g2 = 1;
    static long g3 = (long) 0L; // Bad
    static long g4 = (long) 2L;
    static long g5 = 00; // Bad
    static long g6 = 0123;
    static long g7 = (long) 00L; // Bad
    static long g8 = (long) 0123L;
    static long g9 = 0x0; // Bad
    static long g10 = 0x1;
    static long g11 = (long) 0x0L; // Bad
    static long g12 = (long) 0x1L;
    static long g13 = '\u0000'; // Bad
    static long g14 = 'a';
    static long g15 = computed();

    static Long h1 = 0L;
    static Long h2 = 2L;
    static Long h3 = 00L;
    static Long h4 = 0123L;
    static Long h5 = 0x0L;
    static Long h6 = 0x1L;
    static Long h7 = (long) '\u0000';
    static Long h8 = (long) 'a';
    static Long h9 = computed();

    static long computed() {
        return 0;
    }
}
        
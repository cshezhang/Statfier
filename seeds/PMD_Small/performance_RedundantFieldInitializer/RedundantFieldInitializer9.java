
public class Foo {
    final short a1 = 0;
    final short a2 = 1;
    final short a3 = (short) 0L;
    final short a4 = (short) 2L;
    final short a5 = 00;
    final short a6 = 0123;
    final short a7 = (short) 00L;
    final short a8 = (short) 0123L;
    final short a9 = 0x0;
    final short a10 = 0x1;
    final short a11 = (short) 0x0L;
    final short a12 = (short) 0x1L;
    final short a13 = '\u0000';
    final short a14 = 'a';
    final short a15 = computed();

    final Short b1 = 0;
    final Short b2 = 1;
    final Short b3 = (short) 0L;
    final Short b4 = (short) 2L;
    final Short b5 = 00;
    final Short b6 = 0123;
    final Short b7 = (short) 00L;
    final Short b8 = (short) 0123L;
    final Short b9 = 0x0;
    final Short b10 = 0x1;
    final Short b11 = (short) 0x0L;
    final Short b12 = (short) 0x1L;
    final Short b13 = '\u0000';
    final Short b14 = 'a';
    final Short b15 = computed();

    static final short c1 = 0;
    static final short c2 = 1;
    static final short c3 = (short) 0L;
    static final short c4 = (short) 2L;
    static final short c5 = 00;
    static final short c6 = 0123;
    static final short c7 = (short) 00L;
    static final short c8 = (short) 0123L;
    static final short c9 = 0x0;
    static final short c10 = 0x1;
    static final short c11 = (short) 0x0L;
    static final short c12 = (short) 0x1L;
    static final short c13 = '\u0000';
    static final short c14 = 'a';
    static final short c15 = computed();

    static final Short d1 = 0;
    static final Short d2 = 1;
    static final Short d3 = (short) 0L;
    static final Short d4 = (short) 2L;
    static final Short d5 = 00;
    static final Short d6 = 0123;
    static final Short d7 = (short) 00L;
    static final Short d8 = (short) 0123L;
    static final Short d9 = 0x0;
    static final Short d10 = 0x1;
    static final Short d11 = (short) 0x0L;
    static final Short d12 = (short) 0x1L;
    static final Short d13 = '\u0000';
    static final Short d14 = 'a';
    static final Short d15 = computed();

    short e1 = 0; // Bad
    short e2 = 1;
    short e3 = (short) 0L; // Bad
    short e4 = (short) 2L;
    short e5 = 00; // Bad
    short e6 = 0123;
    short e7 = (short) 00L; // Bad
    short e8 = (short) 0123L;
    short e9 = 0x0; // Bad
    short e10 = 0x1;
    short e11 = (short) 0x0L; // Bad
    short e12 = (short) 0x1L;
    short e13 = '\u0000'; // Bad
    short e14 = 'a';
    short e15 = computed();

    Short f1 = 0;
    Short f2 = 1;
    Short f3 = (short) 0L;
    Short f4 = (short) 2L;
    Short f5 = 00;
    Short f6 = 0123;
    Short f7 = (short) 00L;
    Short f8 = (short) 0123L;
    Short f9 = 0x0;
    Short f10 = 0x1;
    Short f11 = (short) 0x0L;
    Short f12 = (short) 0x1L;
    Short f13 = '\u0000';
    Short f14 = 'a';
    Short f15 = computed();

    static short g1 = 0; // Bad
    static short g2 = 1;
    static short g3 = (short) 0L; // Bad
    static short g4 = (short) 2L;
    static short g5 = 00; // Bad
    static short g6 = 0123;
    static short g7 = (short) 00L; // Bad
    static short g8 = (short) 0123L;
    static short g9 = 0x0; // Bad
    static short g10 = 0x1;
    static short g11 = (short) 0x0L; // Bad
    static short g12 = (short) 0x1L;
    static short g13 = '\u0000'; // Bad
    static short g14 = 'a';
    static short g15 = computed();

    static Short h1 = 0;
    static Short h2 = 1;
    static Short h3 = (short) 0L;
    static Short h4 = (short) 2L;
    static Short h5 = 00;
    static Short h6 = 0123;
    static Short h7 = (short) 00L;
    static Short h8 = (short) 0123L;
    static Short h9 = 0x0;
    static Short h10 = 0x1;
    static Short h11 = (short) 0x0L;
    static Short h12 = (short) 0x1L;
    static Short h13 = '\u0000';
    static Short h14 = 'a';
    static Short h15 = computed();

    static short computed() {
        return 0;
    }
}
        
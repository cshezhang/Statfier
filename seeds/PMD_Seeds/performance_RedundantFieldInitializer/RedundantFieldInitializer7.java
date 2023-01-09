
public class Foo {
    final byte a1 = 0;
    final byte a2 = 1;
    final byte a3 = (byte) 0L;
    final byte a4 = (byte) 2L;
    final byte a5 = 00;
    final byte a6 = 0123;
    final byte a7 = (byte) 00L;
    final byte a8 = (byte) 0123L;
    final byte a9 = 0x0;
    final byte a10 = 0x1;
    final byte a11 = (byte) 0x0L;
    final byte a12 = (byte) 0x1L;
    final byte a13 = '\u0000';
    final byte a14 = 'a';
    final byte a15 = computed();

    final Byte b1 = 0;
    final Byte b2 = 1;
    final Byte b3 = (byte) 0L;
    final Byte b4 = (byte) 2L;
    final Byte b5 = 00;
    final Byte b6 = 0123;
    final Byte b7 = (byte) 00L;
    final Byte b8 = (byte) 0123L;
    final Byte b9 = 0x0;
    final Byte b10 = 0x1;
    final Byte b11 = (byte) 0x0L;
    final Byte b12 = (byte) 0x1L;
    final Byte b13 = '\u0000';
    final Byte b14 = 'a';
    final Byte b15 = computed();

    static final byte c1 = 0;
    static final byte c2 = 1;
    static final byte c3 = (byte) 0L;
    static final byte c4 = (byte) 2L;
    static final byte c5 = 00;
    static final byte c6 = 0123;
    static final byte c7 = (byte) 00L;
    static final byte c8 = (byte) 0123L;
    static final byte c9 = 0x0;
    static final byte c10 = 0x1;
    static final byte c11 = (byte) 0x0L;
    static final byte c12 = (byte) 0x1L;
    static final byte c13 = '\u0000';
    static final byte c14 = 'a';
    static final byte c15 = computed();

    static final Byte d1 = 0;
    static final Byte d2 = 1;
    static final Byte d3 = (byte) 0L;
    static final Byte d4 = (byte) 2L;
    static final Byte d5 = 00;
    static final Byte d6 = 0123;
    static final Byte d7 = (byte) 00L;
    static final Byte d8 = (byte) 0123L;
    static final Byte d9 = 0x0;
    static final Byte d10 = 0x1;
    static final Byte d11 = (byte) 0x0L;
    static final Byte d12 = (byte) 0x1L;
    static final Byte d13 = '\u0000';
    static final Byte d14 = 'a';
    static final Byte d15 = computed();

    byte e1 = 0; // Bad
    byte e2 = 1;
    byte e3 = (byte) 0L; // Bad
    byte e4 = (byte) 2L;
    byte e5 = 00; // Bad
    byte e6 = 0123;
    byte e7 = (byte) 00L; // Bad
    byte e8 = (byte) 0123L;
    byte e9 = 0x0; // Bad
    byte e10 = 0x1;
    byte e11 = (byte) 0x0L; // Bad
    byte e12 = (byte) 0x1L;
    byte e13 = '\u0000'; // Bad
    byte e14 = 'a';
    byte e15 = computed();

    Byte f1 = 0;
    Byte f2 = 1;
    Byte f3 = (byte) 0L;
    Byte f4 = (byte) 2L;
    Byte f5 = 00;
    Byte f6 = 0123;
    Byte f7 = (byte) 00L;
    Byte f8 = (byte) 0123L;
    Byte f9 = 0x0;
    Byte f10 = 0x1;
    Byte f11 = (byte) 0x0L;
    Byte f12 = (byte) 0x1L;
    Byte f13 = '\u0000';
    Byte f14 = 'a';
    Byte f15 = computed();

    static byte g1 = 0; // Bad
    static byte g2 = 1;
    static byte g3 = (byte) 0L; // Bad
    static byte g4 = (byte) 2L;
    static byte g5 = 00; // Bad
    static byte g6 = 0123;
    static byte g7 = (byte) 00L; // Bad
    static byte g8 = (byte) 0123L;
    static byte g9 = 0x0; // Bad
    static byte g10 = 0x1;
    static byte g11 = (byte) 0x0L; // Bad
    static byte g12 = (byte) 0x1L;
    static byte g13 = '\u0000'; // Bad
    static byte g14 = 'a';
    static byte g15 = computed();

    static Byte h1 = 0;
    static Byte h2 = 1;
    static Byte h3 = (byte) 0L;
    static Byte h4 = (byte) 2L;
    static Byte h5 = 00;
    static Byte h6 = 0123;
    static Byte h7 = (byte) 00L;
    static Byte h8 = (byte) 0123L;
    static Byte h9 = 0x0;
    static Byte h10 = 0x1;
    static Byte h11 = (byte) 0x0L;
    static Byte h12 = (byte) 0x1L;
    static Byte h13 = '\u0000';
    static Byte h14 = 'a';
    static Byte h15 = computed();

    static byte computed() {
        return 0;
    }
}
        

            public class Test {
                public void test() {
                    final BigInteger[] msg2 = new BigInteger[11];
                    msg2[0] = G1.modPow(x2, OtrCryptoEngine.MODULUS);
                    BigInteger[] res = proofKnowLog(x2, 3);
                    msg2[1] = res[0];
                    msg2[2] = res[1];
                    // etc.
                }
            }
        
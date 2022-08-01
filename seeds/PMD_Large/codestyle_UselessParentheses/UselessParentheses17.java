
class ExampleClass {
    void exampleExpressions() {
        eUseless = (a++) + b;
        eUseless = (a--) + b;
        eUseless = (++a) + b;
        eUseless = (--a) + b;
        eUseless = (+a) + b;
        eUseless = (-a) + b;
        eUseless = (~a) + b;
        eUseless = (!a) + b;
        eUseless = (a * b) + c;
        eUseless = (a / b) + c;
        eUseless = (a % b) + c;
        eUseless = (a + b) + c;
        eGood = (a - b) + c;
        eGood = (a << b) + c;
        eGood = (a >> b) + c;
        eGood = (a >>> b) + c;
        eGood = (a < b) + c;
        eGood = (a > b) + c;
        eGood = (a <= b) + c;
        eGood = (a >= b) + c;
        eGood = (a instanceof b) + c;
        eGood = (a == b) + c;
        eGood = (a != b) + c;
        eGood = (a & b) + c;
        eGood = (a ^ b) + c;
        eGood = (a | b) + c;
        eGood = (a && b) + c;
        eGood = (a || b) + c;
        eGood = (a ? b : c) + d;
    }
}
        
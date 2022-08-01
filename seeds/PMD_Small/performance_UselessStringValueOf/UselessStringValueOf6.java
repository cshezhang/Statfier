
class TestClass {

    public void testMethod() {
        char[] idFormat = {'0', '0', '0', '0', '0'};
        String abc = "1" + String.valueOf(idFormat);
        System.out.println(abc); // Output 100000
    }
}
        
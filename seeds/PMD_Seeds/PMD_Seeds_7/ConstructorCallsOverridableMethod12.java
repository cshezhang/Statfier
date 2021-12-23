
public class Test {

    public static class SeniorClass {
        public SeniorClass(){
            toString(); //may throw NullPointerException if overridden
        }
        public String toString(){
            return "IAmSeniorClass";
        }
    }
    public static class JuniorClass extends SeniorClass {
        private String name;

        public JuniorClass() {
            super(); //Automatic call leads to NullPointerException
            name = "JuniorClass";
        }
        public String toString(){
            return name.toUpperCase();
        }
    }
    public static void main (String[] args) {
        System.out.println(": "+new SeniorClass());
        System.out.println(": "+new JuniorClass());
    }
}
        
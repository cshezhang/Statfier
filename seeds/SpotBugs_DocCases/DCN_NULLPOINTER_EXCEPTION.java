public class DCN_NULLPOINTER_EXCEPTION {
    boolean hasSpace(String m) {
        try {
            String ms[] = m.split(" ");
            return names.length != 1;
        } catch (NullPointerException e) {
            return false;
        }
    }
}
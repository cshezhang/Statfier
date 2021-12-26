
public class Stack {
    private Node first;
    private int n;   // can't be made final

    public void insert(int x) {
        first = new Node(first, x);
    }

    public int size() {
        return n;
    }

    private class Node {
        Node next;
        int x;
        private Node(Node next, int x) {
            this.next = next;
            this.x = x;
            n++;  // inner class updates instance variable in outer class
        }
    }
}
        
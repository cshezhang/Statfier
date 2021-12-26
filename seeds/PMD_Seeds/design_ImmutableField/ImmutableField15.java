
public class Foo {
    private int x = 2;

    public Foo() {
        mouseListener = new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                x = e.getSource();
                super.mouseClicked(e);
            }
        };
    }
}
        
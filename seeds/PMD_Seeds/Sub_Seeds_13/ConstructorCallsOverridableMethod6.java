
public class Foo {
    public Foo(String s) {
        addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {bar();}
        });
    }
    public void bar() {}
}
        
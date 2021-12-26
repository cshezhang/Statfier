
import java.awt.*;
import javax.swing.*;

public class Foo {
    public JComponent makeUI() {
        Box box = Box.createVerticalBox();
        JTextField field = new JTextField();
        box.add(makePanel("aaa", field));
        return box;
    }
    private static JPanel makePanel(String title, JComponent c) { // this is wrongly triggered
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createTitledBorder(title));
        p.add(c);
        return p;
    }
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override public void run() {
                createAndShowGUI();
            }
        });
    }
    public static void createAndShowGUI() {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.getContentPane().add(new Foo().makeUI());
        f.setSize(320, 240);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}
        
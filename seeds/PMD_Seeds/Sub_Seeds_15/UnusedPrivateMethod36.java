
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.tree.*;

public class Bar {
    private final JTree tree = new JTree();
    public JComponent makeUI() {
        Box box = Box.createVerticalBox();
        box.add(new JButton(new AbstractAction("expand  ") {
            @Override public void actionPerformed(ActionEvent e) {
                TreeNode root = (TreeNode) tree.getModel().getRoot();
                visitAll(tree, new TreePath(root), true); // line 14
            }
        }));
        box.add(new JButton(new AbstractAction("collapse") {
            @Override public void actionPerformed(ActionEvent e) {
                TreeNode root = (TreeNode) tree.getModel().getRoot();
                visitAll(tree, new TreePath(root), false); // line 20
            }
        }));
        box.add(Box.createVerticalGlue());

        JPanel p = new JPanel(new BorderLayout());
        p.add(box, BorderLayout.EAST);
        p.add(new JScrollPane(tree));
        return p;
    }
    private static void visitAll(JTree tree, TreePath parent, boolean expand) {
        TreeNode node = (TreeNode) parent.getLastPathComponent();
        if (!node.isLeaf() && node.getChildCount() >= 0) {
            Enumeration e = node.children();
            while (e.hasMoreElements()) {
                TreeNode n = (TreeNode) e.nextElement();
                TreePath path = parent.pathByAddingChild(n);
                visitAll(tree, path, expand); // line 37
            }
        }
        if (expand) {
            tree.expandPath(parent);
        } else {
            tree.collapsePath(parent);
        }
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
        f.getContentPane().add(new Bar().makeUI());
        f.setSize(320, 240);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}
        
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class RESOURCE_LEAK {

    //  Standard Idiom
    public static void foo1() throws IOException {
        FileOutputStream fos = new FileOutputStream(new File("whatever.txt"));
        try {
            fos.write(7);
        } finally {
            fos.close();
        }
    }
// and you should use the standard idiom for the most part, when you don't want to return the resource to the surrounding context.

// Sometimes people just leave out close(), and that is a bug, but more typically exceptional paths are the root of the problem, as in

    // leak because of exception
    public static void foo2() throws IOException {
        FileOutputStream fos = new FileOutputStream(new File("whatever.txt"));
        fos.write(7);   //DOH! What if exception?
        fos.close();
    }
// where an exception in fos.write will cause execution to skip past the close() statement.

// Multiple Resources Bugs#
// We can deal with multiple resources correctly and simply just by nesting the standard idiom.

    // Two Resources nested
    public static void foo3() throws IOException {
        FileInputStream fis = new FileInputStream(new File("whatever.txt"));
        try {
            FileOutputStream fos = new FileOutputStream(new File("everwhat.txt"));
            try {
                fos.write(fis.read());
            } finally {
                fos.close();
            }
        } finally {
            fis.close();
        }
    }
// Bugs often occur when using multiple resources in other ways because of exceptions in close() methods. For example,

    // Classic Two Resources Bug
    public static void foo4() throws IOException {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            fis = new FileInputStream(new File("whatever.txt"));
            fos = new FileOutputStream(new File("everwhat.txt"));
            fos.write(fis.read());
        } finally {
            if (fis != null) fis.close();
            if (fos != null) fos.close();
        }
    }
// Here, if there is an exception in the call to fis.close() execution will skip past fos.close(); a leak.

// Another way, besides the standard idiom, to deal with this problem is to swallow exceptions.

    // Two Resources Fix 1
    public static void foo5() throws IOException {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            fis = new FileInputStream(new File("whatever.txt"));
            fos = new FileOutputStream(new File("everwhat.txt"));
            fos.write(fis.read());
        } finally {
            try {
                if (fis != null) fis.close();
            } catch (Exception e) {
            }
            ;  // Exception swallowing
            if (fos != null) fos.close();
        }
    }

    public static void foo6() throws IOException {
        try (
                FileInputStream fis = new FileInputStream(new File("whatever.txt"));
                FileOutputStream fos = new FileOutputStream(new File("everwhat.txt"))
        ) {
            fos.write(fis.read());
        }
    }

    public static void foo() throws IOException {
        try (
                FileInputStream fis = new FileInputStream(new File("whatever.txt"));
                FileOutputStream fos = new FileOutputStream(new File("everwhat.txt"))
        ) {
            fos.write(fis.read());
        }
    }

}

import java.io.IOException;
import static org.objectweb.asm.Opcodes.*;
import org.objectweb.asm.ClassWriter; 
import org.objectweb.asm.FieldVisitor; 
import org.objectweb.asm.MethodVisitor;

public class Dumper {
    private static final String VERSION = "version";
    private static final String CLASS = "Dumper";

    public static byte[] dump() throws IOException {
        // ClassWriter is a class visitor that generates the code for the class
        ClassWriter cw = new ClassWriter(0);
        // Start creating the class.
        cw.visit(V11, ACC_PUBLIC + ACC_SUPER, CLASS, null, "java/lang/Object", null);
        FieldVisitor fv; // false positive
        MethodVisitor mv; // false positive
        {
            // version field
            fv = cw.visitField(ACC_PRIVATE, VERSION, "I", null, null);
            fv.visitEnd();
        }
        {
            // Implementing the constructor
            mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
            mv.visitInsn(RETURN);
            mv.visitMaxs(1, 1);
            mv.visitEnd();
        }
        cw.visitEnd();

        return cw.toByteArray();
    }
}
        
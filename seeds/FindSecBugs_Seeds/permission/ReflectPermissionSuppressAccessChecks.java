package testcode.permission;

import java.lang.reflect.ReflectPermission;
import java.security.CodeSource;
import java.security.PermissionCollection;
import java.security.SecureClassLoader;

public class ReflectPermissionSuppressAccessChecks extends SecureClassLoader {
    @Override
    protected PermissionCollection getPermissions(CodeSource cs) {
        PermissionCollection pc = super.getPermissions(cs);
        pc.add(new ReflectPermission("suppressAccessChecks"));
        // Other permissions
        return pc;
    }
}


    public void setPermissionsSafe(String filePath) throws IOException {
        Set<PosixFilePermission> perms = new HashSet<PosixFilePermission>();
        // user permission
        perms.add(PosixFilePermission.OWNER_READ);
        perms.add(PosixFilePermission.OWNER_WRITE);
        perms.add(PosixFilePermission.OWNER_EXECUTE);
        // group permissions
        perms.add(PosixFilePermission.GROUP_READ);
        perms.add(PosixFilePermission.GROUP_EXECUTE);
        // others permissions removed
        perms.remove(PosixFilePermission.OTHERS_READ); // Compliant
        perms.remove(PosixFilePermission.OTHERS_WRITE); // Compliant
        perms.remove(PosixFilePermission.OTHERS_EXECUTE); // Compliant

        Files.setPosixFilePermissions(Paths.get(filePath), perms);
    }

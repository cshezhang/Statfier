
    public void setPermissions(String filePath) {
        Set<PosixFilePermission> perms = new HashSet<PosixFilePermission>();
        // user permission
        perms.add(PosixFilePermission.OWNER_READ);
        perms.add(PosixFilePermission.OWNER_WRITE);
        perms.add(PosixFilePermission.OWNER_EXECUTE);
        // group permissions
        perms.add(PosixFilePermission.GROUP_READ);
        perms.add(PosixFilePermission.GROUP_EXECUTE);
        // others permissions
        perms.add(PosixFilePermission.OTHERS_READ); // Sensitive
        perms.add(PosixFilePermission.OTHERS_WRITE); // Sensitive
        perms.add(PosixFilePermission.OTHERS_EXECUTE); // Sensitive

        Files.setPosixFilePermissions(Paths.get(filePath), perms);
    }

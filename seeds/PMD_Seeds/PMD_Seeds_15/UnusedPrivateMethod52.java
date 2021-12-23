
public class UnusedPrivateMethod {
    public void doSomething() {
        File[] files = getFiles();
        for (int i = 0; i < files.length; i++) {
            String name = getNameFromFilename(files[i].getName());
            System.out.println("name is " + name);
        }
    }

    private String getNameFromFilename(String fileName) {
        int index = fileName.lastIndexOf('.');
        return fileName.substring(0, index);
    }
}
        
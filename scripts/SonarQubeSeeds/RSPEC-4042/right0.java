
public void cleanUp(Path path) throws NoSuchFileException, DirectoryNotEmptyException, IOException {
  Files.delete(path);
}

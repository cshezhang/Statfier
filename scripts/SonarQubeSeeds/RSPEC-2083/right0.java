
protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    String file = request.getParameter("file");

    File fileUnsafe = new File(file);
    File directory = new File("/tmp/");

    try {
      if(FileUtils.directoryContains(directory, fileUnsafe)) {
        FileUtils.forceDelete(fileUnsafe); // Compliant
      }
    }
    catch(IOException ex){
      System.out.println (ex.toString());
    }
}

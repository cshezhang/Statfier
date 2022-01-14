
String myTextBlock = """
    <project>
      <modelVersion>4.0.0</modelVersion>
      <parent>
        <groupId>com.mycompany.app</groupId>
        <artifactId>my-app</artifactId>
        <version>1</version>
      </parent>

      <groupId>com.mycompany.app</groupId>
      <artifactId>my-module</artifactId>
      <version>1</version>
    </project>
    """;

listOfString.stream()
  .map(str -> !myTextBlock.equals(str));

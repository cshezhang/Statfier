
public void handle(String command){
  command.toLowerCase(); // Noncompliant; result of method thrown away
  ...
}

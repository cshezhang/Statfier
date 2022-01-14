
public class Employees {
  private HashSet<Employee> employees = new HashSet<Employee>();  // Noncompliant - "employees" should have type "Set" rather than "HashSet"

  public HashSet<Employee> getEmployees() {                       // Noncompliant
    return employees;
  }
}

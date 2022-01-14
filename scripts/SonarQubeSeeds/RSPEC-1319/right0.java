
public class Employees {
  private Set<Employee> employees = new HashSet<Employee>();      // Compliant

  public Set<Employee> getEmployees() {                           // Compliant
    return employees;
  }
}

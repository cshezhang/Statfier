
public class Address {
  //...
}

//...
HttpSession session = request.getSession();
session.setAttribute("address", new Address());  // Noncompliant; Address isn't serializable

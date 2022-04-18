
            
public class Foo {
  // Has a cognitive complexity of 0
  public void createAccount() {
    Account account = new Account("PMD");
    // save account
  }

  // Has a cognitive complexity of 1
  public Boolean setPhoneNumberIfNotExisting(Account a, String phone) {
    if (a.phone == null) {                          // +1
      a.phone = phone;
      return true;
    }

    return false;
  }

  // Has a cognitive complexity of 4
  public void updateContacts(List<Contact> contacts) {
    List<Contact> contactsToUpdate = new ArrayList<Contact>();

    for (Contact contact : contacts) {                           // +1
      if (contact.department.equals("Finance")) {                // +2 (nesting = 1)
        contact.title = "Finance Specialist";
        contactsToUpdate.add(contact);
      } else if (contact.department.equals("Sales")) {           // +1
        contact.title = "Sales Specialist";
        contactsToUpdate.add(contact);
      }
    }
    // save contacts
  }
}            
        
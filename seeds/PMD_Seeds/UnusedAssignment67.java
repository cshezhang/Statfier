
package iter0;
class Foo {

    public java.lang.Object getSubject() {
      try {
        java.lang.Object subject = Other.currentUserMethod.invoke();
        if (subject == null) {
          subject = Other.anonymousSubjectMethod.invoke(0);
        }
        return subject;
      } catch (Exception ex) {
        throw new RuntimeException("Failed to obtain SubjectHandle", ex);
      }
    }

}
        
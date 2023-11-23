import java.io.*;
import javax.persistence.Entity;
import javax.persistence.Id;
import org.apache.commons.io.input.ClassLoaderObjectInputStream;

public class ObjectDeserialization {

  public UserEntity deserializeObject(InputStream receivedFile)
      throws IOException, ClassNotFoundException {
    ObjectInputStream in = new ObjectInputStream(receivedFile);
    try {
      return (UserEntity) in.readObject();
    } finally {
      in.close();
    }
  }

  public UserEntity deserializeObjectWithInheritance(InputStream receivedFile)
      throws IOException, ClassNotFoundException {
    ClassLoaderObjectInputStream in =
        new ClassLoaderObjectInputStream(getClass().getClassLoader(), receivedFile);
    try {
      return (UserEntity) in.readObject();
    } finally {
      in.close();
    }
  }
}

@Entity
class UserEntity {
  @Id private Long id;
  private String test;

  public String getTest() {
    return test;
  }

  public void setTest(String test) {
    this.test = test;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
}

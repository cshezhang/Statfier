import javax.persistence.Entity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface SpringEntityLeakControllerInterface {
  @RequestMapping("/api1")
  SampleEntity api1(@RequestParam("url") String url);
}

@Entity
class SampleEntity {
  private String test;

  public SampleEntity(String test) {
    this.test = test;
  }

  public String getTest() {
    return test;
  }

  public void setTest(String test) {
    this.test = test;
  }
}

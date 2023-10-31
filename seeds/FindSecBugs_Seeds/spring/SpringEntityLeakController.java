import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;
import javax.persistence.Entity;

@Controller
public class SpringEntityLeakController {
	@RequestMapping("/api1")
	public SampleEntity api1(@RequestParam("url") String url) {
		return new SampleEntity("entity1");
	}

	@RequestMapping("/api2")
	public SampleEntityTwo api2(@RequestParam("url") String url) {
		return new SampleEntityTwo("entity1");
	}

	@RequestMapping("/api3")
	public void api3(SampleEntity sampleEntity) {
		if (sampleEntity.getTest().equals("test"))
			return;
	}

	@RequestMapping("/api4")
	public List<SampleEntity> api4(@RequestParam("active") String active) {
        if (active.equals("enable")) {
            return getData();
        }
        return null;
	}

    @RequestMapping("/api5")
    public void api5(@RequestParam("active") List<SampleEntity> entities) {
    }

    @RequestMapping("/api6")
    public void api6(SampleEntity sampleEntity, Object secondParameter) {
    }

    @RequestMapping("/api7")
    public String[] api7(Double[] firstParameter, List<SampleEntity>[] sampleEntities) {
        return new String[0];
    }

	private List<SampleEntity> getData() { //FP (No request mapping annotation)
	    return null;
    }
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

class SampleEntityTwo extends SampleEntity{
	private String test;

	public SampleEntityTwo(String test) {
		super(test);
		this.test = test;
	}

	public String getTest() {
		return test;
	}

	public void setTest(String test) {
		this.test = test;
	}
}
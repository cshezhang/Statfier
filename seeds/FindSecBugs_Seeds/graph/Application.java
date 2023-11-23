import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

@SpringBootApplication
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Bean
  public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
    return args -> {
      try {
        System.out.println("Loading ...");

        String configXml = new String(Files.readAllBytes(Paths.get("/test")));
        XmlService.receiveXMLStream(configXml);
      } catch (Exception e) {
      }
    };
  }
}

class XmlService {
  public static void receiveXMLStream(final String xml)
      throws ParserConfigurationException, SAXException, IOException {
    // ...
    InputStream inStream = new ByteArrayInputStream(xml.getBytes());
    SAXParserFactory spf = SAXParserFactory.newInstance();
    final SAXParser saxParser = spf.newSAXParser();
    saxParser.parse(inStream, new DefaultHandler());
  }
}

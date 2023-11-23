import java.io.*;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CWE113_HTTP_Response_Splitting__File_addCookieServlet_12 {

  public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable {
    String data;
    if (IO.staticReturnsTrueOrFalse()) {
      data = ""; /* Initialize data */
      {
        File file = new File("C:\\data.txt");
        FileInputStream streamFileInput = null;
        InputStreamReader readerInputStream = null;
        BufferedReader readerBuffered = null;
        try {
          /* read string from file into data */
          streamFileInput = new FileInputStream(file);
          readerInputStream = new InputStreamReader(streamFileInput, "UTF-8");
          readerBuffered = new BufferedReader(readerInputStream);
          /* POTENTIAL FLAW: Read data from a file */
          /* This will be reading the first "line" of the file, which
           * could be very long if there are little or no newlines in the file */
          data = readerBuffered.readLine();
        } catch (IOException exceptIO) {
          IO.logger.log(Level.WARNING, "Error with stream reading", exceptIO);
        } finally {
          /* Close stream reading objects */
          try {
            if (readerBuffered != null) {
              readerBuffered.close();
            }
          } catch (IOException exceptIO) {
            IO.logger.log(Level.WARNING, "Error closing BufferedReader", exceptIO);
          }

          try {
            if (readerInputStream != null) {
              readerInputStream.close();
            }
          } catch (IOException exceptIO) {
            IO.logger.log(Level.WARNING, "Error closing InputStreamReader", exceptIO);
          }

          try {
            if (streamFileInput != null) {
              streamFileInput.close();
            }
          } catch (IOException exceptIO) {
            IO.logger.log(Level.WARNING, "Error closing FileInputStream", exceptIO);
          }
        }
      }
    } else {

      /* FIX: Use a hardcoded string */
      data = "foo";
    }

    if (IO.staticReturnsTrueOrFalse()) {
      if (data != null) {
        Cookie cookieSink = new Cookie("lang", data);
        /* POTENTIAL FLAW: Input not verified before inclusion in the cookie */
        response.addCookie(cookieSink);
      }
    } else {

      if (data != null) {
        Cookie cookieSink = new Cookie("lang", URLEncoder.encode(data, "UTF-8"));
        /* FIX: use URLEncoder.encode to hex-encode non-alphanumerics */
        response.addCookie(cookieSink);
      }
    }
  }
}

class IO {
  public static final Logger logger = Logger.getLogger("testcases");

  public static void writeString(String str) {
    System.out.print(str);
  }

  public static void writeLine(String line) {
    System.out.println(line);
  }

  public static void writeLine(int intNumber) {
    writeLine(String.format("%02d", intNumber));
  }

  public static void writeLine(long longNumber) {
    writeLine(String.format("%02d", longNumber));
  }

  public static void writeLine(double doubleNumber) {
    writeLine(String.format("%02f", doubleNumber));
  }

  public static void writeLine(float floatNumber) {
    writeLine(String.format("%02f", floatNumber));
  }

  public static void writeLine(short shortNumber) {
    writeLine(String.format("%02d", shortNumber));
  }

  public static void writeLine(byte byteHex) {
    writeLine(String.format("%02x", byteHex));
  }

  public static boolean staticReturnsTrueOrFalse() {
    return (new java.util.Random()).nextBoolean();
  }
}

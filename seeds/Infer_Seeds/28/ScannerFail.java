import java.util.Scanner;

abstract class ScannerFail {
  void readOk() {
    Scanner scan = getScanner();
    while (scan.hasNext()) {
      scan.next();
    }
  }

  void readBad() {
    Scanner scan = getScanner();
    scan.next();
  }

  Scanner getScanner() {
    return new Scanner(System.in);
  }
}

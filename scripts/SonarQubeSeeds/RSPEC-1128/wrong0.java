
package my.company;

import java.lang.String;        // Noncompliant; java.lang classes are always implicitly imported
import my.company.SomeClass;    // Noncompliant; same-package files are always implicitly imported
import java.io.File;            // Noncompliant; File is not used

import my.company2.SomeType;
import my.company2.SomeType;    // Noncompliant; 'SomeType' is already imported

class ExampleClass {

  public String someString;
  public SomeType something;

}


package com.mycompany.app;

@Configuration
@ComponentScan("com.mycompany.app.beans")
public class Application {
...
}

package com.mycompany.app.web;

@Controller
public class MyController { // Noncompliant; MyController belong to "com.mycompany.app.web" while the ComponentScan is looking for beans in "com.mycompany.app.beans" package
...
}

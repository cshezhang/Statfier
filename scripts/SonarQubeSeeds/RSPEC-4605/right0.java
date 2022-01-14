
package com.mycompany.app;

@Configuration
@ComponentScan({"com.mycompany.app.beans","com.mycompany.app.web"})
or
@ComponentScan("com.mycompany.app")
or
@ComponentScan
public class Application {
...
}

package com.mycompany.app.web;

@Controller
public class MyController { // "com.mycompany.app.web" is referenced by a @ComponentScan annotated class
...
}

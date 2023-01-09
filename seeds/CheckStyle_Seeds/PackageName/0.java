

package com; // OK
package COM; // violation, name 'COM' must match pattern '^[a-z]+(\.[a-zA-Z_][a-zA-Z0-9_]*)*$'
package com.checkstyle.checks; // OK
package com.A.checkstyle.checks; // OK
package com.checkstyle1.checks; // OK
package com.checkSTYLE.checks; // OK
package com._checkstyle.checks_; // OK
        
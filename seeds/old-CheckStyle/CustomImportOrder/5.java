

package com.company;

import static java.util.*; // OK
import static java.io.*; // OK

import java.time.*; // OK
import javax.net.*; // OK
import org.apache.commons.io.FileUtils; // violation
import com.puppycrawl.tools.checkstyle.checks.imports.CustomImportOrderCheck; // violation
import com.puppycrawl.tools.checkstyle.checks.imports.ImportOrderCheck; // OK
        
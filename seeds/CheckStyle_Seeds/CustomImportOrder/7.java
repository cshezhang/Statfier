

package com.company;

import static java.util.*; // OK
import static java.io.*; // Violation since it should come before"java.util"

import java.time.*; // OK
import javax.net.*; // OK
import org.apache.commons.io.FileUtils; // Violation should be separated by space

import com.puppycrawl.tools.checkstyle.checks.imports.CustomImportOrderCheck; // OK
import com.puppycrawl.tools.checkstyle.checks.imports.ImportOrderCheck; // OK
        
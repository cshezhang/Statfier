/*
CustomImportOrder
customImportOrderRules = STATIC###STANDARD_JAVA_PACKAGE
standardPackageRegExp = (default)^(java|javax)\.
thirdPartyPackageRegExp = (default).*
specialImportsRegExp = (default)^$
separateLineBetweenGroups = false
sortImportsInGroupAlphabetically = true


*/

package com.puppycrawl.tools.checkstyle.checks.imports.customimportorder;

import static javax.swing.WindowConstants.*;
// violation

import com.puppycrawl.tools.checkstyle.*; // violation
import com.puppycrawl.tools.checkstyle.checks.*;
import java.util.*;
import org.apache.commons.beanutils.*;

public class InputCustomImportOrder_NoSeparator {}


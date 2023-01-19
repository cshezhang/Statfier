/*
ImportOrder
option = inflow
groups = (default)
ordered = (default)true
separated = (default)false
separatedStaticGroups = (default)false
caseSensitive = (default)true
staticGroups = (default)
sortStaticImportsAlphabetically = (default)false
useContainerOrderingForStatic = (default)false
tokens = (default)STATIC_IMPORT


*/

package com.puppycrawl.tools.checkstyle.checks.imports.importorder;

// ok
// ok
// ok
// violation
// ok

// violation
// ok
// violation
import static javax.swing.WindowConstants.*; // violation
// violation

// 2 violations
// violation
// ok
// ok
// ok

public class InputImportOrder_InFlow {}


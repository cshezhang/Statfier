/*
ImportOrder
option = (default)under
groups = java, javax, org
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

public class InputImportOrder_WildcardUnspecified {}


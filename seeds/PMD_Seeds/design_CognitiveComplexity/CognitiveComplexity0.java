public class Foo {
  @Nullable
  private MethodJavaSymbol overriddenSymbolFrom(ClassJavaType classType) {
    if (classType.isUnknown()) { // +1
      return Symbols.unknownMethodSymbol;
    }
    boolean unknownFound = false;
    List<JavaSymbol> symbols = classType.getSymbol().members().lookup(name);
    for (JavaSymbol overrideSymbol : symbols) { // +1
      if (overrideSymbol.isKind(JavaSymbol.MTH) // +2 (nesting = 1)
          && !overrideSymbol.isStatic()) { // +1
        MethodJavaSymbol methodJavaSymbol = (MethodJavaSymbol) overrideSymbol;
        if (canOverride(methodJavaSymbol)) { // +3 (nesting = 2)
          Boolean overriding = checkOverridingParameters(methodJavaSymbol, classType);
          if (overriding == null) { // +4 (nesting = 3)
            if (!unknownFound) { // +5 (nesting = 4)
              unknownFound = true;
            }
          } else if (overriding) { // +1
            return methodJavaSymbol;
          }
        }
      }
    }
    if (unknownFound) { // +1
      return Symbols.unknownMethodSymbol;
    }
    return null;
  } // total complexity = 19
}


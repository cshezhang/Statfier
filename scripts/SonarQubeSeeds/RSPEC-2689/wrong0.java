
FileOutputStream fos = new FileOutputStream (fileName , true);  // fos opened in append mode
ObjectOutputStream out = new ObjectOutputStream(fos);  // Noncompliant

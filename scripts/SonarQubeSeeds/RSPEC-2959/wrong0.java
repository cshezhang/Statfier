
try (ByteArrayInputStream b = new ByteArrayInputStream(new byte[10]);  // ignored; this one's required
      Reader r = new InputStreamReader(b);)   // Noncompliant
{
   //do stuff
}

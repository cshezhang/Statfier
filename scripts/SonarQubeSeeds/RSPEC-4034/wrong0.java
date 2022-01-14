
boolean hasRed = widgets.stream().filter(w -> w.getColor() == RED).findFirst().isPresent(); // Noncompliant

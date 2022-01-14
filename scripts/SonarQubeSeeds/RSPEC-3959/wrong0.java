
Stream<Widget> pipeline = widgets.stream().filter(b -> b.getColor() == RED);
int sum1 = pipeline.sum();
int sum2 = pipeline.mapToInt(b -> b.getWeight()).sum(); // Noncompliant

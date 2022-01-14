
int sum = widgets.stream()
                      .filter(b -> b.getColor() == RED)
                      .mapToInt(b -> b.getWeight())
                      .sum();
Stream<Widget> pipeline = widgets.stream()
                                 .filter(b -> b.getColor() == GREEN)
                                 .mapToInt(b -> b.getWeight());
sum = pipeline.sum();

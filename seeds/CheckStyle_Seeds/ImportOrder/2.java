// OK, alphabetical case sensitive ASCII order, 'i' < 'l'
// OK, follows property 'Option' value 'above'
// violation, alphabetical case sensitive ASCII order, 'i' < 'l'

// violation, extra separation in 'java' import group

import static javax.WindowConstants.*; // violation, wrong order, 'javax' comes before 'org'
// violation, must separate from previous import
// OK

import org.albedo.*;

// OK
// OK

public class SomeClass {}


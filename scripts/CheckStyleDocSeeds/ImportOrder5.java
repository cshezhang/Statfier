

import static java.lang.Math.PI;
import static java.lang.Math.abs; // OK, alphabetical case sensitive ASCII order, 'P' < 'a'
import static org.abego.treelayout.Configuration.AlignmentInLevel; // OK, alphabetical order

import java.util.Set; // violation, extra separation in import group
import static java.lang.Math.abs; // violation, wrong order, all static imports comes at 'top'
import org.abego.*;

public class SomeClass { }
        
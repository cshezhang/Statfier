
            package net.sourceforge.pmd.lang.java.rule.design.singularfield;

            public class Issue3303 {

                // "private" is a must for reproducing the problem
                private final NoThrowingCloseable first;

                Issue3303(NoThrowingCloseable first) {
                    this.first = first;
                }

                public void performClosing() {
                    try (first) {
                        // this block can be empty or not
                    }
                }
            }
            
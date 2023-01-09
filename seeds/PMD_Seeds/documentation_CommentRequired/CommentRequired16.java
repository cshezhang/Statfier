
package de.konsens.biene.ka.client;

/**
 * Kommentar zu PmdMissingHeaderCommentTest
 */
public class PmdMissingHeaderCommentTest {

    /**
     * Kommentar zu methode1()
     */
    public void methode1() {

        /**
        * Kommentar zu Local
        */
        class Local {

            /**
             * Kommentar zu methode1()
             */
            void methode1() {

            }
        }

        Local local = new Local();
        local.methode1();

        /**
         * Kommentar zu Local2
         */
        class Local2 {

            /**
             * Kommentar zu methode1()
             */
            void methode1() {
            }
        }

        Local2 local2 = new Local2();
        local2.methode1();
    }
}
        
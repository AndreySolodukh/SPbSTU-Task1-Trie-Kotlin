package tasks

import org.junit.Assert.*
import org.junit.Test
import tasks.Trie.*

class Tests {

    private val trie = Trie(setOf(
            Node('D', setOf(Node('o', setOf(Node('g', setOf()))))),
            Node('C', setOf(Node('a', setOf(Node('t', setOf())))))
    ))

    @Test
    fun tests() {

        trie.add("One")
        assertEquals(Trie(setOf(
                Node('D', setOf(Node('o', setOf(Node('g', setOf()))))),
                Node('C', setOf(Node('a', setOf(Node('t', setOf()))))),
                Node('O', setOf(Node('n', setOf(Node('e', setOf()))))) // <<<< added
        )), trie)

        trie.add("Calc")
        assertEquals(Trie(setOf(
                Node('D', setOf(Node('o', setOf(Node('g', setOf()))))),
                Node('O', setOf(Node('n', setOf(Node('e', setOf()))))),
                Node('C', setOf(Node('a', setOf(Node('t', setOf()),
                        Node('l', setOf(Node('c', setOf())))))))    // <<<< added

        )), trie)

        trie.add("Doge")
        assertEquals(Trie(setOf(
                Node('O', setOf(Node('n', setOf(Node('e', setOf()))))),
                Node('C', setOf(Node('a', setOf(Node('t', setOf()),
                        Node('l', setOf(Node('c', setOf()))))))),
                Node('D', setOf(Node('o', setOf(Node('g', setOf(Node('e', setOf()))))))) // <<<<
        )), trie)

        trie.add("Cat")
        assertEquals(Trie(setOf(
                Node('O', setOf(Node('n', setOf(Node('e', setOf()))))),
                Node('C', setOf(Node('a', setOf(Node('t', setOf()),
                        Node('l', setOf(Node('c', setOf()))))))),
                Node('D', setOf(Node('o', setOf(Node('g', setOf(Node('e', setOf())))))))
        )), trie)
        // No changes


        //assertTrue(trie.add("Calculate")) - changes trie, add as last check or in a different fun if needed
        assertFalse(trie.add("Doge"))
        assertFalse(trie.add(""))

        trie.remove("One")
        assertEquals(Trie(setOf(
                // Node('O', setOf(Node('n', setOf(Node('e', setOf()))))),      <<<< removed
                Node('C', setOf(Node('a', setOf(Node('t', setOf()),
                        Node('l', setOf(Node('c', setOf()))))))),
                Node('D', setOf(Node('o', setOf(Node('g', setOf(Node('e', setOf())))))))
        )), trie)

        trie.remove("Dog")
        assertEquals(Trie(setOf(
                Node('C', setOf(Node('a', setOf(Node('t', setOf()),
                        Node('l', setOf(Node('c', setOf()))))))),
                Node('D', setOf(Node('o', setOf(Node('g', setOf(Node('e', setOf())))))))
        )), trie)
        // No changes.

        trie.add("Call")
        trie.add("Cale")
        trie.add("Calv")
        // Just preparing

        trie.remove("Calc")
        assertEquals(Trie(setOf(
                Node('D', setOf(Node('o', setOf(Node('g', setOf(Node('e', setOf()))))))),
                Node('C', setOf(Node('a', setOf(Node('t', setOf()),
                        Node('l', setOf(Node('l', setOf()), Node('e', setOf()),
                                Node('v', setOf()))))))) // includes "Call", "Cale", "Calv", but not "Calc"
        )), trie)

        assertTrue(trie.find("Doge"))
        assertTrue(trie.find("Ca"))
        assertFalse(trie.find("Grail"))

        assertEquals(setOf("Doge"), trie.findPrefix("Dog"))
        assertEquals(setOf<String>(), trie.findPrefix("Detonator"))
        assertEquals(setOf("Call", "Cale", "Calv"), trie.findPrefix("Cal"))
        assertEquals(setOf("Cat", "Call", "Cale", "Calv"), trie.findPrefix("C"))
    }

    @Test
    fun effectivity() {
        for (i in 0..10000) trie.add("OMAE_WA_MOU_SHINDEIRU_$i")
        for (i in 0..10000 step 2) trie.remove("OMAE_WA_MOU_SHINDEIRU_$i")
        assertEquals(setOf(
                "OMAE_WA_MOU_SHINDEIRU_9621",
                "OMAE_WA_MOU_SHINDEIRU_9623",
                "OMAE_WA_MOU_SHINDEIRU_9625",
                "OMAE_WA_MOU_SHINDEIRU_9627",
                "OMAE_WA_MOU_SHINDEIRU_9629"
        ), trie.findPrefix("OMAE_WA_MOU_SHINDEIRU_962"))
    } // Almost 22 secs. Wow.

}
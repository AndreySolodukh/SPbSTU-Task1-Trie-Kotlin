package tasks

import org.junit.Assert.*
import org.junit.Test
import tasks.Trie.*

class Tests {


    @Test
    fun testadd() {

        val trie = Trie(setOf(
                Node('D', setOf(Node('o', setOf(Node('g', setOf()))))),
                Node('C', setOf(Node('a', setOf(Node('t', setOf())))))
        )) // (Dog, Cat)

        trie.add("One")
        assertEquals(Trie(setOf(
                Node('D', setOf(Node('o', setOf(Node('g', setOf()))))),
                Node('C', setOf(Node('a', setOf(Node('t', setOf()))))),
                Node('O', setOf(Node('n', setOf(Node('e', setOf()))))) // <<<< added
        )), trie) // (Dog, Cat, One)

        trie.add("Calc")
        assertEquals(Trie(setOf(
                Node('D', setOf(Node('o', setOf(Node('g', setOf()))))),
                Node('O', setOf(Node('n', setOf(Node('e', setOf()))))),
                Node('C', setOf(Node('a', setOf(Node('t', setOf()),
                        Node('l', setOf(Node('c', setOf())))))))    // <<<< added

        )), trie) // (Dog, One, Cat, Calc)

        trie.add("Doge")
        assertEquals(Trie(setOf(
                Node('O', setOf(Node('n', setOf(Node('e', setOf()))))),
                Node('C', setOf(Node('a', setOf(Node('t', setOf()),
                        Node('l', setOf(Node('c', setOf()))))))),
                Node('D', setOf(Node('o', setOf(Node('g', setOf(Node('e', setOf()))))))) // <<<<
        )), trie) // (Doge, One, Cat, Calc)

        trie.add("Cat")
        assertEquals(Trie(setOf(
                Node('O', setOf(Node('n', setOf(Node('e', setOf()))))),
                Node('C', setOf(Node('a', setOf(Node('t', setOf()),
                        Node('l', setOf(Node('c', setOf()))))))),
                Node('D', setOf(Node('o', setOf(Node('g', setOf(Node('e', setOf())))))))
        )), trie)  // No changes

        assertTrue(trie.add("Calculate")) // can be added
        assertFalse(trie.add("Doge")) // cannot be added
        assertFalse(trie.add("")) // cannot add empty string
    }


    @Test
    fun testremove() {

        val trie = Trie(setOf(
                Node('O', setOf(Node('n', setOf(Node('e', setOf()))))),
                Node('C', setOf(Node('a', setOf(Node('t', setOf()),
                        Node('l', setOf(Node('c', setOf()), Node('l', setOf()),
                                Node('e', setOf()), Node('v', setOf()))))))),
                Node('D', setOf(Node('o', setOf(Node('g', setOf(Node('e', setOf())))))))
        )) // (One, Cat, Calc, Call, Cale, Calv, Doge)

        trie.remove("One")
        assertEquals(Trie(setOf(
                // Node('O', setOf(Node('n', setOf(Node('e', setOf()))))),      <<<< removed
                Node('C', setOf(Node('a', setOf(Node('t', setOf()),
                        Node('l', setOf(Node('c', setOf()), Node('l', setOf()),
                                Node('e', setOf()), Node('v', setOf()))))))),
                Node('D', setOf(Node('o', setOf(Node('g', setOf(Node('e', setOf())))))))
        )), trie) // (Cat, Calc, Call, Cale, Calv, Doge)

        trie.remove("Dog")
        assertEquals(Trie(setOf(
                Node('C', setOf(Node('a', setOf(Node('t', setOf()),
                        Node('l', setOf(Node('c', setOf()), Node('l', setOf()),
                                Node('e', setOf()), Node('v', setOf()))))))),
                Node('D', setOf(Node('o', setOf(Node('g', setOf(Node('e', setOf())))))))
        )), trie) // No changes.

        trie.remove("Calc")
        assertEquals(Trie(setOf(
                Node('D', setOf(Node('o', setOf(Node('g', setOf(Node('e', setOf()))))))),
                Node('C', setOf(Node('a', setOf(Node('t', setOf()),
                        Node('l', setOf( /* Node('c', setOf()), */ Node('l', setOf()), // <<<< removed
                                Node('e', setOf()), Node('v', setOf())))))))
        )), trie) // (Doge, Cat, Call, Cale, Calv)
    }


    @Test
    fun testfind() {

        val trie = Trie(setOf(Node('D', setOf(Node('o', setOf(Node('g', setOf()))))),
                Node('C', setOf(Node('a', setOf(Node('t', setOf())))))
        )) // (Dog, Cat)
        assertTrue(trie.find("Dog")) // can be found
        assertTrue(trie.find("Ca")) // can be found
        assertTrue(trie.find("D")) // can be found
        assertFalse(trie.find("Doge")) // cannot be found
        assertFalse(trie.find("Grail")) // cannot be found
    }


    @Test
    fun testprefix() {

        val trie = Trie(setOf(
                Node('D', setOf(Node('o', setOf(Node('g', setOf(Node('e', setOf()))))))),
                Node('C', setOf(Node('a', setOf(Node('t', setOf()),
                        Node('l', setOf(Node('l', setOf()), Node('e', setOf()),
                                Node('v', setOf())))))))
        )) // (Doge, Cat, Call, Cale, Calv)

        assertEquals(setOf("Doge"), trie.findPrefix("Dog"))
        assertEquals(setOf<String>(), trie.findPrefix("Detonator"))
        assertEquals(setOf("Call", "Cale", "Calv"), trie.findPrefix("Cal"))
        assertEquals(setOf("Cat", "Call", "Cale", "Calv"), trie.findPrefix("C"))
    }


    @Test
    fun effectivity() {

        val trie = Trie(setOf())

        for (i in 0..10000) trie.add("OMAE_WA_MOU_SHINDEIRU_$i")
        for (i in 0..10000 step 2) trie.remove("OMAE_WA_MOU_SHINDEIRU_$i")
        assertEquals(setOf(
                "OMAE_WA_MOU_SHINDEIRU_9621",
                "OMAE_WA_MOU_SHINDEIRU_9623",
                "OMAE_WA_MOU_SHINDEIRU_9625",
                "OMAE_WA_MOU_SHINDEIRU_9627",
                "OMAE_WA_MOU_SHINDEIRU_9629"
        ), trie.findPrefix("OMAE_WA_MOU_SHINDEIRU_962"))
    } // It takes about 15-20 secs now. Quite a lot.

}
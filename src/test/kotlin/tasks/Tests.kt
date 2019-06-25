package tasks

import org.junit.Assert.*
import org.junit.Test
import tasks.Trie.*

class Tests {


    @Test
    fun testadd() {
        val trie = Trie(Node('*', mutableSetOf(
                Node('D', mutableSetOf(Node('o', mutableSetOf(Node('g', mutableSetOf(),
                        true)), false)), false),
                Node('C', mutableSetOf(Node('a', mutableSetOf(Node('t', mutableSetOf(),
                        true)), false)), false)
        ), false)) // (Dog, Cat)

        trie.addString("One")
        assertEquals(Trie(Node('*', mutableSetOf(
                Node('D', mutableSetOf(Node('o', mutableSetOf(Node('g', mutableSetOf(),
                        true)), false)), false),
                Node('C', mutableSetOf(Node('a', mutableSetOf(Node('t', mutableSetOf(),
                        true)), false)), false),
                Node('O', mutableSetOf(Node('n', mutableSetOf(Node('e', mutableSetOf(),
                        true)), false)), false) // <<<< added
        ), false)), trie) // (Dog, Cat, One)

        trie.addString("Calc")
        assertEquals(Trie(Node('*', mutableSetOf(
                Node('D', mutableSetOf(Node('o', mutableSetOf(Node('g', mutableSetOf(),
                        true)), false)), false),
                Node('C', mutableSetOf(Node('a', mutableSetOf(Node('t', mutableSetOf(),
                        true), Node('l', mutableSetOf(Node('c', mutableSetOf(),
                        true)), false)), false)), false), // <<<< added
                Node('O', mutableSetOf(Node('n', mutableSetOf(Node('e', mutableSetOf(),
                        true)), false)), false)), false)).hashCode(),
                trie.hashCode()) // (Dog, One, Cat, Calc)

        trie.addString("Doge")
        assertEquals(Trie(Node('*', mutableSetOf(
                Node('D', mutableSetOf(Node('o', mutableSetOf(Node('g',
                        mutableSetOf(Node('e', mutableSetOf(), true)),
                        true)), false)), false), // <<<< added
                Node('C', mutableSetOf(Node('a', mutableSetOf(Node('t', mutableSetOf(),
                        true), Node('l', mutableSetOf(Node('c', mutableSetOf(),
                        true)), false)), false)), false),
                Node('O', mutableSetOf(Node('n', mutableSetOf(Node('e', mutableSetOf(),
                        true)), false)), false)
        ), false)), trie) // (Dog, Doge, One, Cat, Calc)

        trie.addString("Cat")
        assertEquals(Node('*', mutableSetOf(
                Node('D', mutableSetOf(Node('o', mutableSetOf(Node('g',
                        mutableSetOf(Node('e', mutableSetOf(), true)),
                        true)), false)), false),
                Node('C', mutableSetOf(Node('a', mutableSetOf(Node('t', mutableSetOf(),
                        true), Node('l', mutableSetOf(Node('c', mutableSetOf(),
                        true)), false)), false)), false),
                Node('O', mutableSetOf(Node('n', mutableSetOf(Node('e', mutableSetOf(),
                        true)), false)), false)
        ), false), trie)  // No changes

        assertTrue(trie.addString("Calculate")) // can be added
        assertFalse(trie.addString("Doge")) // cannot be added
        assertFalse(trie.addString("")) // cannot add empty string
    }

    @Test
    fun testdel() {
        val trie = Trie(Node('*', mutableSetOf(
                Node('D', mutableSetOf(Node('o', mutableSetOf(Node('g',
                        mutableSetOf(Node('e', mutableSetOf(), true)),
                        true)), false)), false),
                Node('C', mutableSetOf(Node('a', mutableSetOf(Node('t', mutableSetOf(),
                        true), Node('l', mutableSetOf(Node('c', mutableSetOf(),
                        true)), false)), false)), false),
                Node('O', mutableSetOf(Node('n', mutableSetOf(Node('e', mutableSetOf(),
                        true)), false)), false)), false)) // (Dog, Doge, One, Cat, Calc)

        trie.deleteString("Doge")
        assertEquals(Trie(Node('*', mutableSetOf(
                Node('D', mutableSetOf(Node('o', mutableSetOf(Node('g', mutableSetOf(),
                        true)), false)), false),
                Node('C', mutableSetOf(Node('a', mutableSetOf(Node('t', mutableSetOf(),
                        true), Node('l', mutableSetOf(Node('c', mutableSetOf(),
                        true)), false)), false)), false),
                Node('O', mutableSetOf(Node('n', mutableSetOf(Node('e', mutableSetOf(),
                        true)),false)),false)
        ),false)), trie) // (Dog, One, Cat, Calc)

        trie.deleteString("Calc")
        assertEquals(Trie(Node('*', mutableSetOf(
                Node('D', mutableSetOf(Node('o', mutableSetOf(Node('g', mutableSetOf(),
                        true)), false)), false),
                Node('C', mutableSetOf(Node('a', mutableSetOf(Node('t', mutableSetOf(),
                        true)), false)), false),
                Node('O', mutableSetOf(Node('n', mutableSetOf(Node('e', mutableSetOf(),
                        true)), false)), false)
        ), false)), trie) // (Dog, Cat, One)

        trie.deleteString("One")
        assertEquals(Trie(Node('*', mutableSetOf(
                Node('D', mutableSetOf(Node('o', mutableSetOf(Node('g', mutableSetOf(),
                        true)), false)), false),
                Node('C', mutableSetOf(Node('a', mutableSetOf(Node('t', mutableSetOf(),
                        true)), false)), false)
        ), false)), trie) // (Dog, Cat)
    }

    @Test
    fun testfind() {
        val trie = Trie(Node('*', mutableSetOf(
                Node('D', mutableSetOf(Node('o', mutableSetOf(Node('g',
                        mutableSetOf(Node('e', mutableSetOf(), true)),
                        true)), false)), false),
                Node('C', mutableSetOf(Node('a', mutableSetOf(Node('t', mutableSetOf(),
                        true), Node('l', mutableSetOf(Node('c', mutableSetOf(),
                        true)), false)), false)), false),
                Node('O', mutableSetOf(Node('n', mutableSetOf(Node('e', mutableSetOf(),
                        true)), false)), false)), false))

        assertTrue(trie.find("Doge"))
        assertTrue(trie.find("Dog"))
        assertTrue(trie.find("Calc"))
        assertTrue(trie.find("Cat"))
        assertFalse(trie.find("Pill"))
        assertFalse(trie.find("Do"))
        assertFalse(trie.find("C"))
    }


    @Test
    fun testPrefix() {
        val trie = Trie(Node('*', mutableSetOf(
                Node('D', mutableSetOf(Node('o', mutableSetOf(Node('g',
                        mutableSetOf(Node('e', mutableSetOf(), true)),
                        true)), false)), false),
                Node('C', mutableSetOf(Node('a', mutableSetOf(Node('t', mutableSetOf(),
                        true), Node('l', mutableSetOf(Node('c', mutableSetOf(),
                        true)), false)), false)), false),
                Node('O', mutableSetOf(Node('n', mutableSetOf(Node('e', mutableSetOf(),
                        true)), false)), false)), false))

        assertEquals(setOf("One"), trie.findPrefix("One"))
        assertEquals(setOf("Dog", "Doge"), trie.findPrefix("Do"))
        assertEquals(setOf("Cat", "Calc"), trie.findPrefix("C"))
        assertEquals(setOf<String>(), trie.findPrefix("Go"))
    }
}

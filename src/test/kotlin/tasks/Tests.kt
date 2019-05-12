package tasks

import org.junit.Assert.*
import org.junit.Test
import tasks.Trie.*

class Tests {


    @Test
    fun testadd() {

        val trie = Node('*', mutableSetOf(
                Node('D', mutableSetOf(Node('o', mutableSetOf(Node('g', mutableSetOf(),
                        true)), false)), false),
                Node('C', mutableSetOf(Node('a', mutableSetOf(Node('t', mutableSetOf(),
                        true)), false)), false)
        ), false) // (Dog, Cat)

        trie.addStr("One")
        assertEquals(Node('*', mutableSetOf(
                Node('D', mutableSetOf(Node('o', mutableSetOf(Node('g', mutableSetOf(),
                        true)), false)), false),
                Node('C', mutableSetOf(Node('a', mutableSetOf(Node('t', mutableSetOf(),
                        true)), false)), false),
                Node('O', mutableSetOf(Node('n', mutableSetOf(Node('e', mutableSetOf(),
                        true)), false)), false) // <<<< added
        ), false), trie) // (Dog, Cat, One)

        trie.addStr("Calc")
        assertEquals(Node('*', mutableSetOf(
                Node('D', mutableSetOf(Node('o', mutableSetOf(Node('g', mutableSetOf(),
                        true)), false)), false),
                Node('C', mutableSetOf(Node('a', mutableSetOf(Node('t', mutableSetOf(),
                        true), Node('l', mutableSetOf(Node('c', mutableSetOf(),
                        true)), false)), false)), false), // <<<< added
                Node('O', mutableSetOf(Node('n', mutableSetOf(Node('e', mutableSetOf(),
                        true)),false)),false)
        ),false).hashCode(), trie.hashCode()) // (Dog, One, Cat, Calc)

        trie.addStr("Doge")
        assertEquals(Node('*', mutableSetOf(
                Node('D', mutableSetOf(Node('o', mutableSetOf(Node('g',
                        mutableSetOf(Node('e', mutableSetOf(), true)),
                        true)), false)), false), // <<<< added
                Node('C', mutableSetOf(Node('a', mutableSetOf(Node('t', mutableSetOf(),
                        true), Node('l', mutableSetOf(Node('c', mutableSetOf(),
                        true)), false)), false)), false),
                Node('O', mutableSetOf(Node('n', mutableSetOf(Node('e', mutableSetOf(),
                        true)), false)), false)
        ), false).hashCode(), trie.hashCode()) // (Dog, Doge, One, Cat, Calc)

        trie.addStr("Cat")
        assertEquals(Node('*', mutableSetOf(
                Node('D', mutableSetOf(Node('o', mutableSetOf(Node('g',
                        mutableSetOf(Node('e', mutableSetOf(), true)),
                        true)), false)), false),
                Node('C', mutableSetOf(Node('a', mutableSetOf(Node('t', mutableSetOf(),
                        true), Node('l', mutableSetOf(Node('c', mutableSetOf(),
                        true)), false)), false)), false),
                Node('O', mutableSetOf(Node('n', mutableSetOf(Node('e', mutableSetOf(),
                        true)), false)), false)
        ), false).hashCode(), trie.hashCode())  // No changes

        assertTrue(trie.addStr("Calculate")) // can be added
        assertFalse(trie.addStr("Doge")) // cannot be added
        assertFalse(trie.addStr("")) // cannot add empty string
    }


    @Test
    fun testdel() {
        val trie = Node('*', mutableSetOf(
                Node('D', mutableSetOf(Node('o', mutableSetOf(Node('g',
                        mutableSetOf(Node('e', mutableSetOf(), true)),
                        true)), false)), false),
                Node('C', mutableSetOf(Node('a', mutableSetOf(Node('t', mutableSetOf(),
                        true), Node('l', mutableSetOf(Node('c', mutableSetOf(),
                        true)), false)), false)), false),
                Node('O', mutableSetOf(Node('n', mutableSetOf(Node('e', mutableSetOf(),
                        true)), false)), false)), false) // (Dog, Doge, One, Cat, Calc)

        trie.delStr("Doge")
        assertEquals(Node('*', mutableSetOf(
                Node('D', mutableSetOf(Node('o', mutableSetOf(Node('g', mutableSetOf(),
                        true)), false)), false),
                Node('C', mutableSetOf(Node('a', mutableSetOf(Node('t', mutableSetOf(),
                        true), Node('l', mutableSetOf(Node('c', mutableSetOf(),
                        true)), false)), false)), false),
                Node('O', mutableSetOf(Node('n', mutableSetOf(Node('e', mutableSetOf(),
                        true)),false)),false)
        ),false).hashCode(), trie.hashCode()) // (Dog, One, Cat, Calc)

        trie.delStr("Calc")
        assertEquals(Node('*', mutableSetOf(
                Node('D', mutableSetOf(Node('o', mutableSetOf(Node('g', mutableSetOf(),
                        true)), false)), false),
                Node('C', mutableSetOf(Node('a', mutableSetOf(Node('t', mutableSetOf(),
                        true)), false)), false),
                Node('O', mutableSetOf(Node('n', mutableSetOf(Node('e', mutableSetOf(),
                        true)), false)), false)
        ), false).hashCode(), trie.hashCode()) // (Dog, Cat, One)

        trie.delStr("One")
        assertEquals(Node('*', mutableSetOf(
                Node('D', mutableSetOf(Node('o', mutableSetOf(Node('g', mutableSetOf(),
                        true)), false)), false),
                Node('C', mutableSetOf(Node('a', mutableSetOf(Node('t', mutableSetOf(),
                        true)), false)), false)
        ), false).hashCode(), trie.hashCode()) // (Dog, Cat)

    }


    @Test
    fun testfind() {
        val trie = Node('*', mutableSetOf(
                Node('D', mutableSetOf(Node('o', mutableSetOf(Node('g',
                        mutableSetOf(Node('e', mutableSetOf(), true)),
                        true)), false)), false),
                Node('C', mutableSetOf(Node('a', mutableSetOf(Node('t', mutableSetOf(),
                        true), Node('l', mutableSetOf(Node('c', mutableSetOf(),
                        true)), false)), false)), false),
                Node('O', mutableSetOf(Node('n', mutableSetOf(Node('e', mutableSetOf(),
                        true)), false)), false)), false)

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
        val trie = Node('*', mutableSetOf(
                Node('D', mutableSetOf(Node('o', mutableSetOf(Node('g',
                        mutableSetOf(Node('e', mutableSetOf(), true)),
                        true)), false)), false),
                Node('C', mutableSetOf(Node('a', mutableSetOf(Node('t', mutableSetOf(),
                        true), Node('l', mutableSetOf(Node('c', mutableSetOf(),
                        true)), false)), false)), false),
                Node('O', mutableSetOf(Node('n', mutableSetOf(Node('e', mutableSetOf(),
                        true)), false)), false)), false)

        assertEquals(setOf("One"), trie.findPrefix("One"))
        assertEquals(setOf("Dog", "Doge"), trie.findPrefix("Do"))
        assertEquals(setOf("Cat", "Calc"), trie.findPrefix("C"))
        assertEquals(setOf<String>(), trie.findPrefix("Go"))
    }
}

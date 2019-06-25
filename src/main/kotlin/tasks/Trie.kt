package tasks


data class Trie(var trie: Node) {

    data class Node(val value: Char, var next: MutableSet<Node>, var terminal: Boolean) {
        fun toTrie(): Trie = Trie(this)
    }

    fun addString(str: String): Boolean {
        if (str.isEmpty()) return false
        if (trie.next.all { it.value != str.first() }) {
            var node = Node(str.last(), mutableSetOf(), true)
            for (i in str.length - 2 downTo 0) node = Node(str[i], mutableSetOf(node), false)
            trie.next.add(node)
            return true
        }
        if (str.length == 1) {
            return if (!trie.next.first { it.value == str.first() }.terminal) {
                trie.next.first { it.value == str.first() }.terminal = true
                true
            } else false
        }
        return trie.next.first { it.value == str.first() }.toTrie().addString(str.drop(1))
    }


    fun deleteString(str: String): Boolean {
        var match: Node? = null
        try {
            match = trie.next.first { it.value == str.first() }
        } catch (e: NoSuchElementException) {
        }
        if (str.isEmpty()) return false
        if (str.length != 1)
            if (match != null) {
                val c = match.terminal     // connected (1) \/
                match.toTrie().deleteString(str.drop(1))
                if (match.terminal && !c) {
                    trie.next = trie.next.filter { it.value != str.first() }.toMutableSet()
                    if (trie.next.isEmpty()) trie.terminal = true     // connected (1) /\ \/
                }
            } else return false
        return if (str.length == 1 && match != null && match.terminal) {
            match.terminal = false
            if (match.next.isEmpty()) {
                trie.next = trie.next.filter { it.value != str.first() }.toMutableSet()
                if (trie.next.isEmpty()) trie.terminal = true     // connected (1) /\
            }
            true
        } else false
    }

    private fun pathfinder(str: String): Node? {
        if (str.isEmpty()) return null
        if (trie.next.all { it.value != str.first() }) return null
        if (str.length == 1)
            return trie.next.first { it.value == str.first() }
        return trie.next.first { it.value == str.first() }.toTrie().pathfinder(str.drop(1))
    }

    private fun stringer(input: Node, str: String, output: Set<String>): Set<String> {
        var out = output + input.next.filter { it.terminal }.map { str + it.value }
        if (input.next.isEmpty()) return out
        for (elem in input.next) out += stringer(elem, str + elem.value, out)
        return out
    }

    fun find(str: String): Boolean {
        return (pathfinder(str) ?: return false).terminal
    }

    fun findPrefix(start: String): Set<String> {
        var sum = setOf<String>()
        val path = pathfinder(start) ?: return sum
        if (path.terminal) sum += start
        return stringer(path, start, sum)
    }
}
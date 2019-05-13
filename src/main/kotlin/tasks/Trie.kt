package tasks


data class Trie(var trie: Node) {


    data class Node(val value: Char, var next: MutableSet<Node>, var terminal: Boolean) {


        private fun buildNode(str: String): Node {
            var node = Node(str.last(), mutableSetOf(), true)
            for (i in str.length - 2 downTo 0) node = Node(str[i], mutableSetOf(node), false)
            return node
        }


        fun addStr(str: String): Boolean {

            if (str.isEmpty()) return false

            if (next.all { it.value != str.first() }) {
                next.add(buildNode(str))
                return true
            }

            if (str.length == 1) {
                return if (!next.first { it.value == str.first() }.terminal) {
                    next.first { it.value == str.first() }.terminal = true
                    true
                } else false
            }
            return next.first { it.value == str.first() }.addStr(str.drop(1))
        }


        fun delStr(start: String): Boolean {

            fun deleter(str: String): Boolean {

                if (str.isEmpty()) return false

                if (str.length != 1)
                    if (next.any { it.value == str.first() }) {
                        val c = next.first { it.value == str.first() }.terminal     // connected (1) \/
                        next.first { it.value == str.first() }.delStr(str.drop(1))
                        if (next.first { it.value == str.first() }.terminal && !c) {
                            next = next.filter { it.value != str.first() }.toMutableSet()
                            if (next.isEmpty()) terminal = true     // connected (1) /\ \/
                        }
                    } else return false

                if (str.length == 1 && next.any { it.value == str.first() && it.terminal }) {
                    next.first { it.value == str.first() }.terminal = false
                    if (next.first { it.value == str.first() }.next.isEmpty()) {
                        next = next.filter { it.value != str.first() }.toMutableSet() // ! - не работает, если изменить
                        if (next.isEmpty()) terminal = true     // connected (1) /\
                    }
                    return true
                } else return false
            }
            return deleter(start)
        }


        fun find(str: String): Boolean {
            if (str.isEmpty()) return false
            if (next.all { it.value != str.first() }) return false
            if (str.length == 1 && next.first { it.value == str.first() }.terminal) return true
            return next.first { it.value == str.first() }.find(str.drop(1))
        }


        private fun pathfinder(str: String): Node? {
            if (str.isEmpty()) return null
            if (next.all { it.value != str.first() }) return null
            if (str.length == 1)
                return next.first { it.value == str.first() }
            return next.first { it.value == str.first() }.pathfinder(str.drop(1))
        }
        private fun stringer(input: Node, str: String, output: Set<String>): Set<String> {
            var out = output + input.next.filter { it.terminal }.map { str + it.value}
            if (input.next.isEmpty()) return out
            for (elem in input.next) out += stringer(elem, str + elem.value, out)
            return out
        }

        fun findPrefix(start: String): Set<String> {
            var sum = setOf<String>()
            val path = pathfinder(start) ?: return sum
            if (find(start)) sum += start
            return stringer(path, start, sum)
        }
    }
}

package tasks

data class Trie(var trie: Set<Node>) {

    /*
        Конструкция:
                                          /---- [C], Set of ...
                        /---- [A], Set of --- [D], Set of ...
        [ROOT] Set of: {
                        \---- [B], Set of --- [E], Set of ...

     */

    data class Node(val value: Char, val next: Set<Node>)


    // *** Вспомогательные функции "add" ***
    private fun buildNode(str: String): Node {
        var node = Node(str.last(), setOf())
        for (i in str.length - 2 downTo 0) node = Node(str[i], setOf(node))
        return node
    }
    private fun delfirst(s: String, num: Int): String = buildString {
        for (i in num until s.length) this.append(s[i])
    }

    fun add(str: String): Boolean {

        if (str.isEmpty()) return false

        // Нельзя добавить пустую строку. В тестах выдавало ошибку, поэтому пришлось добавить эту проверку.

        if (trie.all { it.value != str.first() }) {
            trie += buildNode(str)
            return true
        }

        // Если нет веток первого уровня, начинающихся с того же символа, то можно сразу добавить всю строку.

        var root = trie.first { it.value == str.first() }

        // Узел со значением, совпадающим с первым символом строки

        var other: Map<Int, Set<Node>> = mapOf()

        // Здесь будут храниться все узлы со значениями, не совпадающими с символами строки на их уровне.
        // Нужно, чтобы потом отстроить новую (дополненную введенной строкой) ветку и заменить ей старую.

        for (i in 1 until str.length) {
            if (root.next.any { it.value == str[i] }) {
                other += mapOf(i to root.next.filter { it.value != str[i] }.toSet())
                root = root.next.first { it.value == str[i] }
            }

            // Если среди нижних узлов есть такой, что его значение равно следующему символу строки,
            // то мы сохраняем все остальные нижние узлы и переходим к нужному

            else {
                root = Node(root.value, root.next + buildNode(delfirst(str, i)))

                // Если больше нет узлов с подходящими значениями, мы создаем новую ветку и добавляем ее на
                // уровень, с которым работаем сейчас.

                for (j in i - 1 downTo 1)
                    root = Node(str[j - 1], other[j]!! + root)

                // Дальше мы идем вверх по дереву, внося во все старые ветки информацию
                // о новом узле. Таким образом доходим до ветки первого уровня.

                trie = trie - trie.filter { it.value == str.first() } + root
                return true

                // Полностью удаляем старую ветку первого уровня и ставим вместо нее обновленную.

            }
        }
        return false

        // Если мы полностью прошли все символы строки в дереве и не нашли ниодного недостающего, то
        // эта строка уже есть в дереве.
    }



    fun remove(str: String): Boolean {

        if (add(str)) return false

        // Если ветку можно добавить, значит ее нет в дереве и ее нельзя удалить.

        var root = trie.first { it.value == str.first() }
        var other: Map<Int, Set<Node>> = mapOf()

        // См. функцию "add"

        if (str.length == 1)
            return if (root.next.isEmpty()) {
                trie -= Node(str.first(), setOf())
                true
            } else false

        // Пришлось добавить, т.к. при попытке удалить строку длинной в один символ выдает ошибку.

        for (i in 1 until str.length) {
            other += mapOf(i to root.next.filter { it.value != str[i] }.toSet())
            root = root.next.first { it.value == str[i] }
        }

        // Здесь никаких проверок на наличие символа не нужно, т.к. мы уже установили, что строка есть
        // в дереве.

        if (root.next.isNotEmpty()) return false

        // Нельзя удалить префикс от строки.

        root = Node(str[other.size - 1], other[other.size]!!)

        // Создаем новую ветку, но уже без последнего символа удаляемой строки.

        for (j in other.size - 1 downTo 1)
            root = if (root.next.isEmpty())
                Node(str[j - 1], other[j]!!)
            else
                Node(str[j - 1], other[j]!! + root)

        // Как и в "add", вносим изменения в старые ветки. Если с какого-то момента удаляемая строка становится
        // префиксом, то дальше удалять символы нельзя.
        // P.S. Вот в этом месте программу можно сделать покрасивее.

        trie = if (root.next.isEmpty())
            trie - trie.filter { it.value == str.first() }
        else
            trie - trie.filter { it.value == str.first() } + root

        // По сути, то же самое действие, что и в цикле выше. Но сейчас изменения вносятся уже в само дерево.

        return true
    }



    fun find(str: String): Boolean = !add(str)



    // *** Вспомогательные функции "findPrefix" ***
    private fun getchars(n: Node): Set<String> = n.next.map { it.value.toString() }.toSet()
    private fun take(): Node = trie.first()

    fun findPrefix(str: String): Set<String> {

        if (add(str)) {
            println("Prefix not found ($str)")
            return setOf()
        }

        var root = trie.first { it.value == str.first() }

        for (i in 1 until str.length) {
            root = root.next.first { it.value == str[i] }
        }

        var sum = setOf<String>()
        val tree = Trie(root.next)

        while (getchars(root).any { tree.find(it) }) {
            val string = buildString {
                var now = tree.take()
                append(now.value)
                while (now.next.isNotEmpty()) {
                    now = now.next.first()
                    append(now.value)
                }
            }
            tree.remove(string)
            sum += str + string
        }

        return(sum)
    }
    // Самая сложная в плане реализации функция ввиду конструктивных особенностей дерева.
    // Еще и неэффективная.
    // И вообще, зачем было так запариваться и возвращать строки, если можно было просто кинуть нужную ветку?

}
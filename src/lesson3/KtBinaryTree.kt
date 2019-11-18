package lesson3

import java.util.*
import kotlin.NoSuchElementException
import kotlin.math.max

// Attention: comparable supported but comparator is not
open class KtBinaryTree<T : Comparable<T>> : AbstractMutableSet<T>(), CheckableSortedSet<T> {

    private var root: Node<T>? = null

    override var size = 0

    private class Node<T>(val value: T) {

        var left: Node<T>? = null

        var right: Node<T>? = null
    }

    override fun add(element: T): Boolean {
        val closest = find(element)
        val comparison = if (closest == null) -1 else element.compareTo(closest.value)
        if (comparison == 0) {
            return false
        }
        val newNode = Node(element)
        when {
            closest == null -> root = newNode
            comparison < 0 -> {
                assert(closest.left == null)
                closest.left = newNode
            }
            else -> {
                assert(closest.right == null)
                closest.right = newNode
            }
        }
        size++
        return true
    }

    override fun checkInvariant(): Boolean =
        root?.let { checkInvariant(it) } ?: true

    override fun height(): Int = height(root)

    private fun checkInvariant(node: Node<T>): Boolean {
        val left = node.left
        if (left != null && (left.value >= node.value || !checkInvariant(left))) return false
        val right = node.right
        return right == null || right.value > node.value && checkInvariant(right)
    }

    private fun height(node: Node<T>?): Int {
        if (node == null) return 0
        return 1 + max(height(node.left), height(node.right))
    }

    /**
     * Удаление элемента в дереве
     * Средняя
     */
    override fun remove(element: T): Boolean {
        TODO()
    }

    override operator fun contains(element: T): Boolean {
        val closest = find(element)
        return closest != null && element.compareTo(closest.value) == 0
    }

    private fun find(value: T): Node<T>? =
        root?.let { find(it, value) }

    private fun find(start: Node<T>, value: T): Node<T> {
        val comparison = value.compareTo(start.value)
        return when {
            comparison == 0 -> start
            comparison < 0 -> start.left?.let { find(it, value) } ?: start
            else -> start.right?.let { find(it, value) } ?: start
        }
    }

    inner class BinaryTreeIterator internal constructor() : MutableIterator<T> {
        private var stack: Stack<Node<T>> = Stack()

        init {
            addLeft(root)
        }

        private fun addLeft(node: Node<T>?) {
            var root = node
            while (root != null) {
                stack.push(root)
                root = root.left
            }
        }

        /**
         * Проверка наличия следующего элемента
         * Средняя
         */
        override fun hasNext(): Boolean = stack.isNotEmpty() // Трудоемкость - O(1), Ресурсоемкость - O(1)

        /**
         * Поиск следующего элемента
         * Средняя
         */
        override fun next(): T {
            if (!hasNext()) throw NoSuchElementException()

            val node = stack.pop()
            val right = node.right

            if (right != null) {
                addLeft(right)
            }

            return node.value
            // Трудоемкость в худшем случае - O(n), в среднем случае - O(1), Ресурсоемкость - O(высота дерева)
        }

        /**
         * Удаление следующего элемента
         * Сложная
         */
        override fun remove() {
            // TODO
            throw NotImplementedError()
        }
    }

    override fun iterator(): MutableIterator<T> = BinaryTreeIterator()

    override fun comparator(): Comparator<in T>? = null

    inner class BinarySubTree<T : Comparable<T>> internal constructor(
        private val tree: KtBinaryTree<T>,
        private val from: T?,
        private val to: T?
    ) : KtBinaryTree<T>() {

        override var size: Int = 0
            get() = tree.count { inRange(it) }

        override fun add(element: T): Boolean {
            if (!inRange(element)) {
                throw IllegalArgumentException()
            }
            return tree.add(element)
        }

        private fun inRange(element: T) = (from == null || element >= from) && (to == null || element < to)

        override fun contains(element: T): Boolean = tree.contains(element) && inRange(element)
    }

    /**
     * Найти множество всех элементов в диапазоне [fromElement, toElement)
     * Очень сложная
     */
    override fun subSet(fromElement: T, toElement: T): SortedSet<T> = BinarySubTree(this, fromElement, toElement)
    // Трудоемеость - O(1), Ресурсоемкость - O(1)

    /**
     * Найти множество всех элементов меньше заданного
     * Сложная
     */
    override fun headSet(toElement: T): SortedSet<T> = BinarySubTree(this, null, toElement)
    // Трудоемеость - O(1), Ресурсоемкость - O(1)

    /**
     * Найти множество всех элементов больше или равных заданного
     * Сложная
     */
    override fun tailSet(fromElement: T): SortedSet<T> = BinarySubTree(this, fromElement, null)
    // Трудоемеость - O(1), Ресурсоемкость - O(1)

    override fun first(): T {
        var current: Node<T> = root ?: throw NoSuchElementException()
        while (current.left != null) {
            current = current.left!!
        }
        return current.value
    }

    override fun last(): T {
        var current: Node<T> = root ?: throw NoSuchElementException()
        while (current.right != null) {
            current = current.right!!
        }
        return current.value
    }
}

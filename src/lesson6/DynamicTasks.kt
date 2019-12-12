@file:Suppress("UNUSED_PARAMETER")

package lesson6

import java.io.File

/**
 * Наибольшая общая подпоследовательность.
 * Средняя
 *
 * Дано две строки, например "nematode knowledge" и "empty bottle".
 * Найти их самую длинную общую подпоследовательность -- в примере это "emt ole".
 * Подпоследовательность отличается от подстроки тем, что её символы не обязаны идти подряд
 * (но по-прежнему должны быть расположены в исходной строке в том же порядке).
 * Если общей подпоследовательности нет, вернуть пустую строку.
 * Если есть несколько самых длинных общих подпоследовательностей, вернуть любую из них.
 * При сравнении подстрок, регистр символов *имеет* значение.
 */
fun longestCommonSubSequence(first: String, second: String): String {
    TODO()
}

/**
 * Наибольшая возрастающая подпоследовательность
 * Сложная
 *
 * Дан список целых чисел, например, [2 8 5 9 12 6].
 * Найти в нём самую длинную возрастающую подпоследовательность.
 * Элементы подпоследовательности не обязаны идти подряд,
 * но должны быть расположены в исходном списке в том же порядке.
 * Если самых длинных возрастающих подпоследовательностей несколько (как в примере),
 * то вернуть ту, в которой числа расположены раньше (приоритет имеют первые числа).
 * В примере ответами являются 2, 8, 9, 12 или 2, 5, 9, 12 -- выбираем первую из них.
 */
fun longestIncreasingSubSequence(list: List<Int>): List<Int> {
    val size = list.size

    if (size == 0) return listOf()

    val length = IntArray(size) { 1 }
    val path = IntArray(size) { -1 }

    for (i in list.indices) {
        for (j in 0 until i) {
            if (list[j] < list[i] && (length[i] < length[j] + 1)) {
                length[i] = length[j] + 1
                path[i] = j
            }
        }
    }

    var pointer = length.indexOf(length.max()!!)

    val result = mutableListOf<Int>()

    while (pointer != -1) {
        result.add(list[pointer])
        pointer = path[pointer]
    }

    return result.reversed()
    // Трудоемкость - О(n^2), Ресурсоемкость - O(n)
}

/**
 * Самый короткий маршрут на прямоугольном поле.
 * Средняя
 *
 * В файле с именем inputName задано прямоугольное поле:
 *
 * 0 2 3 2 4 1
 * 1 5 3 4 6 2
 * 2 6 2 5 1 3
 * 1 4 3 2 6 2
 * 4 2 3 1 5 0
 *
 * Можно совершать шаги длиной в одну клетку вправо, вниз или по диагонали вправо-вниз.
 * В каждой клетке записано некоторое натуральное число или нуль.
 * Необходимо попасть из верхней левой клетки в правую нижнюю.
 * Вес маршрута вычисляется как сумма чисел со всех посещенных клеток.
 * Необходимо найти маршрут с минимальным весом и вернуть этот минимальный вес.
 *
 * Здесь ответ 2 + 3 + 4 + 1 + 2 = 12
 */
fun shortestPathOnField(inputName: String): Int {
    val lines = File(inputName).readLines().map { it.split(" ") }

    val grid = mutableListOf<MutableList<Int>>()

    for (i in lines.indices) {
        grid.add(lines[i].map { it.toInt() }.toMutableList())
    }

    for (i in 1 until grid[0].size) {
        grid[0][i] += grid[0][i - 1]
    }

    when {
        grid.size == 1 -> return grid[0].last()
        grid[0].size == 1 -> return grid.sumBy { it[0] }
    }

    var thisLine = mutableListOf<Int>()

    for (i in 1 until grid.size) {
        val prevLine = grid[i - 1]
        thisLine = grid[i]

        thisLine[0] += prevLine[0]

        for (j in 1 until thisLine.size) {
            thisLine[j] = thisLine[j] + minOf(thisLine[j - 1], prevLine[j - 1], prevLine[j])
        }
    }

    return thisLine.last()
    // Трудоемкость - O(ширина * длина поля), Ресурсоемкость - O(n)
}

// Задачу "Максимальное независимое множество вершин в графе без циклов"
// смотрите в уроке 5
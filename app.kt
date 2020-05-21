package search
import java.util.*

enum class MESSAGES(val msg: String) {
    ENTER_PEOPLE_NUM("Enter the number of people:"),
    ENTER_PEOPLE("Enter all people:"),
    ENTER_QUERIES_NUM("Enter the number of search queries:"),
    ENTER_QUERY_DATA("Enter data to search people:"),
    SEARCH_RESULTS("Found people:"),
    NO_SEARCH_RESULTS("No matching people found.")
}

class SearchData {
    val items = mutableMapOf<Int, String>()

    fun add(item: String) {
        items[items.size] = item
    }

    fun search(query: String): Map<Int, String> {
        return items.filterValues { it.toUpperCase().contains(query.toUpperCase()) }
    }
}

fun main() {
    val scanner = Scanner(System.`in`)
    val searchData = SearchData()

    var count: Int?
    do {
        println(MESSAGES.ENTER_PEOPLE_NUM.msg)
        count = scanner.nextLine().toIntOrNull()
    } while (count == null || count < 1)

    println(MESSAGES.ENTER_PEOPLE.msg)
    repeat(count) {
        searchData.add(scanner.nextLine())
    }
    println()

    do {
        println(MESSAGES.ENTER_QUERIES_NUM.msg)
        count = scanner.nextLine().toIntOrNull()
    } while (count == null || count < 1)
    println()

    repeat(count) {
        println(MESSAGES.ENTER_QUERY_DATA.msg)
        val results = searchData.search(scanner.nextLine())
        if (results.isEmpty()) {
            println(MESSAGES.NO_SEARCH_RESULTS.msg)
        } else {
            println(MESSAGES.SEARCH_RESULTS.msg)
            results.values.map(::println)
        }
        println()
    }
}

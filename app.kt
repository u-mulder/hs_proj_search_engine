package search
import java.util.*

enum class MESSAGES(val msg: String) {
    ENTER_PEOPLE_NUM("Enter the number of people:"),
    ENTER_PEOPLE("Enter all people:"),
    MENU_HEADER("=== Menu ==="),
    MENU_ITEM_SEARCH("1. Find a person"),
    MENU_ITEM_PRINT_ALL("2. Print all people"),
    MENU_ITEM_EXIT("0. Exit"),
    MENU_ITEM_ERR("Incorrect option! Try again."),
    ENTER_QUERY_DATA("Enter a name or email to search all suitable people."),
    NO_SEARCH_RESULTS("No matching people found."),
    LIST_HEADER("=== List of people ==="),
    BYE("Bye!")
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

fun printMenu() {
    println(MESSAGES.MENU_HEADER.msg)
    println(MESSAGES.MENU_ITEM_SEARCH.msg)
    println(MESSAGES.MENU_ITEM_PRINT_ALL.msg)
    println(MESSAGES.MENU_ITEM_EXIT.msg)
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

    var menuItem: String
    do {
        printMenu()
        menuItem = scanner.nextLine()
        when (menuItem) {
            "0" -> {}
            "1" -> {
                println()
                println(MESSAGES.ENTER_QUERY_DATA.msg)
                val results = searchData.search(scanner.nextLine())
                if (results.isEmpty()) {
                    println(MESSAGES.NO_SEARCH_RESULTS.msg)
                } else {
                    results.values.map(::println)
                }
                println()
            }
            "2" -> {
                println()
                println(MESSAGES.LIST_HEADER.msg)
                searchData.items.values.map(::println)
                println()
            }
            else -> {
                println(MESSAGES.MENU_ITEM_ERR.msg)
                println()
            }
        }
    } while (menuItem != "0")

    println()
    println(MESSAGES.BYE.msg)
}

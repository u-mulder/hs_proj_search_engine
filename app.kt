package search
import java.io.File
import java.util.*

enum class MESSAGES(val msg: String) {
    ERROR_LOADING_DATA("Error loading data from file"),
    MENU_HEADER("=== Menu ==="),
    MENU_ITEM_SEARCH("1. Find a person"),
    MENU_ITEM_PRINT_ALL("2. Print all people"),
    MENU_ITEM_EXIT("0. Exit"),
    MENU_ITEM_ERR("Incorrect option! Try again."),
    ENTER_QUERY_DATA("Enter a name or email to search all suitable people."),
    NO_SEARCH_RESULTS("No matching people found."),
    LIST_HEADER("=== List of people ==="),
    INCORRECT_ARGUMENTS("Correct argument name is --data"),
    BYE("Bye!")
}

class SearchData {
    val items = mutableMapOf<Int, String>()
    val searchIndexes = mutableMapOf<String, ArrayList<Int>>()

    fun loadFromFile(filePath: String?) {
        var counter = 0
        File(filePath).forEachLine {
            add(it)

            for (part in it.split(" ")) {
                val upperPart = part.toUpperCase()
                if (!searchIndexes.containsKey(upperPart)) {
                    searchIndexes[upperPart] = arrayListOf<Int>()
                }

                searchIndexes[upperPart]?.add(counter)
            }

            counter++
        }
    }

    fun add(item: String) {
        items[items.size] = item
    }

    fun search(query: String): Map<Int, String> {
        var results = mutableMapOf<Int, String>()

        val upperQuery = query.toUpperCase()
        if (searchIndexes.containsKey(upperQuery)) {
            for (index in searchIndexes[upperQuery]!!) {
                results[results.size] = items[index]!!
            }
        }

        return results
    }
}

fun printMenu() {
    println(MESSAGES.MENU_HEADER.msg)
    println(MESSAGES.MENU_ITEM_SEARCH.msg)
    println(MESSAGES.MENU_ITEM_PRINT_ALL.msg)
    println(MESSAGES.MENU_ITEM_EXIT.msg)
}

fun main(args: Array<String>) {
    val argName = args.getOrNull(0)
    val argValue = args.getOrNull(1)

    if (null == argName || "--data" != argName || null == argValue) {
        println(MESSAGES.INCORRECT_ARGUMENTS.msg)
        println(MESSAGES.BYE.msg)
        return
    }

    val scanner = Scanner(System.`in`)
    val searchData = SearchData()
    try {
        searchData.loadFromFile(argValue)    
    } catch (e: Throwable) {
        println(MESSAGES.ERROR_LOADING_DATA.msg)
        return
    }

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

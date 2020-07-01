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
    ENTER_STRATEGY("Select a matching strategy: "),
    INCORRECT_STRATEGY("Incorrect strategy. Allowed strategies: "),
    ENTER_QUERY_DATA("Enter a name or email to search all suitable people."),
    NO_SEARCH_RESULTS("No matching people found."),
    LIST_HEADER("=== List of people ==="),
    INCORRECT_ARGUMENTS("Correct argument name is --data"),
    BYE("Bye!")
}

enum class SEARCH_STRATEGIES {
    ALL,
    ANY,
    NONE;

    companion object {
        fun getNames(): String {
            val names = mutableListOf<String>()
            names.add(ALL.name)
            names.add(ANY.name)
            names.add(NONE.name)

            return names.joinToString()
        }
    }
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

    fun search(query: String, strategy: String): Map<Int, String> {
        var results = mutableMapOf<Int, String>()
        val queryParts = query.toUpperCase().split(" ")
        var keysMap = mutableMapOf<Int, Boolean>()

        when (strategy) {
            SEARCH_STRATEGIES.ALL.name -> {
                var firstRun = true

                for (part in queryParts) {
                    // if part is not found in searchIndexes, then
                    // we will not find anything with ALL strategy
                    if (!searchIndexes.containsKey(part)) {
                        // if we already have keys - remove them
                        if (0 < keysMap.count()) {
                            keysMap.clear()
                        }

                        break
                    }

                    // for the first iteration we just select indexes of first part of query
                    if (firstRun) {
                        firstRun = false
                        for (key in searchIndexes[part]!!) {
                            keysMap[key] = true
                        }

                        continue
                    }

                    // for second and other iterations we check which keys 
                    // exist both in `keysMap` and in `searchIndexes[part]`
                    keysMap = keysMap
                        .filterKeys { k: Int -> searchIndexes[part]!!.contains(k) }
                        .toMutableMap()

                    if (0 == keysMap.count()) {
                        break
                    }
                }
            }
            SEARCH_STRATEGIES.ANY.name -> {
                keysMap = getKeysForStrategyAny(queryParts)
            }
            SEARCH_STRATEGIES.NONE.name -> {
                var keysMapForAny = getKeysForStrategyAny(queryParts)

                for (key in 0..items.size - 1) {
                    if (false == keysMapForAny.containsKey(key)) {
                        keysMap[key] = true
                    }
                }
            }
        }

        if (0 < keysMap.count()) {
            for (key in keysMap.keys) {
                results[results.size] = items[key]!!
            }
        }

        return results
    }

    fun getKeysForStrategyAny(queryParts: List<String>): MutableMap<Int, Boolean> {
        var keys = mutableMapOf<Int, Boolean>()

        for (part in queryParts) {
            // if part is not found in searchIndexes,
            // we just skip it with ANY strategy
            if (!searchIndexes.containsKey(part)) {
                continue
            }

            for (key in searchIndexes[part]!!) {
                keys[key] = true
            }
        }

        return keys
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
                var strategy:String
                var isAllowed = false

                do {
                    println()
                    println(MESSAGES.ENTER_STRATEGY.msg)
                    strategy = scanner.nextLine().toUpperCase()
                    for (strategyEnum in SEARCH_STRATEGIES.values()) {
                        if (strategy == strategyEnum.name) {
                            isAllowed = true
                            break
                        }
                    }

                    if (!isAllowed) {
                        println()
                        println(MESSAGES.INCORRECT_STRATEGY.msg + SEARCH_STRATEGIES.getNames())
                    }
                } while (!isAllowed)

                println()
                println(MESSAGES.ENTER_QUERY_DATA.msg)
                val results = searchData.search(scanner.nextLine(), strategy)
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

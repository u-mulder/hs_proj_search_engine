package search
import java.util.*

fun main() {
    val scanner = Scanner(System.`in`)
    
    val items = scanner.nextLine().split(" ")
    val query = scanner.nextLine()
    
    var foundIndex = 0
    for (index in items.indices) {
        if (items[index] == query) {
            foundIndex = index + 1
            break
        }
    }
    
    println(if (0 < foundIndex) foundIndex else "Not found")
}

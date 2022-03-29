package model

fun main() {
    val a = resum(23)
    print(a)
    /*
    var line = readLine()
    while (line != null) {
        var n = line.toInt()
        while (true) {

            if ()
        }

        line = readLine();
    }*/
}

fun resum(a: Int): Int {
    return a.toString().map { x -> x.toString().toInt() }.sum()
}

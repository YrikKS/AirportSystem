package ru.nsu.group20211.airport_system.presentation


class DbFilter(
    var nameFieldSort: String = "",
    var desc: Boolean = false,
    var queryMap: MutableMap<Int, String> = mutableMapOf()
) {

    fun generateQuery(): Pair<List<String>, List<String>> {
        var listOrder = mutableListOf<String>()
        if (nameFieldSort != "None") {
            if (desc) {
                listOrder.add("DESC " + nameFieldSort)
            } else {
                listOrder.add(nameFieldSort)
            }
        }
        var listCond = mutableListOf<String>()
        queryMap.forEach {
            listCond.add(it.value)
        }
        return listCond to listOrder
    }

}
package ru.nsu.group20211.airport_system.presentation

import ru.nsu.group20211.airport_system.data.addQuo


class DbFilter(
    var nameFieldSort: String = "",
    var desc: Boolean = false,
    var queryMap: MutableMap<Int, String> = mutableMapOf()
) {

    fun generateQuery(): Pair<List<String>, List<String>> {
        var listOrder = mutableListOf<String>()
        if (nameFieldSort != "None" && nameFieldSort.isNotEmpty()) {
            if (desc) {
                listOrder.add(""" "$nameFieldSort" DESC""")
            } else {
                listOrder.add(nameFieldSort.addQuo())
            }
        }
        var listCond = mutableListOf<String>()
        queryMap.forEach {
            listCond.add(it.value)
        }
        return listCond to listOrder
    }

}
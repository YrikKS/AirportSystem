package ru.nsu.group20211.airport_system.data

import android.util.Log

fun String.addQuo() = "\"$this\""

fun String.log(): String {
    Log.i("SQL: ", "SQL: $this")
    return this
}

fun StringBuilder.addWhere(list: List<String>): StringBuilder {
    var isWhenAdded = false
    list.forEach { element ->
        if (element.isNotEmpty()) {
            if (isWhenAdded == false) {
                append(" WHERE ")
                isWhenAdded = true
            } else {
                append(" AND ")
            }
            append(element)
        }
    }
//    if (list.isNotEmpty()) append(" WHERE ") else append("")
//    if (list.isNotEmpty())
//        append(list.first())
//    if (list.size > 1) {
//        list.forEachIndexed { index, str ->
//            if (index != 0 && str.isNotEmpty()) {
//                append(" AND ")
//                append(str)
//            }
//        }
//    }
    return this
}

fun addOrderBy(list: List<String>): String {
    return buildString { addOrderBy(list) }.toString()
}

fun StringBuilder.addOrderBy(list: List<String>): StringBuilder {
    var isOrderAdded = false
    list.forEach { element ->
        if (element.isNotEmpty()) {
            if (isOrderAdded == false) {
                append(" ORDER BY ")
                isOrderAdded = true
            } else {
                append(" AND ")
            }
            append(element)
        }
    }
    return this
}

fun addWhere(list: List<String>): String {
    return buildString { addWhere(list) }.toString()
}

fun StringBuilder.addJoins(list: List<String>): StringBuilder {
    list.forEach {
        append(" $it ")
    }
    return this
}

fun addJoins(list: List<String>): String {
    return buildString { addJoins(list) }.toString()
}
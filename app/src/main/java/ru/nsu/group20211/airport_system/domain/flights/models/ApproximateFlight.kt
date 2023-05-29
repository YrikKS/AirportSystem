package ru.nsu.group20211.airport_system.domain.flights.models

import ru.nsu.group20211.airport_system.data.addQuo
import ru.nsu.group20211.airport_system.domain.DbEntity
import ru.nsu.group20211.airport_system.domain.DbEntityCompanion
import java.sql.ResultSet
import java.sql.Timestamp
import kotlin.reflect.KClass

data class ApproximateFlight(
    var id: Int = 0,
    var idDepartureAirport: Int = 0,
    var idArrivalAirport: Int = 0,
    var frequencyInDays: Int = 1,
    var approximateTakeoffTime: Timestamp? = null,
    var approximatePrice: Float = 0F,

    var departureAirport: Airport? = null,
    var arrivalAirport: Airport? = null,
) : DbEntity {
    fun getTime() = buildString {
        append(approximateTakeoffTime!!.hours.toString())
        append(":")
        append(approximateTakeoffTime!!.minutes.toString())
        append(":")
        append(approximateTakeoffTime!!.seconds.toString())
    }

    override fun customGetId(): Int {
        return id
    }

    override fun insertQuery(): String {
        return buildString {
            append(""" INSERT INTO ${getTableName()} ("idDepartureAirport",  """)

            if (approximateTakeoffTime != null) {
                append(""" "idArrivalAirport", "frequencyInDays", "approximateTakeoffTime", "approximatePrice") """)
            } else {
                append(""" "idArrivalAirport", "frequencyInDays", "approximatePrice") """)
            }

            append(""" VALUES ($idDepartureAirport, $idArrivalAirport, $frequencyInDays,  """)
            if (approximateTakeoffTime != null) {
                append(""" TO_TIMESTAMP('$approximateTakeoffTime', 'YYYY-MM-DD HH24:MI:SS.FF'), $approximatePrice) """)
            } else {
                append(""" $approximatePrice) """)
            }
        }
    }

    override fun deleteQuery(): String {
        return """ DELETE FROM ${getTableName()} WHERE "id" = $id """
    }

    override fun updateQuery(): String {
        return buildString {
            append(""" UPDATE ${getTableName()} SET """)
            append(""" "idDepartureAirport" = $idDepartureAirport, """)
            append(""" "idArrivalAirport" = $idArrivalAirport, """)
            append(""" "frequencyInDays" = $frequencyInDays, """)
            if (approximateTakeoffTime != null)
                append(""" "approximateTakeoffTime" = TO_TIMESTAMP('$approximateTakeoffTime', 'YYYY-MM-DD HH24:MI:SS.FF'), """)
            append(""" "approximatePrice" = $approximatePrice """)
            append(""" WHERE "id" = $id """)
        }
    }

    override fun myEquals(other: Any): Boolean {
        return equals(other)
    }

    companion object : DbEntityCompanion<ApproximateFlight> {
        override fun getTableName(): String {
            return "approximateFlights".addQuo()
        }

        override fun getAll(): String {
            return super.getAll()
        }

        override fun ResultSet.getInstance(
            indexStart: Int,
            clazz: KClass<ApproximateFlight>
        ): Pair<ApproximateFlight, Int> {
            return ApproximateFlight(
                id = getInt(indexStart),
                idDepartureAirport = getInt(indexStart + 1),
                idArrivalAirport = getInt(indexStart + 2),
                frequencyInDays = getInt(indexStart + 3),
                approximateTakeoffTime = getTimestamp(indexStart + 4),
                approximatePrice = getFloat(indexStart + 5),
            ) to indexStart + 6
        }
    }
}
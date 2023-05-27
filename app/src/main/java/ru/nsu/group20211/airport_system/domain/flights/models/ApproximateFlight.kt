package ru.nsu.group20211.airport_system.domain.flights.models

import entity.addQuo
import ru.nsu.group20211.airport_system.domain.DbEntity
import ru.nsu.group20211.airport_system.domain.DbEntityCompanion
import java.sql.ResultSet
import java.sql.Timestamp
import kotlin.reflect.KClass

data class ApproximateFlight(
    var id: Int,
    var idDepartureAirport: Int,
    var idArrivalAirport: Int,
    var frequencyInDays: Int = 1,
    var approximateTakeoffTime: Timestamp? = null,
    var approximatePrice: Float
) : DbEntity {
    override fun customGetId(): Int {
        return id
    }

    override fun insertQuery(): String {
        return buildString {
            append(""" INSERT INTO ${getTableName()} ("idDepartureAirport",  """)
            append(""" "idArrivalAirport", "frequencyInDays", "approximateTakeoffTime", "approximatePrice") """)
            append(""" VALUES ($idDepartureAirport, $idArrivalAirport, $frequencyInDays,  """)
            append(""" TO_TIMESTAMP('$approximateTakeoffTime', 'YYYY-MM-DD HH24:MI:SS.FF'), $approximatePrice) """)
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
            return "approximateFlight".addQuo()
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
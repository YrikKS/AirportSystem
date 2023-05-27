package ru.nsu.group20211.airport_system.domain.flights.models

import entity.addQuo
import ru.nsu.group20211.airport_system.domain.DbEntity
import ru.nsu.group20211.airport_system.domain.DbEntityCompanion
import java.sql.ResultSet
import kotlin.reflect.KClass

data class Airport(
    var id: Int = 0,
    var city: String = "",
    var airportName: String = ""
) : DbEntity {
    override fun customGetId(): Int {
        return id
    }

    override fun insertQuery(): String {
        return """ INSERT INTO ${getTableName()} ("city", "airportName") VALUES ('$city', '$airportName') """
    }

    override fun deleteQuery(): String {
        return """ DELETE FROM ${getTableName()} WHERE "id" = $id """
    }

    override fun updateQuery(): String {
        return buildString {
            append(""" UPDATE ${getTableName()} SET  """)
            append(""" "city" = '$city', """)
            append(""" "airportName" = '$airportName' """)
            append(""" WHERE "id" = $id """)

        }
    }

    override fun myEquals(other: Any): Boolean {
        return equals(other)
    }

    companion object : DbEntityCompanion<Airport> {
        override fun getTableName(): String {
            return "airport".addQuo()
        }

        override fun getAll(): String {
            return super.getAll()
        }

        override fun ResultSet.getInstance(
            indexStart: Int,
            clazz: KClass<Airport>
        ): Pair<Airport, Int> {
            return Airport(
                id = getInt(indexStart),
                city = getString(indexStart + 1),
                airportName = getString(indexStart + 2),
            ) to indexStart + 3
        }
    }
}
package ru.nsu.group20211.airport_system.domain.passengers.models

import ru.nsu.group20211.airport_system.data.addQuo
import ru.nsu.group20211.airport_system.domain.DbEntity
import ru.nsu.group20211.airport_system.domain.DbEntityCompanion
import ru.nsu.group20211.airport_system.domain.flights.models.FlightSchedule
import java.sql.ResultSet
import java.sql.Timestamp
import kotlin.reflect.KClass

data class Ticket(
    var id: Int = 0,
    var passenger: Int = 0,
    var idFlight: Int = 0,
    var realPrice: Float = 0.0F,
    var registrationTime: Timestamp? = null,
    var luggage: Char = 'N',
    var place: Int = 0,

    var passengerEntity: Passenger? = null,
    var schedule: FlightSchedule? = null
) : DbEntity {
    override fun customGetId(): Int {
        return id
    }

    override fun insertQuery(): String {
        return buildString {
            append(""" INSERT INTO ${getTableName()} """)
            append(""" ("passenger", "idFlight", "realPrice(in rubles)", "luggage", "place") """)
            append(""" VALUES """)
            append(""" ($passenger, $idFlight, $realPrice, '$luggage', '$place') """)
        }
    }

    override fun deleteQuery(): String {
        return """ DELETE FROM ${getTableName()} WHERE "id" = $id """
    }

    override fun updateQuery(): String {
        return buildString {
            append(""" UPDATE ${getTableName()} SET """)
            append(""" "passenger" = $passenger, """)
            append(""" "idFlight" = $idFlight, """)
            append(""" "realPrice(in rubles)" = $realPrice, """)
            append(""" "registrationTime" = TO_TIMESTAMP('$registrationTime', 'YYYY-MM-DD HH24:MI:SS.FF'), """)
            append(""" "luggage" = '$luggage', """)
            append(""" "place" = '$place' """)
            append(""" WHERE "id" = $id """)
        }
    }

    override fun myEquals(other: Any): Boolean {
        return equals(other)
    }

    companion object : DbEntityCompanion<Ticket> {
        override fun getTableName(): String {
            return "tickets".addQuo()
        }

        override fun getAll(): String {
            return super.getAll()
        }

        override fun ResultSet.getInstance(
            indexStart: Int,
            clazz: KClass<Ticket>
        ): Pair<Ticket, Int> {
            return Ticket(
                id = getInt(indexStart),
                passenger = getInt(indexStart + 1),
                idFlight = getInt(indexStart + 2),
                realPrice = getFloat(indexStart + 3),
                registrationTime = getTimestamp(indexStart + 4),
                luggage = getString(indexStart + 5)[0],
                place = getInt(indexStart + 6)
            ) to indexStart + 7
        }
    }
}

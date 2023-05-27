package ru.nsu.group20211.airport_system.domain.flights.models

import entity.addQuo
import ru.nsu.group20211.airport_system.domain.DbEntity
import ru.nsu.group20211.airport_system.domain.DbEntityCompanion
import ru.nsu.group20211.airport_system.domain.employee.models.Brigade
import ru.nsu.group20211.airport_system.domain.plane.models.Plane
import java.sql.ResultSet
import java.sql.Timestamp
import kotlin.reflect.KClass

data class FlightSchedule(
    var id: Int,
    var plane: Int,
    var typeFlight: Int,
    var status: Int,
    var brigadePilots: Int,
    var brigadeWorker: Int,
    var idApproximateFlights: Int?,
    var idDepartureAirport: Int?,
    var idArrivalAirport: Int?,
    var takeoffTime: Timestamp?,
    var boardingTime: Timestamp?,
    var price: Float,
    var minNumberTickets: Int,

    var departure: Airport? = null,
    var arrival: Airport? = null,
    var approximateFlight: ApproximateFlight? = null,
    var planeEntity: Plane? = null,
    var pilots: Brigade? = null,
    var workers: Brigade? = null
) : DbEntity {
    override fun customGetId(): Int {
        return id
    }

    override fun insertQuery(): String {
        return buildString {
            append(""" INSERT INTO ${getTableName()} ("plane", "typeFlight", "status", "brigadePilots", "brigadeWorker" """)
            if (idApproximateFlights != null)
                append(""", idApproximateFlights """)
            if (idDepartureAirport != null)
                append(""", idDepartureAirport """)
            if (idArrivalAirport != null)
                append(""", idArrivalAirport  """)
            if (takeoffTime != null)
                append(""", takeoffTime """)
            if (boardingTime != null)
                append(""", boardingTime """)
            append(""", price, minNumberTickets) VALUES (""")
            append(""" $plane, $typeFlight, $status, $brigadePilots, $brigadeWorker """)

            if (idApproximateFlights != null)
                append(""", $idApproximateFlights """)
            if (idDepartureAirport != null)
                append(""", $idDepartureAirport """)
            if (idArrivalAirport != null)
                append(""", $idArrivalAirport  """)
            if (takeoffTime != null)
                append(""", TO_TIMESTAMP('$takeoffTime', 'YYYY-MM-DD HH24:MI:SS.FF') """)
            if (boardingTime != null)
                append(""", TO_TIMESTAMP('$boardingTime', 'YYYY-MM-DD HH24:MI:SS.FF') """)

            append(""", $price, $minNumberTickets) """)
        }
    }

    override fun deleteQuery(): String {
        return """DELETE FROM  ${getTableName()}  WHERE "id" = $id """
    }

    override fun updateQuery(): String {
        return buildString {
            append("""UPDATE ${getTableName()} """)
            append("""SET "plane" = $plane """)
            append(""", "typeFlight" = $typeFlight """)
            append(""", "status" = $status """)
            append(""", "brigadePilots" = $brigadePilots """)
            append(""", "brigadeWorker" = $brigadeWorker """)
            if (idApproximateFlights != null)
                append(""", "idApproximateFlights" = $idApproximateFlights """)
            if (idDepartureAirport != null)
                append(""", "idDepartureAirport" = $idDepartureAirport """)
            if (idArrivalAirport != null)
                append(""", "idArrivalAirport" = $idArrivalAirport """)
            if (takeoffTime != null)
                append(""", "takeoffTime" = TO_TIMESTAMP('$takeoffTime', 'YYYY-MM-DD HH24:MI:SS.FF') """)
            if (boardingTime != null)
                append(""", "boardingTime" = TO_TIMESTAMP('$boardingTime', 'YYYY-MM-DD HH24:MI:SS.FF') """)
            append(""", "price" = $price """)
            append(""", "minNumberTickets" = $minNumberTickets """)
            append(""" WHERE id = $id """)
        }
    }

    override fun myEquals(other: Any): Boolean {
        return equals(other)
    }

    companion object : DbEntityCompanion<FlightSchedule> {
        override fun getTableName(): String {
            return "flightSchedule".addQuo()
        }

        override fun getAll(): String {
            return super.getAll()
        }

        override fun ResultSet.getInstance(
            indexStart: Int,
            clazz: KClass<FlightSchedule>
        ): Pair<FlightSchedule, Int> {
            return FlightSchedule(
                id = getInt(indexStart),
                plane = getInt(indexStart + 1),
                typeFlight = getInt(indexStart + 2),
                status = getInt(indexStart + 3),
                brigadePilots = getInt(indexStart + 4),
                brigadeWorker = getInt(indexStart + 5),
                idApproximateFlights = getInt(indexStart + 6),
                idDepartureAirport = getInt(indexStart + 7),
                idArrivalAirport = getInt(indexStart + 8),
                takeoffTime = getTimestamp(indexStart + 9),
                boardingTime = getTimestamp(indexStart + 10),
                price = getFloat(indexStart + 11),
                minNumberTickets = getInt(indexStart + 12)
            ) to indexStart + 13
        }
    }
}
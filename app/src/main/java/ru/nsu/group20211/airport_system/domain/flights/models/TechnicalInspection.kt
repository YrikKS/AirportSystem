package ru.nsu.group20211.airport_system.domain.flights.models

import entity.addQuo
import ru.nsu.group20211.airport_system.domain.DbEntity
import ru.nsu.group20211.airport_system.domain.DbEntityCompanion
import ru.nsu.group20211.airport_system.domain.employee.models.Brigade
import ru.nsu.group20211.airport_system.domain.plane.models.Plane
import java.sql.ResultSet
import java.sql.Timestamp
import kotlin.reflect.KClass

data class TechnicalInspection(
    var id: Int = 0,
    var idFlight: Int = 0,
    var idInspectionTeam: Int = 0,
    var date: Timestamp? = null,
    var resultInspection: String = "",

    var schedule: FlightSchedule? = null,
    var brigade: Brigade? = null
) : DbEntity {
    override fun customGetId(): Int {
        TODO("Not yet implemented")
    }

    override fun insertQuery(): String {
        return if (date != null)
            """INSERT INTO ${getTableName()} ("idFlight", "idInspectionTeam", "date", "resultInspection")
                | VALUES ($idFlight, $idInspectionTeam, 
                | TO_TIMESTAMP('$date', 'YYYY-MM-DD HH24:MI:SS.FF'), '$resultInspection')""".trimMargin()
        else {
            """INSERT INTO ${getTableName()} ("idFlight", "idInspectionTeam", "resultInspection")
                | VALUES ($idFlight, $idInspectionTeam,
                | '$resultInspection')""".trimMargin()
        }
    }

    override fun deleteQuery(): String {
        return """ DELETE FROM ${getTableName()} WHERE "id" = $id """
    }

    override fun updateQuery(): String {
        return buildString {
            append("""UPDATE ${getTableName()} """)
            append(""" SET "idFlight" = $idFlight """)
            append(""", "idInspectionTeam" = $idInspectionTeam """)
            if (date != null)
                append(""", "date" = TO_TIMESTAMP('$date', 'YYYY-MM-DD HH24:MI:SS.FF') """)
            append(""", "resultInspection" = '$resultInspection' """)
            append(""" WHERE "id" = $id """)
        }
    }

    override fun myEquals(other: Any): Boolean {
        return equals(other)
    }

    companion object : DbEntityCompanion<TechnicalInspection> {
        override fun getTableName(): String {
            return "technicalInspection".addQuo()
        }

        override fun getAll(): String {
            return super.getAll()
        }

        override fun ResultSet.getInstance(
            indexStart: Int,
            clazz: KClass<TechnicalInspection>
        ): Pair<TechnicalInspection, Int> {
            return TechnicalInspection(
                id = getInt(indexStart),
                idFlight = getInt(indexStart + 1),
                idInspectionTeam = getInt(indexStart + 2),
                date = getTimestamp(indexStart + 3),
                resultInspection = getString(indexStart + 4)
            ) to indexStart + 5
        }
    }
}
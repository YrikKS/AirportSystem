package ru.nsu.group20211.airport_system.domain.flights.models

import entity.addQuo
import ru.nsu.group20211.airport_system.domain.DbEntity
import ru.nsu.group20211.airport_system.domain.DbEntityCompanion
import java.sql.ResultSet
import java.sql.Timestamp
import kotlin.reflect.KClass

data class SalonMaintenance(
    var id: Int = 0,
    var idFlight: Int = 0,
    var idCleaningTeamSalon: Int = 0,
    var date: Timestamp? = null,
    var report: String = ""
) : DbEntity {
    override fun customGetId(): Int {
        return id
    }

    override fun insertQuery(): String {
        return buildString {
            append(""" INSERT INTO ${getTableName()} ("idFlight", "idCleaningTeamSalon", """)
            if (date != null)
                append(""" "date", """)
            append(""" "report") VALUES ($idFlight, $idCleaningTeamSalon,  """)
            if (date != null)
                append(""" TO_TIMESTAMP('$date', 'YYYY-MM-DD HH24:MI:SS.FF'), """)
            append(""" $report) """)
        }
    }

    override fun deleteQuery(): String {
        return """ DELETE FROM ${getTableName()} WHERE "id" = $id """
    }

    override fun updateQuery(): String {
        return buildString {
            append(""" UPDATE ${getTableName()} SET "idFlight" = $idFlight """)
            append(""" , "idCleaningTeamSalon" = $idCleaningTeamSalon """)
            append(""" , "date" = TO_TIMESTAMP('$date', 'YYYY-MM-DD HH24:MI:SS.FF') """)
            append(""" , "report" = '$report' """)
            append(""" WHERE "id" = $id """)
        }
    }

    override fun myEquals(other: Any): Boolean {
        return equals(other)
    }

    companion object : DbEntityCompanion<SalonMaintenance> {
        override fun getTableName(): String {
            return "salonMaintenance".addQuo()
        }

        override fun getAll(): String {
            return super.getAll()
        }

        override fun ResultSet.getInstance(
            indexStart: Int,
            clazz: KClass<SalonMaintenance>
        ): Pair<SalonMaintenance, Int> {
            return SalonMaintenance(
                id = getInt(indexStart),
                idFlight = getInt(indexStart + 1),
                idCleaningTeamSalon = getInt(indexStart + 2),
                date = getTimestamp(indexStart + 3),
                report = getString(indexStart + 4),
            ) to indexStart + 5
        }
    }
}
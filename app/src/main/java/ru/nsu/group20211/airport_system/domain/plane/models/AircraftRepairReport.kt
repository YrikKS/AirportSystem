package ru.nsu.group20211.airport_system.domain.plane.models

import ru.nsu.group20211.airport_system.data.addQuo
import ru.nsu.group20211.airport_system.domain.DbEntity
import ru.nsu.group20211.airport_system.domain.DbEntityCompanion
import ru.nsu.group20211.airport_system.domain.employee.models.Brigade
import java.sql.ResultSet
import java.sql.Timestamp
import kotlin.reflect.KClass

data class AircraftRepairReport(
    var id: Int = 0,
    var plane: Int = 0,
    var repairTeam: Int = 0,
    var dateRepair: Timestamp? = null,
    var report: String = "",

    var planeEntity: Plane? = null,
    var brigade: Brigade? = null
) : DbEntity {
    override fun customGetId(): Int {
        TODO("Not yet implemented")
    }

    override fun insertQuery(): String {
        return buildString {
            append("""INSERT INTO ${getTableName()} ("plane", "repairTeam" """)
            if (dateRepair != null) {
                append(""", "dateRepair" """)
            }
            append(""" , "report") """)
            append(""" VALUES ($plane, $repairTeam""")
            if (dateRepair != null) {
                append(""", TO_TIMESTAMP('$dateRepair', 'YYYY-MM-DD HH24:MI:SS.FF') """)
            }
            append(""", '$report') """)
        }
    }

    override fun deleteQuery(): String {
        return """ DELETE FROM ${getTableName()} WHERE "id" = $id """
    }

    override fun updateQuery(): String {
        return buildString {
            append("""UPDATE ${getTableName()} """)
            append("""SET "plane" = $plane""")
            append(""" , "repairTeam" = $repairTeam """)
            if (dateRepair != null)
                append(""" , "dateRepair" = TO_TIMESTAMP('$dateRepair', 'YYYY-MM-DD HH24:MI:SS.FF') """)
            append(""" , "report" = '$report' """)
            append(""" WHERE "id" = $id """)
        }
    }

    override fun myEquals(other: Any): Boolean {
        return equals(other)
    }

    companion object : DbEntityCompanion<AircraftRepairReport> {
        override fun getTableName(): String {
            return "aircraftRepairReports".addQuo()
        }

        override fun getAll(): String {
            return super.getAll()
        }

        override fun ResultSet.getInstance(
            indexStart: Int,
            clazz: KClass<AircraftRepairReport>
        ): Pair<AircraftRepairReport, Int> {
            return AircraftRepairReport(
                id = getInt(indexStart),
                plane = getInt(indexStart + 1),
                repairTeam = getInt(indexStart + 2),
                dateRepair = getTimestamp(indexStart + 3),
                report = getString(indexStart + 4),
            ) to indexStart + 5
        }
    }
}
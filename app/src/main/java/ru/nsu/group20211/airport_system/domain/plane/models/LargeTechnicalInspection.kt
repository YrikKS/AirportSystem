package ru.nsu.group20211.airport_system.domain.plane.models

import entity.addQuo
import ru.nsu.group20211.airport_system.domain.DbEntity
import ru.nsu.group20211.airport_system.domain.DbEntityCompanion
import java.sql.ResultSet
import java.sql.Timestamp
import kotlin.reflect.KClass

data class LargeTechnicalInspection(
    val id: Int,
    val idPlane: Int,
    val idInspectionTeam: Int,
    val date: Timestamp?,
    val scheduledRepairs: Char,
    val resultInspection: String,

    val plane: Plane? = null
) : DbEntity {
    override fun customGetId(): Int {
        TODO("Not yet implemented")
    }

    override fun insertQuery(): String {
        return if (date != null) """INSERT INTO largeTechnicalInspection (idPlane, idInspectionTeam, date, scheduledRepairs, resultInspection)
                | VALUES ($idPlane, $idInspectionTeam, 
                | TO_TIMESTAMP('$date', 'YYYY-MM-DD HH24:MI:SS.FF'), '$scheduledRepairs', '$resultInspection')""".trimMargin()
        else {
            """INSERT INTO largeTechnicalInspection (idPlane, idInspectionTeam, scheduledRepairs, resultInspection)
                | VALUES ($idPlane, $idInspectionTeam,
                | '$scheduledRepairs', '$resultInspection')""".trimMargin()
        }
    }

    override fun deleteQuery(): String {
        return """ DELETE FROM ${getTableName()} WHERE "id" = $id """
    }

    override fun updateQuery(): String {
        return buildString {
            append("""UPDATE "largeTechnicalInspection" """)
            append(""" SET "idPlane" = $idPlane """)
            append("""", "idInspectionTeam" = $idInspectionTeam, """)
            if (date != null)
                append("""", "date" = TO_TIMESTAMP('$date', 'YYYY-MM-DD HH24:MI:SS.FF') """)
            append(""", "scheduledRepairs" = '$scheduledRepairs' """)
            append(""", "resultInspection" = '$resultInspection' """)
            append(""" WHERE "id" = $id """)
        }
    }

    override fun myEquals(other: Any): Boolean {
        return equals(other)
    }

    companion object : DbEntityCompanion<LargeTechnicalInspection> {
        override fun getTableName(): String {
            return "largeTechnicalInspection".addQuo()
        }

        override fun getAll(): String {
            return super.getAll()
        }

        override fun ResultSet.getInstance(
            indexStart: Int,
            clazz: KClass<LargeTechnicalInspection>
        ): Pair<LargeTechnicalInspection, Int> {
            return LargeTechnicalInspection(
                id = getInt(indexStart),
                idPlane = getInt(indexStart + 1),
                idInspectionTeam = getInt(indexStart + 2),
                date = getTimestamp(indexStart + 3),
                scheduledRepairs = getString(indexStart + 4)[0],
                resultInspection = getString(indexStart + 5)
            ) to indexStart + 6
        }
    }
}
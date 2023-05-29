package ru.nsu.group20211.airport_system.domain.plane.models

import ru.nsu.group20211.airport_system.data.addQuo
import ru.nsu.group20211.airport_system.domain.DbEntity
import ru.nsu.group20211.airport_system.domain.DbEntityCompanion
import ru.nsu.group20211.airport_system.domain.employee.models.Brigade
import java.sql.ResultSet
import java.sql.Timestamp
import kotlin.reflect.KClass

data class LargeTechnicalInspection(
    var id: Int = 0,
    var idPlane: Int = 0,
    var idInspectionTeam: Int = 0,
    var date: Timestamp? = null,
    var resultInspection: String = "",

    var planeEntity: Plane? = null,
    var brigade: Brigade? = null
) : DbEntity {
    override fun customGetId(): Int {
        TODO("Not yet implemented")
    }

    override fun insertQuery(): String {
        return if (date != null) """INSERT INTO ${getTableName()} ("idPlane", "idInspectionTeam", "date", "resultInspection")
                | VALUES ($idPlane, $idInspectionTeam, 
                | TO_TIMESTAMP('$date', 'YYYY-MM-DD HH24:MI:SS.FF'), '$resultInspection')""".trimMargin()
        else {
            """INSERT INTO ${getTableName()} ("idPlane", "idInspectionTeam", "resultInspection")
                | VALUES ($idPlane, $idInspectionTeam,
                | '$resultInspection')""".trimMargin()
        }
    }

    override fun deleteQuery(): String {
        return """ DELETE FROM ${getTableName()} WHERE "id" = $id """
    }

    override fun updateQuery(): String {
        return buildString {
            append("""UPDATE ${getTableName()} """)
            append(""" SET "idPlane" = $idPlane """)
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
                resultInspection = getString(indexStart + 4)
            ) to indexStart + 5
        }
    }
}
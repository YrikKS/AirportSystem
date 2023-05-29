package ru.nsu.group20211.airport_system.domain.employee.models

import ru.nsu.group20211.airport_system.data.addQuo
import ru.nsu.group20211.airport_system.domain.DbEntity
import ru.nsu.group20211.airport_system.domain.DbEntityCompanion
import java.sql.ResultSet
import java.util.Date
import kotlin.reflect.KClass

data class MedicalExamination(
    val id: Int = 0,
    val idPilot: Int = 0,
    val healthy: Char = 'Y',
    val conclusion: String? = null,
    val date: Date? = null
) : DbEntity {
    override fun customGetId(): Int {
        return id
    }

    override fun insertQuery(): String {
        return buildString {
            append("""INSERT INTO ${getTableName()} ("idPilot", "healthy" """)
            if (conclusion != null) append(""", "conclusion" """)
            if (date != null) append(""", "date" """)
            append(""") VALUES(""")
            append(""" $idPilot, '$healthy'""")
            if (conclusion != null) append(""", '$conclusion' """)
            if (date != null) append(""", TO_DATE('$date', 'YYYY-MM-DD') """)
            append(")")
        }
    }

    override fun deleteQuery(): String {
        return buildString {
            """ DELETE FROM ${getTableName()} WHERE "id" = $id """
        }
    }

    override fun updateQuery(): String {
        return buildString {
            append(""" UPDATE ${getTableName()} SET """)
            append(""" "idPilot" = $idPilot, """)
            append(""" "healthy" = '$healthy' """)
            if (conclusion != null) append(""", "conclusion" = $conclusion """)
            if (date != null) append(""", "date" = TO_DATE('$date', 'YYYY-MM-DD') """)
            append(""" WHERE "id" =  $id """)
        }
    }

    override fun myEquals(other: Any): Boolean {
        return equals(other)
    }

    companion object : DbEntityCompanion<MedicalExamination> {
        override fun getTableName(): String {
            return "medicalExamination".addQuo()
        }

        override fun getAll(): String {
            return super.getAll()
        }

        override fun ResultSet.getInstance(
            indexStart: Int,
            clazz: KClass<MedicalExamination>
        ): Pair<MedicalExamination, Int> {
            return MedicalExamination(
                id = getInt(indexStart),
                idPilot = getInt(indexStart + 1),
                healthy = getString(indexStart + 2)[0],
                conclusion = getString(indexStart + 3),
                date = getDate(indexStart + 4)
            ) to indexStart + 5
        }
    }
}
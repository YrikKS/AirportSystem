package ru.nsu.group20211.airport_system.domain.models

import entity.addQuo
import java.sql.Date
import java.sql.ResultSet
import kotlin.reflect.KClass

data class Employee(
    var id: Int = 0,
    var idBrigade: Int = 0,
    var dateOfEmployment: Date? = null,
    var humanId: Int = 0,
    var salary: Float = 0f,

    var human: Human? = null,
    var brigade: Brigade? = null
) : DbEntity {
    override fun customGetId(): Int {
        return id
    }

    override fun myEquals(other: Any): Boolean {
        return this == other
    }


    override fun insertQuery() = buildString {
        append("INSERT INTO ${getTableName()} ")
        append("""("idHuman", "idBrigade", "dateOfEmployment", "salary") VALUES (""")
        append("""$humanId, $idBrigade, TO_DATE('${dateOfEmployment}', 'YYYY-MM-DD'), $salary)""")
    }

    override fun deleteQuery() = buildString {
        append("DELETE FROM ")
        append("employees".addQuo())
        append(" ")
        append(" WHERE \"id\" = ${id} ")
    }

    override fun updateQuery() = buildString {
        append("UPDATE ")
        append("employees".addQuo())
        append(" SET ")
        append(""" "idBrigade" = $idBrigade,  """)
        append(""" "dateOfEmployment" = TO_DATE('$dateOfEmployment', 'YYYY-MM-DD'),  """)
        append(""" "idHuman" = $humanId,  """)
        append(""" "salary" = $salary  """)
        append(""" WHERE "id" = $id """)
    }

    companion object : DbEntityCompanion<Employee> {
        override fun getTableName() = "employees".addQuo()

        override fun getAll() = buildString {
            append("SELECT * FROM ${getTableName()} ")
        }

        override fun ResultSet.getInstance(
            indexStart: Int,
            clazz: KClass<Employee>
        ): Pair<Employee, Int> {
            return Employee(
                id = getInt(indexStart),
                idBrigade = getInt(indexStart + 1),
                dateOfEmployment = getDate(indexStart + 2),
                humanId = getInt(indexStart + 3),
                salary = getFloat(indexStart + 4)
            ) to indexStart + 5
        }
    }
}
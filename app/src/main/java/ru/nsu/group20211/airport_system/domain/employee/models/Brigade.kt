package ru.nsu.group20211.airport_system.domain.employee.models

import entity.addQuo
import ru.nsu.group20211.airport_system.domain.DbEntity
import ru.nsu.group20211.airport_system.domain.DbEntityCompanion
import java.sql.ResultSet
import kotlin.reflect.KClass

data class Brigade(
    var id: Int = 0,
    var idDepartment: Int = 0,
    var nameBrigade: String = "",

    var department: Department? = null,
    var avgSalary: Int? = null,
    var sumSalary: Int? = null,
    var countPeople: Int? = null
) : DbEntity {
    override fun customGetId(): Int {
        return id
    }

    override fun myEquals(other: Any): Boolean {
        return this == other
    }

    override fun insertQuery(): String {
        return """ INSERT INTO ${getTableName()} ("idDepartment", "nameBrigade") VALUES(${idDepartment}, '$nameBrigade') """
    }

    override fun deleteQuery(): String {
        return buildString {
            append(""" DELETE FROM ${getTableName()} WHERE "id" = $id """)
        }
    }

    override fun updateQuery(): String {
        return buildString {
            append("UPDATE ")
            append(getTableName())
            append(" SET ")
            append(""" "idDepartment" = $idDepartment, "nameBrigade" = '$nameBrigade' """)
            append(""" WHERE "id" = $id """)
        }
    }

    companion object : DbEntityCompanion<Brigade> {
        fun getById(id: Int) = """ SELECT * FROM ${getTableName()} WHERE "id" = $id """

        override fun getTableName(): String {
            return "brigades".addQuo()
        }

        override fun getAll(): String {
            return super.getAll()
        }

        override fun ResultSet.getInstance(
            indexStart: Int,
            clazz: KClass<Brigade>
        ): Pair<Brigade, Int> {
            return Brigade(
                id = getInt(indexStart),
                idDepartment = getInt(indexStart + 1),
                nameBrigade = getString(indexStart + 2)
            ) to indexStart + 3
        }
    }
}
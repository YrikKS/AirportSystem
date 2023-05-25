package ru.nsu.group20211.airport_system.domain.models

import entity.addQuo
import java.sql.ResultSet
import kotlin.reflect.KClass

data class Brigade(
    var id: Int = 0,
    var idDepartmentEntity: Int = 0,
    var nameBrigade: String = ""
) : DbEntity {
    override fun customGetId(): Int {
        return id
    }

    override fun myEquals(other: Any): Boolean {
        return this == other
    }

    override fun insertQuery(): String {
        return """ INSERT INTO ${getTableName()} ("idDepartment", "nameBrigade") VALUES(${idDepartmentEntity}, $nameBrigade) """
    }

    override fun deleteQuery(): String {
        return buildString {
            """ DELETE FROM ${getTableName()} WHERE "id" = $id; """
        }
    }

    override fun updateQuery(): String {
        return buildString {
            append("UPDATE ")
            append(getTableName())
            append(" SET ")
            append(""" "idDepartmentEntity" = $idDepartmentEntity, "nameBrigade" = $nameBrigade """)
            append(""" WHERE "id" = $id; """)
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
                idDepartmentEntity = getInt(indexStart + 1),
                nameBrigade = getString(indexStart + 2)
            ) to indexStart + 3
        }
    }
}
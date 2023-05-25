package ru.nsu.group20211.airport_system.domain.models

import entity.addQuo
import java.sql.ResultSet
import kotlin.reflect.KClass

data class Department(
    val id: Int = 0,
    val idBoss: Int = 0,
    val nameDepartment: String = ""
) : DbEntity {
    override fun customGetId(): Int {
        return id
    }

    override fun myEquals(other: Any): Boolean {
        return this == other
    }

    override fun insertQuery(): String {
        return buildString {
            append(""" INSERT INTO "departments" ("idBoss", "nameDepartment") VALUES($idBoss, '$nameDepartment'); """)
        }
    }

    override fun deleteQuery(): String {
        return """ DELETE FROM ${getTableName()} WHERE "id" = "$id" """
    }

    override fun updateQuery(): String {
        return buildString {
            append("UPDATE ")
            append(getTableName())
            append(" SET ")
            append(""" "idBoss" = $idBoss, "nameDepartment" = '$nameDepartment' WHERE "id" = $id; """)
        }
    }

    companion object : DbEntityCompanion<Department> {
        override fun getTableName(): String {
            return "departments".addQuo()
        }

        override fun getAll(): String {
            return super.getAll()
        }

        override fun ResultSet.getInstance(
            indexStart: Int,
            clazz: KClass<Department>
        ): Pair<Department, Int> {
            return Department(
                id = getInt(indexStart),
                idBoss = getInt(indexStart + 1),
                nameDepartment = getString(indexStart + 2)
            ) to indexStart + 3
        }
    }

}
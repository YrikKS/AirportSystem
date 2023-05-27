package ru.nsu.group20211.airport_system.domain.flights.models

import entity.addQuo
import ru.nsu.group20211.airport_system.domain.DbEntity
import ru.nsu.group20211.airport_system.domain.DbEntityCompanion
import java.sql.ResultSet
import kotlin.reflect.KClass

data class TypeFule(
    val id: Int = 0,
    val name: String = "",
) : DbEntity {
    override fun customGetId(): Int {
        return id
    }

    override fun insertQuery(): String {
        return """ INSERT INTO ${getTableName()} ("name") VALUES ('$name') """
    }

    override fun deleteQuery(): String {
        return """DELETE FROM ${getTableName()} WHERE "id" = $id """
    }

    override fun updateQuery(): String {
        return """ UPDATE ${getTableName()} SET "name" = '$name' WHERE id = $id """
    }

    override fun myEquals(other: Any): Boolean {
        return equals(other)
    }


    companion object : DbEntityCompanion<TypeFule> {
        override fun getTableName(): String {
            return "typeFule".addQuo()
        }

        override fun getAll(): String {
            return super.getAll()
        }

        override fun ResultSet.getInstance(
            indexStart: Int,
            clazz: KClass<TypeFule>
        ): Pair<TypeFule, Int> {
            return TypeFule(
                id = getInt(indexStart),
                name = getString(indexStart + 1)
            ) to indexStart + 2
        }
    }
}
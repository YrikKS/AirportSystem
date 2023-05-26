package ru.nsu.group20211.airport_system.domain.plane.models

import entity.addQuo
import ru.nsu.group20211.airport_system.domain.DbEntity
import ru.nsu.group20211.airport_system.domain.DbEntityCompanion
import java.sql.ResultSet
import kotlin.reflect.KClass

data class ModelPlane(
    var id: Int = 0,
    var nameModel: String = ""
) : DbEntity {
    override fun customGetId(): Int {
        return id
    }

    override fun insertQuery(): String {
        return """ INSERT INTO ${getTableName()} ("nameModel") VALUES ('$nameModel') """
    }

    override fun deleteQuery(): String {
        return """ DELETE FROM ${getTableName()} WHERE "id" = $id """
    }

    override fun updateQuery(): String {
        return """ UPDATE ${getTableName()} SET "nameModel" = '$nameModel' WHERE "id" = $id """
    }

    override fun myEquals(other: Any): Boolean {
        return equals(other)
    }

    companion object : DbEntityCompanion<ModelPlane> {
        override fun getTableName(): String {
            return "modelPlane".addQuo()
        }

        override fun getAll(): String {
            return super.getAll()
        }

        override fun ResultSet.getInstance(
            indexStart: Int,
            clazz: KClass<ModelPlane>
        ): Pair<ModelPlane, Int> {
            return ModelPlane(
                id = getInt(indexStart),
                nameModel = getString(indexStart + 1)
            ) to indexStart + 2
        }
    }
}
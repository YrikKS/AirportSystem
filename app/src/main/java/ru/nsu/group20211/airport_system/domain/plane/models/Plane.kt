package ru.nsu.group20211.airport_system.domain.plane.models

import entity.addQuo
import ru.nsu.group20211.airport_system.domain.DbEntity
import ru.nsu.group20211.airport_system.domain.DbEntityCompanion
import java.sql.Date
import java.sql.ResultSet
import kotlin.reflect.KClass

data class Plane(
    val id: Int = 0,
    val model: Int = 0,
    val numberPassengerSeats: Int = 0,
    val dateCreation: Date,

    val modelPlane: ModelPlane? = null,
) : DbEntity {
    override fun customGetId(): Int {
        return id
    }

    override fun insertQuery(): String {
        return buildString {
            append(""" INSERT INTO ${getTableName()}( """)
            append(""" "id", "model", "numberPassengerSeats", "dateCreation") """)
            append(""" VALUES ($id, $model, $numberPassengerSeats, TO_DATE('$dateCreation', 'YYYY-MM-DD')) """)
        }
    }

    override fun deleteQuery(): String {
        return """ DELETE FROM ${getTableName()} WHERE "id" = $id; """
    }

    override fun updateQuery(): String {
        return buildString {
            append(""" UPDATE ${getTableName()} SET  """)
            append(""" "model" = $model, """)
            append(""" "numberPassengerSeats" = $numberPassengerSeats, """)
            append(""" "dateCreation" = TO_DATE('$dateCreation', 'YYYY-MM-DD') """)
            append(""" WHERE "id" = $id """)
        }
    }

    override fun myEquals(other: Any): Boolean {
        return equals(other)
    }

    companion object : DbEntityCompanion<Plane> {
        override fun getTableName(): String {
            return "planes".addQuo()
        }

        override fun getAll(): String {
            return super.getAll()
        }

        override fun ResultSet.getInstance(
            indexStart: Int,
            clazz: KClass<Plane>
        ): Pair<Plane, Int> {
            return Plane(
                id = getInt(indexStart),
                model = getInt(indexStart + 1),
                numberPassengerSeats = getInt(indexStart + 2),
                dateCreation = getDate(indexStart + 3)
            ) to indexStart + 4
        }
    }
}
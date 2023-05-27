package ru.nsu.group20211.airport_system.domain.flights.models

import entity.addQuo
import ru.nsu.group20211.airport_system.domain.DbEntity
import ru.nsu.group20211.airport_system.domain.DbEntityCompanion
import java.sql.ResultSet
import java.sql.Timestamp
import kotlin.reflect.KClass

data class Refueling(
    var id: Int = 0,
    var idFlight: Int = 0,
    var typeFuel: Int = 0,
    var idRefuelingTeam: Int = 0,
    var refilledLiters: Float = 0.0F,
    var date: Timestamp? = null,

    var tupeFule: TypeFule? = null
) : DbEntity {

    override fun customGetId(): Int {
        return id
    }

    override fun insertQuery(): String {
        return buildString {
            if (date != null) {
                append("""INSERT INTO ${getTableName()} ("idFlight", "typeFuel", "idRefuelingTeam", "refilledLiters", "date") """)
                append("""  VALUES($idFlight, $typeFuel, $idRefuelingTeam, $refilledLiters, TO_TIMESTAMP('$date', 'YYYY-MM-DD HH24:MI:SS.FF')) """)
            } else {
                append("""INSERT INTO ${getTableName()} ("idFlight", "typeFuel", "idRefuelingTeam", "refilledLiters") """)
                append("""  VALUES($idFlight, $typeFuel, $idRefuelingTeam, $refilledLiters) """)
            }
        }
    }

    override fun deleteQuery(): String {
        return """ DELETE FROM ${getTableName()} WHERE "id" = $id """
    }

    override fun updateQuery(): String {
        return buildString {
            append(""" UPDATE ${getTableName()} SET "idFlight" = $idFlight""")
            append(""", "typeFule" = $typeFuel""")
            append(""", "idRefuelingTeam" = $idRefuelingTeam """)
            append(""", "refilledLiters" = $refilledLiters """)
            if (date != null)
                append(""", "date" = TO_TIMESTAMP('$date', 'YYYY-MM-DD HH24:MI:SS.FF') """)
            append("""WHERE "id" = $id """)
        }
    }

    override fun myEquals(other: Any): Boolean {
        return equals(other)
    }

    companion object : DbEntityCompanion<Refueling> {
        override fun getTableName(): String {
            return "refueling".addQuo()
        }

        override fun getAll(): String {
            return super.getAll()
        }

        override fun ResultSet.getInstance(
            indexStart: Int,
            clazz: KClass<Refueling>
        ): Pair<Refueling, Int> {
            return Refueling(
                id = getInt(indexStart),
                idFlight = getInt(indexStart + 1),
                typeFuel = getInt(indexStart + 2),
                idRefuelingTeam = getInt(indexStart + 3),
                refilledLiters = getFloat(indexStart + 4),
                date = getTimestamp(indexStart + 5),
            ) to indexStart + 6
        }
    }
}
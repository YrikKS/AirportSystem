package ru.nsu.group20211.airport_system.domain.passengers.models

import ru.nsu.group20211.airport_system.data.addQuo
import ru.nsu.group20211.airport_system.domain.DbEntity
import ru.nsu.group20211.airport_system.domain.DbEntityCompanion
import java.sql.Date
import java.sql.ResultSet
import kotlin.reflect.KClass

data class Passenger(
    var id: Int = 0,
    var sex: Char = 'M',
    var name: String = "",
    var surname: String = "",
    var patronymic: String? = null,
    var dateOfBirth: Date? = null
) : DbEntity {

    fun getFIO(): String {
        return buildString {
            append("$surname ")
            append("$name ")
            if (patronymic != null && patronymic != "null")
                append("$patronymic ")
        }
    }

    override fun customGetId(): Int {
        return id
    }

    override fun insertQuery(): String {
        return buildString {
            if (patronymic != null && patronymic!!.isNotEmpty()) {
                append(""" INSERT INTO ${getTableName()} ("sex", "name", "surname", "patronymic", "dateOfBirth") """)
            } else {
                append(""" INSERT INTO ${getTableName()} ("sex", "name", "surname", "dateOfBirth") """)
            }

            if (patronymic != null && patronymic!!.isNotEmpty()) {
                append(""" VALUES ('$sex', '$name', '$surname', '$patronymic', TO_DATE('$dateOfBirth', 'YYYY-MM-DD'))""")
            } else {
                append(""" VALUES ('$sex', '$name', '$surname', TO_DATE('$dateOfBirth', 'YYYY-MM-DD')) """)
            }


        }
    }

    override fun deleteQuery(): String {
        return """ DELETE FROM ${getTableName()} WHERE "id" = $id """
    }

    override fun updateQuery(): String {
        return buildString {
            append(""" UPDATE ${getTableName()} SET """)
            append(""" "sex" = '$sex', """)
            append(""" "name" = '$name', """)
            append(""" "surname" = '$surname', """)
            append(""" "patronymic" = '$patronymic', """)
            append(""" "dateOfBirth" = TO_DATE('$dateOfBirth', 'YYYY-MM-DD') """)
            append(""" WHERE "id" = $id """)

        }
    }

    override fun myEquals(other: Any): Boolean {
        return equals(other)
    }

    companion object : DbEntityCompanion<Passenger> {
        override fun getTableName(): String {
            return "passengers".addQuo()
        }

        override fun getAll(): String {
            return super.getAll()
        }

        override fun ResultSet.getInstance(
            indexStart: Int,
            clazz: KClass<Passenger>
        ): Pair<Passenger, Int> {
            return Passenger(
                id = getInt(indexStart),
                sex = getString(indexStart + 1)[0],
                name = getString(indexStart + 2),
                surname = getString(indexStart + 3),
                patronymic = getString(indexStart + 4),
                dateOfBirth = getDate(indexStart + 5),
            ) to indexStart + 6
        }
    }
}
package ru.nsu.group20211.airport_system.domain.employee.models

import android.os.Parcelable
import entity.addQuo
import kotlinx.parcelize.Parcelize
import ru.nsu.group20211.airport_system.domain.DbEntity
import ru.nsu.group20211.airport_system.domain.DbEntityCompanion
import java.sql.Date
import java.sql.ResultSet
import java.util.Calendar
import kotlin.reflect.KClass

@Parcelize
data class Human(
    var id: Int = 0,
    var name: String = "",
    var surname: String = "",
    var patronymic: String? = null,
    var sex: Char = 'M',
    var dateOfBirth: Date? = Date(Calendar.getInstance().time.time),
    var countChildren: Int = 0,
) : DbEntity, Parcelable {

    override fun customGetId(): Int {
        return id
    }

    override fun myEquals(other: Any): Boolean {
        return this == other
    }

    override fun insertQuery() = buildString {
        append("INSERT INTO ${getTableName()} ")
        if (dateOfBirth != null) {
            append("""("name", "surname", "patronymic", "sex", "dateOfBirth", "countChildren") VALUES (""")
        } else {
            append("""("name", "surname", "patronymic", "sex", "countChildren") VALUES (""")
        }
        append("""'${name}', '${surname}', '${patronymic}', '${sex}', """)
        if (dateOfBirth != null)
            append("""TO_DATE('${dateOfBirth}', 'YYYY-MM-DD'), """)
        append("""${countChildren})""")
    }

    override fun deleteQuery() = buildString {
        append("DELETE FROM ")
        append(getTableName())
        append(" ")
        append("WHERE \"id\" = ${id}")
    }

    override fun updateQuery() = buildString {
        append("UPDATE ")
        append(getTableName())
        append(" SET ")
        append(""" "name" = '$name', """)
        append(""" "surname" = '$surname', """)
        append(""" "patronymic" = '$patronymic', """)
        append(""" "sex" = '$sex',  """)
        if (dateOfBirth != null)
            append(""" "dateOfBirth" = TO_DATE('${dateOfBirth}', 'YYYY-MM-DD'),  """)
        append(""" "countChildren" = $countChildren  """)
        append(""" WHERE "id" = $id """)
    }

    companion object : DbEntityCompanion<Human> {
        override fun getTableName() = "human".addQuo()

        override fun getAll() = buildString {
            append("SELECT * FROM ${getTableName()} ")
        }


        fun getById(id: Int) = "SELECT * FROM ${getTableName()} WHERE \"id\" = $id "

        override fun ResultSet.getInstance(
            indexStart: Int,
            clazz: KClass<Human>
        ): Pair<Human, Int> {
            return Human(
                id = getInt(indexStart),
                name = getString(indexStart + 1),
                surname = getString(indexStart + 2),
                patronymic = getString(indexStart + 3),
                sex = getString(indexStart + 4)[0],
                dateOfBirth = getDate(indexStart + 5),
                countChildren = getInt(indexStart + 6)
            ) to indexStart + 7
        }

        fun ResultSet.getHuman(indexStart: Int = 1): Pair<Human, Int> {
            return Human(
                id = getInt(indexStart),
                name = getString(indexStart + 1),
                surname = getString(indexStart + 2),
                patronymic = getString(indexStart + 3),
                sex = getString(indexStart + 4)[0],
                dateOfBirth = getDate(indexStart + 5),
                countChildren = getInt(indexStart + 6)
            ) to indexStart + 7
        }
    }
}

fun human(block: Human.() -> Unit) = Human().apply(block)
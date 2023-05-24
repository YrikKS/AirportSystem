package ru.nsu.group20211.airport_system.domain.models

import entity.addQuo
import java.sql.Date
import java.sql.Timestamp
import java.util.Calendar

sealed class Employee1(
    var employeeId: Int = 0,
    var name: String = "",
    var surname: String = "",
    var pyrtonymic: String? = null,
    var sex: Char = 'M',
    var dateOfBirth: Date = Date(Calendar.getInstance().time.time),
    var countChildren: Int = 0,

    var humanId: Int = 0,
    var idBrigade: Int = 0,
    var dateOfEmployment: Timestamp? = null,
    var salary: Float = 0f
) {
    class EmptyHuman : Employee1()
    class EmptyEmployee1 : Employee1()
    data class Techniques(var qualification: String = "") : Employee1()
    data class Cashiers(var numberLanguages: Int = 0) : Employee1()
    data class Pilots(var licenseCategory: String = "", var rating: String = "") : Employee1()
    class Administrator : Employee1()
    data class Security(
        var weaponsPermission: Boolean = false,
        var militaryService: Boolean = false
    ) : Employee1()

    data class Dispatchers(var numberLanguages: Int = 0) : Employee1()
    data class Other(var typeWorker: String = "") : Employee1()


    private fun getTableName() = when (this) {
        is Administrator -> "administrators".addQuo()
        is Cashiers -> "cashiers".addQuo()
        is Dispatchers -> "dispatchers".addQuo()
        is Other -> "othersEmployees".addQuo()
        is Pilots -> "pilots".addQuo()
        is Security -> "securityServiceEmployees".addQuo()
        is Techniques -> "techniques".addQuo()
        is EmptyEmployee1 -> "employees".addQuo()
        is EmptyHuman -> "human".addQuo()
    }

    fun queryForDelAll() =
        listOf(
            buildString {
                append("DELETE FROM ")
                append(getTableName())
                append(" ")
                append("WHERE \"employee\" = ${employeeId};")
            },
            buildString {
                append("DELETE FROM ")
                append("employees".addQuo())
                append(" ")
                append("WHERE \"id\" = ${employeeId};")
            },
            buildString {
                append("DELETE FROM ")
                append("human".addQuo())
                append(" ")
                append("WHERE \"id\" = ${humanId};")
            },
        )

    fun queryInsert() =
        buildString {
            append("INSERT INTO ${getTableName()} ")
            when (this@Employee1) {
                is Administrator -> append("""("employee") VALUES (""")
                is Cashiers -> append("""("employee", "numberLanguages") VALUES (""")
                is Dispatchers -> append("""("employee, "numberLanguages") VALUES (""")
                is Other -> append("""("employee", "typeWorker") VALUES (""")
                is Pilots -> append("""("employee", "licenseCategory", "rating") VALUES (""")
                is Security -> append("""("employee", "weaponsPermission", "militaryService") VALUES (""")
                is Techniques -> append("""("employee", "qualification") VALUES (""")
                is EmptyEmployee1 -> append("""("idHuman", "idBrigade", "dateOfEmployment", "salary") VALUES (""")
                is EmptyHuman -> append("""("name", "surname", "pyrtonymic", "sex", "dateOfBirth", "countChildren") VALUES (""")
            }
            when (this@Employee1) {
                is Administrator -> append("""$employeeId);""")
                is Cashiers -> append("""$employeeId, $numberLanguages);""")
                is Dispatchers -> append("""$employeeId, $numberLanguages);""")
                is Other -> append("""$employeeId, "$typeWorker");""")
                is Pilots -> append("""$employeeId, "$licenseCategory", "$rating");""")
                is Security -> append("""$employeeId, $weaponsPermission, $militaryService);""")
                is Techniques -> append("""$employeeId, "$qualification");""")
                is EmptyEmployee1 -> append("""$humanId, $idBrigade, TO_DATE('${dateOfEmployment}', 'YYYY-MM-DD'), $salary);""")
                is EmptyHuman -> append("""'${name}', '${surname}', '${pyrtonymic}', '${sex}', TO_DATE('${dateOfBirth}', 'YYYY:MM:DD'), ${countChildren});""")
            }
        }
}
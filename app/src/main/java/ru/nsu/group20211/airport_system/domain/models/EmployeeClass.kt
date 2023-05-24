package ru.nsu.group20211.airport_system.domain.models

import entity.addQuo
import java.sql.ResultSet

sealed class EmployeeClass : DbEntity {
    var employee: Int = 0

    data class Techniques(var qualification: String = "") : EmployeeClass() {
        companion object  : DbEntityCompanion{
            override fun getTableName(): String {
                TODO("Not yet implemented")
            }

            override fun getAll(): String {
                return super.getAll()
            }
        }
    }

    data class Cashiers(var numberLanguages: Int = 0) : EmployeeClass()
    data class Pilots(var licenseCategory: String = "", var rating: String = "") : EmployeeClass()
    data class Administrator(var id: Int) : EmployeeClass()
    data class Security(
        var weaponsPermission: Boolean = false,
        var militaryService: Boolean = false
    ) : EmployeeClass()

    data class Dispatchers(var numberLanguages: Int = 0) : EmployeeClass()
    data class Other(var typeWorker: String = "") : EmployeeClass()

    override fun insertQuery() = buildString {
        append("INSERT INTO ${getTableName()} ")
        when (this@EmployeeClass) {
            is EmployeeClass.Administrator -> append("""("employee") VALUES (""")
            is EmployeeClass.Cashiers -> append("""("employee", "numberLanguages") VALUES (""")
            is EmployeeClass.Dispatchers -> append("""("employee, "numberLanguages") VALUES (""")
            is EmployeeClass.Other -> append("""("employee", "typeWorker") VALUES (""")
            is EmployeeClass.Pilots -> append("""("employee", "licenseCategory", "rating") VALUES (""")
            is EmployeeClass.Security -> append("""("employee", "weaponsPermission", "militaryService") VALUES (""")
            is EmployeeClass.Techniques -> append("""("employee", "qualification") VALUES (""")
        }
        when (this@EmployeeClass) {
            is EmployeeClass.Administrator -> append("""$employee);""")
            is EmployeeClass.Cashiers -> append("""$employee, $numberLanguages);""")
            is EmployeeClass.Dispatchers -> append("""$employee, $numberLanguages);""")
            is EmployeeClass.Other -> append("""$employee, '$typeWorker');""")
            is EmployeeClass.Pilots -> append("""$employee, '$licenseCategory', '$rating');""")
            is EmployeeClass.Security -> append("""$employee, $weaponsPermission, $militaryService);""")
            is EmployeeClass.Techniques -> append("""$employee, '$qualification');""")
        }
    }

    override fun deleteQuery() = buildString {
        append("DELETE FROM ")
        append(getTableName())
        append(" ")
        if (this@EmployeeClass is Administrator) append(""" WHERE "id" = $id  """) else
            append("WHERE \"employee\" = ${employee};")
    }

    override fun updateQuery() = buildString {
        append("UPDATE ")
        append(getTableName())
        append(" SET ")
        when (this@EmployeeClass) {
            is Administrator -> append(""" "employee" = $employee """)
            is Cashiers -> append(""" "employee" = $employee, "numberLanguages" = '$numberLanguages' """)
            is Dispatchers -> append(""" "employee" = $employee, "numberLanguages" = '$numberLanguages' """)
            is Other -> append(""" "employee" = $employee, "typeWorker" = '$typeWorker' """)
            is Pilots -> append(""" "employee" = $employee, "licenseCategory" = '$licenseCategory', "rating" = '$rating' """)
            is Security -> append(""" "employee" = $employee, "weaponsPermission" = '$weaponsPermission', "militaryService" = '$militaryService' """)
            is Techniques -> append(""" "employee" = $employee, "qualification" = '$qualification' """)
        }
        if (this@EmployeeClass !is Administrator) {
            append(""" WHERE  "employee" = $employee; """)
        } else {
            append(""" WHERE  "id" = $id; """)
        }
    }

    fun getAll(tableName: String): String {
        return buildString {
            append(""" SELECT * FROM $tableName """)
        }
    }

    fun getTableName() = when (this) {
        is EmployeeClass.Administrator -> "administrators".addQuo()
        is EmployeeClass.Cashiers -> "cashiers".addQuo()
        is EmployeeClass.Dispatchers -> "dispatchers".addQuo()
        is EmployeeClass.Other -> "othersEmployees".addQuo()
        is EmployeeClass.Pilots -> "pilots".addQuo()
        is EmployeeClass.Security -> "securityServiceEmployees".addQuo()
        is EmployeeClass.Techniques -> "techniques".addQuo()
    }


    companion object {
        fun getAllTableList() = listOf(
            "techniques",
            "cashiers",
            "pilots",
            "securityServiceEmployees",
            "othersEmployees",
            "dispatchers",
            "administrators"
        )

        fun ResultSet.getEmployeeClass(
            indexStart: Int,
            tableName: String
        ): Pair<EmployeeClass, Int> {
            return when (tableName) {
                "techniques" -> {
                    EmployeeClass.Techniques(
                        qualification = getString(indexStart + 1)
                    ).apply { employee = getInt(indexStart) } to indexStart + 2
                }

                "cashiers" -> {
                    EmployeeClass.Cashiers(
                        numberLanguages = getInt(indexStart + 1)
                    ).apply {
                        employee = getInt(indexStart)
                    } to indexStart + 2
                }

                "pilots" -> {
                    EmployeeClass.Pilots(
                        licenseCategory = getString(indexStart + 1),
                        rating = getString(indexStart + 2)
                    ).apply {
                        employee = getInt(indexStart)
                    } to indexStart + 3
                }

                "securityServiceEmployees" -> {
                    EmployeeClass.Security(
                        weaponsPermission = getBoolean(indexStart + 1),
                        militaryService = getBoolean(indexStart + 2)
                    ).apply {
                        employee = getInt(indexStart)
                    } to indexStart + 3
                }

                "othersEmployees" -> {
                    EmployeeClass.Other(
                        typeWorker = getString(indexStart + 1)
                    ).apply {
                        employee = getInt(indexStart)
                    } to indexStart + 2
                }

                "dispatchers" -> {
                    EmployeeClass.Dispatchers(
                        numberLanguages = getInt(indexStart + 1)
                    ).apply {
                        employee = getInt(indexStart)
                    } to indexStart + 2
                }

                "administrators" -> {
                    EmployeeClass.Administrator(
                        getInt(indexStart)
                    ).apply {
                        employee = getInt(indexStart + 1)
                    } to indexStart + 2
                }

                else -> {
                    throw IllegalStateException("Undefined table")
                }
            }
        }
    }
}
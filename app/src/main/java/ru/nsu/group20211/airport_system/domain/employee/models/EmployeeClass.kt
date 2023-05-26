package ru.nsu.group20211.airport_system.domain.employee.models

import entity.addQuo
import ru.nsu.group20211.airport_system.domain.DbEntity
import ru.nsu.group20211.airport_system.domain.DbEntityCompanion
import ru.nsu.group20211.airport_system.domain.employee.models.EmployeeClass.Administrator.Companion.getInstance
import ru.nsu.group20211.airport_system.domain.employee.models.EmployeeClass.Cashiers.Companion.getInstance
import ru.nsu.group20211.airport_system.domain.employee.models.EmployeeClass.Dispatchers.Companion.getInstance
import ru.nsu.group20211.airport_system.domain.employee.models.EmployeeClass.Other.Companion.getInstance
import ru.nsu.group20211.airport_system.domain.employee.models.EmployeeClass.Pilots.Companion.getInstance
import ru.nsu.group20211.airport_system.domain.employee.models.EmployeeClass.Security.Companion.getInstance
import ru.nsu.group20211.airport_system.domain.employee.models.EmployeeClass.Techniques.Companion.getInstance
import java.sql.ResultSet
import kotlin.reflect.KClass

sealed class EmployeeClass : DbEntity {
    var employee: Int = 0
    var employeeEntity: Employee? = null

    override fun customGetId() = employee

    fun myCopy(): EmployeeClass {
        return when (this) {
            is Administrator -> Administrator(id).apply {
                this.employee = this@EmployeeClass.employee
            }

            is Cashiers -> Cashiers(numberLanguages).apply {
                this.employee = this@EmployeeClass.employee
            }

            is Dispatchers -> Dispatchers(numberLanguages).apply {
                this.employee = this@EmployeeClass.employee
            }

            is Other -> Other(typeWorker).apply { this.employee = this@EmployeeClass.employee }
            is Pilots -> Pilots(licenseCategory, rating).apply {
                this.employee = this@EmployeeClass.employee
            }

            is Security -> Security(weaponsPermission, militaryService).apply {
                this.employee = this@EmployeeClass.employee
            }

            is Techniques -> Techniques(qualification).apply {
                this.employee = this@EmployeeClass.employee
            }
        }
    }

    data class Techniques(var qualification: String = "") : EmployeeClass() {
        override fun myEquals(other: Any): Boolean {
            return this == other
        }

        companion object : DbEntityCompanion<Techniques> {
            override fun getTableName(): String {
                return "techniques".addQuo()
            }

            override fun getAll(): String {
                return super.getAll()
            }

            override fun ResultSet.getInstance(
                indexStart: Int,
                clazz: KClass<Techniques>
            ): Pair<Techniques, Int> {
                return Techniques(
                    qualification = getString(indexStart + 1)
                ).apply { employee = getInt(indexStart) } to indexStart + 2
            }
        }
    }

    data class Cashiers(var numberLanguages: Int = 0) : EmployeeClass() {
        override fun myEquals(other: Any): Boolean {
            return this == other
        }

        companion object : DbEntityCompanion<Cashiers> {
            override fun getTableName(): String {
                return "cashiers".addQuo()
            }

            override fun getAll(): String {
                return super.getAll()
            }

            override fun ResultSet.getInstance(
                indexStart: Int,
                clazz: KClass<Cashiers>
            ): Pair<Cashiers, Int> {
                return Cashiers(
                    numberLanguages = getInt(indexStart + 1)
                ).apply {
                    employee = getInt(indexStart)
                } to indexStart + 2
            }
        }
    }

    data class Pilots(var licenseCategory: String = "", var rating: String = "") : EmployeeClass() {
        override fun myEquals(other: Any): Boolean {
            return this == other
        }

        companion object : DbEntityCompanion<Pilots> {
            override fun getTableName(): String {
                return "pilots".addQuo()
            }

            override fun getAll(): String {
                return super.getAll()
            }

            override fun ResultSet.getInstance(
                indexStart: Int,
                clazz: KClass<Pilots>
            ): Pair<Pilots, Int> {
                return Pilots(
                    licenseCategory = getString(indexStart + 1),
                    rating = getString(indexStart + 2)
                ).apply {
                    employee = getInt(indexStart)
                } to indexStart + 3
            }
        }
    }

    data class Administrator(var id: Int = 0) : EmployeeClass() {
        companion object : DbEntityCompanion<Administrator> {
            override fun getTableName(): String {
                return "administrators".addQuo()
            }

            override fun getAll(): String {
                return super.getAll()
            }

            override fun ResultSet.getInstance(
                indexStart: Int,
                clazz: KClass<Administrator>
            ): Pair<Administrator, Int> {
                return Administrator(
                    getInt(indexStart)
                ).apply {
                    employee = getInt(indexStart + 1)
                } to indexStart + 2
            }
        }

        override fun myEquals(other: Any): Boolean {
            return this == other
        }

    }

    data class Security(
        var weaponsPermission: Boolean = false,
        var militaryService: Boolean = false
    ) : EmployeeClass() {
        override fun myEquals(other: Any): Boolean {
            return this == other
        }

        companion object : DbEntityCompanion<Security> {
            override fun getTableName(): String {
                return "securityServiceEmployees".addQuo()
            }

            override fun getAll(): String {
                return super.getAll()
            }

            override fun ResultSet.getInstance(
                indexStart: Int,
                clazz: KClass<Security>
            ): Pair<Security, Int> {
                return Security(
                    weaponsPermission = getBoolean(indexStart + 1),
                    militaryService = getBoolean(indexStart + 2)
                ).apply {
                    employee = getInt(indexStart)
                } to indexStart + 3
            }
        }
    }

    data class Dispatchers(var numberLanguages: Int = 0) : EmployeeClass() {
        override fun myEquals(other: Any): Boolean {
            return this == other
        }

        companion object : DbEntityCompanion<Dispatchers> {
            override fun getTableName(): String {
                return "dispatchers".addQuo()
            }

            override fun getAll(): String {
                return super.getAll()
            }

            override fun ResultSet.getInstance(
                indexStart: Int,
                clazz: KClass<Dispatchers>
            ): Pair<Dispatchers, Int> {
                return Dispatchers(
                    numberLanguages = getInt(indexStart + 1)
                ).apply {
                    employee = getInt(indexStart)
                } to indexStart + 2
            }
        }
    }

    data class Other(var typeWorker: String = "") : EmployeeClass() {
        override fun myEquals(other: Any): Boolean {
            return this == other
        }

        companion object : DbEntityCompanion<Other> {
            override fun getTableName(): String {
                return "othersEmployees".addQuo()
            }

            override fun getAll(): String {
                return super.getAll()
            }

            override fun ResultSet.getInstance(
                indexStart: Int,
                clazz: KClass<Other>
            ): Pair<Other, Int> {
                return Other(
                    typeWorker = getString(indexStart + 1)
                ).apply {
                    employee = getInt(indexStart)
                } to indexStart + 2
            }
        }
    }

    override fun insertQuery() = buildString {
        append("INSERT INTO ${getTableName()} ")
        when (this@EmployeeClass) {
            is Administrator -> append("""("employee") VALUES (""")
            is Cashiers -> append("""("employee", "numberLanguages") VALUES (""")
            is Dispatchers -> append("""("employee, "numberLanguages") VALUES (""")
            is Other -> append("""("employee", "typeWorker") VALUES (""")
            is Pilots -> append("""("employee", "licenseCategory", "rating") VALUES (""")
            is Security -> append("""("employee", "weaponsPermission", "militaryService") VALUES (""")
            is Techniques -> append("""("employee", "qualification") VALUES (""")
        }
        when (this@EmployeeClass) {
            is Administrator -> append("""$employee) """)
            is Cashiers -> append("""$employee, $numberLanguages) """)
            is Dispatchers -> append("""$employee, $numberLanguages) """)
            is Other -> append("""$employee, '$typeWorker') """)
            is Pilots -> append("""$employee, '$licenseCategory', '$rating') """)
            is Security -> append("""$employee, $weaponsPermission, $militaryService) """)
            is Techniques -> append("""$employee, '$qualification') """)
        }
    }

    override fun deleteQuery() = buildString {
        append("DELETE FROM ")
        append(getTableName())
        append(" ")
        if (this@EmployeeClass is Administrator) append(""" WHERE "id" = $id  """) else
            append("WHERE \"employee\" = ${employee} ")
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
            append(""" WHERE  "employee" = $employee  """)
        } else {
            append(""" WHERE  "id" = $id  """)
        }
    }

    fun getAll(tableName: String): String {
        return buildString {
            append(""" SELECT * FROM $tableName """)
        }
    }

    fun getTableName() = when (this) {
        is Administrator -> "administrators".addQuo()
        is Cashiers -> "cashiers".addQuo()
        is Dispatchers -> "dispatchers".addQuo()
        is Other -> "othersEmployees".addQuo()
        is Pilots -> "pilots".addQuo()
        is Security -> "securityServiceEmployees".addQuo()
        is Techniques -> "techniques".addQuo()
    }


    companion object {
        fun getAllQuery() = listOf<Pair<String, (Int, ResultSet) -> Pair<EmployeeClass, Int>>>(
            Techniques.getAll() to { index: Int, result: ResultSet ->
                result.getInstance(index, Techniques::class)
            },
            Cashiers.getAll() to { index: Int, result: ResultSet ->
                result.getInstance(index, Cashiers::class)
            },
            Pilots.getAll() to { index: Int, result: ResultSet ->
                result.getInstance(index, Pilots::class)
            },
            Security.getAll() to { index: Int, result: ResultSet ->
                result.getInstance(index, Security::class)
            },
            Other.getAll() to { index: Int, result: ResultSet ->
                result.getInstance(index, Other::class)
            },
            Dispatchers.getAll() to { index: Int, result: ResultSet ->
                result.getInstance(index, Dispatchers::class)
            },
            Administrator.getAll() to { index: Int, result: ResultSet ->
                result.getInstance(index, Administrator::class)
            },
        )

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
                    Techniques(
                        qualification = getString(indexStart + 1)
                    ).apply { employee = getInt(indexStart) } to indexStart + 2
                }

                "cashiers" -> {
                    Cashiers(
                        numberLanguages = getInt(indexStart + 1)
                    ).apply {
                        employee = getInt(indexStart)
                    } to indexStart + 2
                }

                "pilots" -> {
                    Pilots(
                        licenseCategory = getString(indexStart + 1),
                        rating = getString(indexStart + 2)
                    ).apply {
                        employee = getInt(indexStart)
                    } to indexStart + 3
                }

                "securityServiceEmployees" -> {
                    Security(
                        weaponsPermission = getBoolean(indexStart + 1),
                        militaryService = getBoolean(indexStart + 2)
                    ).apply {
                        employee = getInt(indexStart)
                    } to indexStart + 3
                }

                "othersEmployees" -> {
                    Other(
                        typeWorker = getString(indexStart + 1)
                    ).apply {
                        employee = getInt(indexStart)
                    } to indexStart + 2
                }

                "dispatchers" -> {
                    Dispatchers(
                        numberLanguages = getInt(indexStart + 1)
                    ).apply {
                        employee = getInt(indexStart)
                    } to indexStart + 2
                }

                "administrators" -> {
                    Administrator(
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
package ru.nsu.group20211.airport_system.data

import entity.addOrderBy
import entity.addWhere
import entity.log
import ru.nsu.group20211.airport_system.di.app_module.DatabaseModule
import ru.nsu.group20211.airport_system.domain.models.Employee
import ru.nsu.group20211.airport_system.domain.models.Employee.Companion.getInstance
import ru.nsu.group20211.airport_system.domain.models.Employee1
import javax.inject.Inject


class EmployeeRepository @Inject constructor(
    private val dbContainer: DatabaseModule.DriverContainer
) {

    fun getEmployees(
        listCond: List<String> = emptyList(),
        listOrder: List<String> = emptyList()
    ): List<Employee> {
        val listResult = mutableListOf<Employee>()
        dbContainer.connect().use {
            val result =
                it.executeQuery((Employee.getAll() + addWhere(listCond) + addOrderBy(listOrder)).log())
            while (result.next()) {
                listResult.add(result.getInstance(clazz = Employee::class).first)
            }
        }
        return listResult
    }

    fun deleteEmployee(
        employee: Employee
    ) {
        dbContainer.connect().use {
            it.execute((employee.deleteQuery()).log())
        }
    }

    fun insertEmployee(
        employee: Employee,
    ) {
        dbContainer.connect().use {
            it.execute((employee.insertQuery()).log())
        }
    }

    fun updateEmployee(
        employee: Employee
    ) {
        dbContainer.connect().use {
            it.execute((employee.updateQuery()).log())
        }
    }


    fun insert(employee1: Employee1) {
        dbContainer.connect().use {
            it.execute(employee1.queryInsert())
        }
    }

    fun getAllHuman(): List<Employee1> {
        val listResult = mutableListOf<Employee1>()
        dbContainer.connect()
            .use {
                val result = it.executeQuery("""SELECT * FROM "human" """.log())
                while (result.next()) {
                    listResult.add(Employee1.EmptyHuman().apply {
                        humanId = result.getInt(1)
                        name = result.getString(2)
                        surname = result.getString(3)
                        pyrtonymic = result.getString(4)
                        sex = result.getString(5)[0]
                        dateOfBirth = result.getDate(6)
                        countChildren = result.getInt(7)
                    })
                }
            }
        return listResult
    }

    fun getAllEmployee(): List<Employee1> {
        val listResult = mutableListOf<Employee1>()
        dbContainer.connect()
            .use {
                val result = it.executeQuery("""SELECT * FROM "employees" """.log())
                while (result.next()) {
                    listResult.add(Employee1.EmptyEmployee1().apply {
                        employeeId = result.getInt(1)
                        idBrigade = result.getInt(2)
                        dateOfEmployment = result.getTimestamp(3)
                        humanId = result.getInt(4)
                        salary = result.getFloat(5)
                    })
                }
            }
        return listResult
    }

    fun getCountEmployees(): Int {
        return dbContainer.connect()
            .use {
                it.executeQuery("""SELECT COUNT(*) FROM "employess"  """.log()).let { count ->
                    count.next()
                    count.getInt(1)
                }
            }
    }

    fun getAll(
        listCond: List<String> = emptyList(),
        listOrder: List<String> = emptyList()
    ): List<Employee1> {
        val resultList = mutableListOf<Employee1>()
        listOf(
            "techniques",
            "cashiers",
            "pilots",
            "securityServiceEmployees",
            "othersEmployees",
            "dispatchers",
            "administrators"
        ).forEachIndexed { index, it ->
            dbContainer.connect()
                .use { use ->
                    val result = use.executeQuery(
                        ("""SELECT "employees".*, "human".*, "$it".* 
                                FROM "$it" 
                                LEFT JOIN "employees" ON ("employee" = "employees"."id") 
                                LEFT JOIN  "human" ON ("employees"."idHuman" = "human"."id") 
                                ${addWhere(listCond)}""").log()
                    )

                    while (result.next()) {
                        when (index) {
                            0 -> Employee1.Techniques().apply {
                                employeeId = result.getInt(1)
                                idBrigade = result.getInt(2)
                                dateOfEmployment = result.getTimestamp(3)
                                humanId = result.getInt(4)
                                salary = result.getFloat(5)
                                name = result.getString(7)
                                surname = result.getString(8)
                                pyrtonymic = result.getString(9)
                                sex = result.getString(10)[0]
                                dateOfBirth = result.getDate(11)
                                countChildren = result.getInt(12)
                                qualification = result.getString(14)
                            }

                            1 -> Employee1.Cashiers().apply {
                                employeeId = result.getInt(1)
                                idBrigade = result.getInt(2)
                                dateOfEmployment = result.getTimestamp(3)
                                humanId = result.getInt(4)
                                salary = result.getFloat(5)
                                name = result.getString(7)
                                surname = result.getString(8)
                                pyrtonymic = result.getString(9)
                                sex = result.getString(10)[0]
                                dateOfBirth = result.getDate(11)
                                countChildren = result.getInt(12)
                                numberLanguages = result.getInt(14)
                            }

                            2 -> Employee1.Pilots().apply {
                                employeeId = result.getInt(1)
                                idBrigade = result.getInt(2)
                                dateOfEmployment = result.getTimestamp(3)
                                humanId = result.getInt(4)
                                salary = result.getFloat(5)
                                name = result.getString(7)
                                surname = result.getString(8)
                                pyrtonymic = result.getString(9)
                                sex = result.getString(10)[0]
                                dateOfBirth = result.getDate(11)
                                countChildren = result.getInt(12)
                                licenseCategory = result.getString(14)
                                rating = result.getString(15)
                            }

                            3 -> Employee1.Security().apply {
                                employeeId = result.getInt(1)
                                idBrigade = result.getInt(2)
                                dateOfEmployment = result.getTimestamp(3)
                                humanId = result.getInt(4)
                                salary = result.getFloat(5)
                                name = result.getString(7)
                                surname = result.getString(8)
                                pyrtonymic = result.getString(9)
                                sex = result.getString(10)[0]
                                dateOfBirth = result.getDate(11)
                                countChildren = result.getInt(12)
                                weaponsPermission = result.getBoolean(14)
                                militaryService = result.getBoolean(15)
                            }

                            4 -> Employee1.Other().apply {
                                employeeId = result.getInt(1)
                                idBrigade = result.getInt(2)
                                dateOfEmployment = result.getTimestamp(3)
                                humanId = result.getInt(4)
                                salary = result.getFloat(5)
                                name = result.getString(7)
                                surname = result.getString(8)
                                pyrtonymic = result.getString(9)
                                sex = result.getString(10)[0]
                                dateOfBirth = result.getDate(11)
                                countChildren = result.getInt(12)
                                typeWorker = result.getString(14)
                            }

                            5 -> Employee1.Dispatchers().apply {
                                employeeId = result.getInt(1)
                                idBrigade = result.getInt(2)
                                dateOfEmployment = result.getTimestamp(3)
                                humanId = result.getInt(4)
                                salary = result.getFloat(5)
                                name = result.getString(7)
                                surname = result.getString(8)
                                pyrtonymic = result.getString(9)
                                sex = result.getString(10)[0]
                                dateOfBirth = result.getDate(11)
                                countChildren = result.getInt(12)
                                numberLanguages = result.getInt(14)
                            }

                            6 -> Employee1.Administrator().apply {
                                employeeId = result.getInt(1)
                                idBrigade = result.getInt(2)
                                dateOfEmployment = result.getTimestamp(3)
                                humanId = result.getInt(4)
                                salary = result.getFloat(5)
                                name = result.getString(7)
                                surname = result.getString(8)
                                pyrtonymic = result.getString(9)
                                sex = result.getString(10)[0]
                                dateOfBirth = result.getDate(11)
                                countChildren = result.getInt(12)
                            }

                            else -> {
                                null
                            }
                        }?.let { it1 -> resultList.add(it1) }
                    }
                }
        }
        return resultList
    }
}
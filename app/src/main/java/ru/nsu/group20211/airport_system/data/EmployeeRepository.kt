package ru.nsu.group20211.airport_system.data

import entity.addJoins
import entity.addOrderBy
import entity.addWhere
import entity.log
import ru.nsu.group20211.airport_system.di.app_module.DatabaseModule
import ru.nsu.group20211.airport_system.domain.models.Brigade
import ru.nsu.group20211.airport_system.domain.models.Brigade.Companion.getInstance
import ru.nsu.group20211.airport_system.domain.models.Employee
import ru.nsu.group20211.airport_system.domain.models.Employee.Companion.getInstance
import ru.nsu.group20211.airport_system.domain.models.EmployeeClass
import ru.nsu.group20211.airport_system.domain.models.Human
import ru.nsu.group20211.airport_system.domain.models.Human.Companion.getInstance
import javax.inject.Inject


class EmployeeRepository @Inject constructor(
    private val dbContainer: DatabaseModule.DriverContainer
) {

    fun getEmployees(
        listCond: List<String> = emptyList(),
        listOrder: List<String> = emptyList()
    ): List<Employee> {
        val listResult = mutableListOf<Employee>()
        var joinList = listOf(
            """LEFT JOIN ${Human.getTableName()} ON (${Employee.getTableName()}."idHuman" = ${Human.getTableName()}."id") """,
            """LEFT JOIN ${Brigade.getTableName()} ON (${Employee.getTableName()}."idBrigade" = ${Brigade.getTableName()}."id") """
        )
        dbContainer.connect().use {
            val result =
                it.executeQuery(
                    (Employee.getAll() + addJoins(joinList) + addWhere(listCond) + addOrderBy(
                        listOrder
                    )).log()
                )
            while (result.next()) {
                val (employee, index) = result.getInstance(clazz = Employee::class)
                val (human, index_2) = result.getInstance(index, clazz = Human::class)
                val (brigade, index_3) = result.getInstance(index_2, clazz = Brigade::class)
                employee.human = human
                employee.brigade = brigade
                listResult.add(employee)
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


    fun getAllClassEmployee(): List<EmployeeClass> {
        val listResult = mutableListOf<EmployeeClass>()
        dbContainer.connect()
            .use {
                EmployeeClass.getAllQuery().forEach { query ->
                    val result = it.executeQuery(query.first.log())
                    while (result.next()) {
                        listResult.add(query.second(1, result).first)
                    }
                }
            }
        return listResult
    }


    fun getCountEmployees(): Int {
        return dbContainer.connect()
            .use {
                it.executeQuery("""SELECT COUNT(*) FROM "employees"  """.log()).let { count ->
                    count.next()
                    count.getInt(1)
                }
            }
    }

    fun getBrigades(): List<Brigade> {
        return dbContainer.connect().use {
            val listResult = mutableListOf<Brigade>()
            val result = it.executeQuery(Brigade.getAll().log())
            while (result.next()) {
                listResult.add(result.getInstance(clazz = Brigade::class).first)
            }
            listResult
        }
    }

    fun getMinSalary(): Float {
        return dbContainer.connect().use {
            val result =
                it.executeQuery("""SELECT MIN("salary") FROM ${Employee.getTableName()}""".log())
            result.next()
            result.getFloat(1)
        }
    }

    fun getMaxSalary(): Float {
        return dbContainer.connect().use {
            val result =
                it.executeQuery("""SELECT MAX("salary") FROM ${Employee.getTableName()}""".log())
            result.next()
            result.getFloat(1)
        }
    }

    fun getMinExperience(): Int {
        return dbContainer.connect().use {
            val result =
                it.executeQuery("""SELECT MIN(EXTRACT (YEAR FROM SYSDATE) - EXTRACT (YEAR FROM "employees"."dateOfEmployment")) FROM ${Employee.getTableName()} """.log())
            result.next()
            result.getInt(1)
        }
    }

    fun getMaxExperience(): Int {
        return dbContainer.connect().use {
            val result =
                it.executeQuery("""SELECT MAX(EXTRACT (YEAR FROM SYSDATE) - EXTRACT (YEAR FROM "employees"."dateOfEmployment")) FROM ${Employee.getTableName()}""".log())
            result.next()
            result.getInt(1)
        }
    }
}
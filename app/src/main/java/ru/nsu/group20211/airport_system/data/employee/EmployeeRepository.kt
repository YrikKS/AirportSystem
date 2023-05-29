package ru.nsu.group20211.airport_system.data.employee

import ru.nsu.group20211.airport_system.data.addJoins
import ru.nsu.group20211.airport_system.data.addOrderBy
import ru.nsu.group20211.airport_system.data.addWhere
import ru.nsu.group20211.airport_system.data.log
import ru.nsu.group20211.airport_system.data.Repository
import ru.nsu.group20211.airport_system.di.app_module.DatabaseModule
import ru.nsu.group20211.airport_system.domain.employee.models.Brigade
import ru.nsu.group20211.airport_system.domain.employee.models.Brigade.Companion.getInstance
import ru.nsu.group20211.airport_system.domain.employee.models.Department
import ru.nsu.group20211.airport_system.domain.employee.models.Department.Companion.getInstance
import ru.nsu.group20211.airport_system.domain.employee.models.Employee
import ru.nsu.group20211.airport_system.domain.employee.models.Employee.Companion.getInstance
import ru.nsu.group20211.airport_system.domain.employee.models.EmployeeClass
import ru.nsu.group20211.airport_system.domain.employee.models.Human
import ru.nsu.group20211.airport_system.domain.employee.models.Human.Companion.getInstance
import javax.inject.Inject


class EmployeeRepository @Inject constructor(
    override val dbContainer: DatabaseModule.DriverContainer
) : Repository<Employee> {

    override suspend fun getAll(
        listCond: List<String>,
        listOrder: List<String>
    ): List<Employee> {
        val listResult = mutableListOf<Employee>()
        var joinList = listOf(
            """LEFT JOIN ${Human.getTableName()} ON (${Employee.getTableName()}."idHuman" = ${Human.getTableName()}."id") """,
            """LEFT JOIN ${Brigade.getTableName()} ON (${Employee.getTableName()}."idBrigade" = ${Brigade.getTableName()}."id") """,
            """LEFT JOIN ${Department.getTableName()} ON (${Department.getTableName()}."id" = ${Brigade.getTableName()}."idDepartment") """
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
                val (department, index_4) = result.getInstance(index_3, clazz = Department::class)
                employee.human = human
                employee.brigade = brigade
                brigade.department = department
                listResult.add(employee)
            }
        }
        return listResult
    }

    override fun delete(
        element: Employee
    ) {
        dbContainer.connect().use {
            it.execute((element.deleteQuery()).log())
        }
    }

    override fun insert(
        element: Employee,
    ) {
        dbContainer.connect().use {
            it.execute((element.insertQuery()).log())
        }
    }

    override fun update(
        element: Employee
    ) {
        dbContainer.connect().use {
            it.execute((element.updateQuery()).log())
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
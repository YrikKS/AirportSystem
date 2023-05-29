package ru.nsu.group20211.airport_system.data.employee

import ru.nsu.group20211.airport_system.data.addJoins
import ru.nsu.group20211.airport_system.data.addOrderBy
import ru.nsu.group20211.airport_system.data.addWhere
import ru.nsu.group20211.airport_system.data.log
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import ru.nsu.group20211.airport_system.data.Repository
import ru.nsu.group20211.airport_system.di.app_module.DatabaseModule
import ru.nsu.group20211.airport_system.domain.employee.models.Brigade
import ru.nsu.group20211.airport_system.domain.employee.models.Department
import ru.nsu.group20211.airport_system.domain.employee.models.Department.Companion.getInstance
import ru.nsu.group20211.airport_system.domain.employee.models.Employee
import ru.nsu.group20211.airport_system.domain.employee.models.Employee.Companion.getInstance
import ru.nsu.group20211.airport_system.domain.employee.models.EmployeeClass
import ru.nsu.group20211.airport_system.domain.employee.models.EmployeeClass.Administrator.Companion.getInstance
import ru.nsu.group20211.airport_system.domain.employee.models.Human
import ru.nsu.group20211.airport_system.domain.employee.models.Human.Companion.getInstance
import javax.inject.Inject

class DepartmentRepository @Inject constructor(
    override val dbContainer: DatabaseModule.DriverContainer
) : Repository<Department> {
    override suspend fun getAll(
        listCond: List<String>,
        listOrder: List<String>
    ): List<Department> {
        val listResult = mutableListOf<Department>()
        val joinList = listOf(
            """LEFT JOIN ${EmployeeClass.Administrator.getTableName()} ON (${EmployeeClass.Administrator.getTableName()}."id" = ${Department.getTableName()}."idBoss") """,
            """LEFT JOIN ${Employee.getTableName()} ON (${Employee.getTableName()}."id" = ${EmployeeClass.Administrator.getTableName()}."employee") """,
            """LEFT JOIN ${Human.getTableName()} ON ( ${Human.getTableName()}."id" = ${Employee.getTableName()}."idHuman") """
        )
        dbContainer.connect().use {
            val result =
                it.executeQuery(
                    (Department.getAll() + addJoins(joinList) + addWhere(listCond) + addOrderBy(
                        listOrder
                    )).log()
                )
            while (result.next()) {
                val (department, index) = result.getInstance(clazz = Department::class)
                val (administrator, index_2) = result.getInstance(
                    index,
                    clazz = EmployeeClass.Administrator::class
                )
                val (employee, index_3) = result.getInstance(index_2, clazz = Employee::class)
                val (human, index_4) = result.getInstance(index_3, clazz = Human::class)
                department.administrator = administrator
                department.administrator?.employeeEntity = employee
                department.administrator?.employeeEntity?.human = human
                listResult.add(department)
            }
        }
        listResult.forEach {
            coroutineScope {
                launch {
                    launch {
                        dbContainer.connect().use { connect ->
                            val result = connect.executeQuery(
                                """ SELECT count(*) FROM ${Department.getTableName()} 
                                | RIGHT JOIN ${Brigade.getTableName()} ON (${Brigade.getTableName()}."idDepartment" = ${Department.getTableName()}."id" ) 
                                | RIGHT JOIN ${Employee.getTableName()} ON (${Employee.getTableName()}."idBrigade" = ${Brigade.getTableName()}."id" ) 
                                | WHERE ${Department.getTableName()}."id" = ${it.id}""".trimMargin().log()
                            )
                            if (result.next()) {
                                it.countPeople = result.getInt(1)
                            } else {
                                it.countPeople = 0
                            }
                        }
                    }
                }.join()
            }
        }
        return listResult
    }


    suspend fun getAdministrators(): List<EmployeeClass.Administrator> {
        val listResult = mutableListOf<EmployeeClass.Administrator>()
        val joinList = listOf(
            """LEFT JOIN ${Employee.getTableName()} ON (${Employee.getTableName()}."id" = "employee") """,
            """LEFT JOIN ${Human.getTableName()} ON ( ${Human.getTableName()}."id" = ${Employee.getTableName()}."idHuman") """
        )
        return dbContainer.connect().use {
            val result =
                it.executeQuery((EmployeeClass.Administrator.getAll() + addJoins(joinList)).log())
            while (result.next()) {
                val (classEmployee, index) = result.getInstance(clazz = EmployeeClass.Administrator::class)
                val (employee, index_2) = result.getInstance(index, clazz = Employee::class)
                val (human, index_3) = result.getInstance(index_2, clazz = Human::class)
                classEmployee.employeeEntity = employee
                classEmployee.employeeEntity?.human = human
                listResult.add(classEmployee)
            }
            listResult
        }
    }
}
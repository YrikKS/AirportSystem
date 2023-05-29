package ru.nsu.group20211.airport_system.data.employee

import ru.nsu.group20211.airport_system.data.addJoins
import ru.nsu.group20211.airport_system.data.addOrderBy
import ru.nsu.group20211.airport_system.data.addWhere
import ru.nsu.group20211.airport_system.data.log
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.nsu.group20211.airport_system.data.Repository
import ru.nsu.group20211.airport_system.di.app_module.DatabaseModule
import ru.nsu.group20211.airport_system.domain.employee.models.Employee
import ru.nsu.group20211.airport_system.domain.employee.models.Employee.Companion.getInstance
import ru.nsu.group20211.airport_system.domain.employee.models.EmployeeClass
import ru.nsu.group20211.airport_system.domain.employee.models.EmployeeClass.Administrator.Companion.getInstance
import ru.nsu.group20211.airport_system.domain.employee.models.Human
import ru.nsu.group20211.airport_system.domain.employee.models.Human.Companion.getInstance
import javax.inject.Inject

class EmployeeClassRepository @Inject constructor(
    override val dbContainer: DatabaseModule.DriverContainer
) : Repository<EmployeeClass> {

    override suspend fun getAll(
        listCond: List<String>,
        listOrder: List<String>
    ): List<EmployeeClass> {
        val listEmployees = mutableListOf<EmployeeClass>()
        val joinList = listOf(
            """LEFT JOIN ${Employee.getTableName()} ON (${Employee.getTableName()}."id" = "employee") """,
            """LEFT JOIN ${Human.getTableName()} ON ( ${Human.getTableName()}."id" = ${Employee.getTableName()}."idHuman") """
        )
        val mutex = Mutex()
        coroutineScope {
            launch {
                launch {
                    dbContainer.connect().use {
                        val result = it.executeQuery(
                            (EmployeeClass.Administrator.getAll() + addJoins(joinList) + addWhere(
                                listCond
                            ) + addOrderBy(
                                listOrder
                            )).log()
                        )
                        while (result.next()) {
                            val (classEmployee, index) = result.getInstance(clazz = EmployeeClass.Administrator::class)
                            val (employee, index_2) = result.getInstance(
                                index,
                                clazz = Employee::class
                            )
                            val (human, index_3) = result.getInstance(
                                index_2,
                                clazz = Human::class
                            )
                            classEmployee.employeeEntity = employee
                            classEmployee.employeeEntity?.human = human
                            mutex.withLock {
                                listEmployees.add(classEmployee)
                            }
                        }
                    }
                }
                EmployeeClass.getAllQuery().dropLast(1).forEach { query ->
                    launch {
                        dbContainer.connect().use {
                            val result = it.executeQuery(
                                (query.first + addJoins(joinList) + addWhere(listCond) + addOrderBy(
                                    listOrder
                                ).log())
                            )
                            while (result.next()) {
                                val (classEmployee, index) = query.second(1, result)
                                val (employee, index_2) = result.getInstance(
                                    index,
                                    clazz = Employee::class
                                )
                                val (human, index_3) = result.getInstance(
                                    index_2,
                                    clazz = Human::class
                                )
                                classEmployee.employeeEntity = employee
                                classEmployee.employeeEntity?.human = human
                                mutex.withLock {
                                    listEmployees.add(classEmployee)
                                }
                            }
                        }
                    }
                }
            }.join()
        }
        return listEmployees
    }

    suspend fun getEmployees(): List<Employee> {
        return dbContainer.connect().use {
            val listResult = mutableListOf<Employee>()
            val joinList =
                listOf("""LEFT JOIN ${Human.getTableName()} ON ( ${Human.getTableName()}."id" = ${Employee.getTableName()}."idHuman") """)
            val result = it.executeQuery(Employee.getAll() + addJoins(joinList))
            while (result.next()) {
                val (employee, index) = result.getInstance(clazz = Employee::class)
                val (human, index_2) = result.getInstance(index, clazz = Human::class)
                employee.human = human
                listResult.add(employee)
            }
            listResult
        }
    }
}
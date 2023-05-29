package ru.nsu.group20211.airport_system.data.employee

import ru.nsu.group20211.airport_system.data.addJoins
import ru.nsu.group20211.airport_system.data.addOrderBy
import ru.nsu.group20211.airport_system.data.addWhere
import ru.nsu.group20211.airport_system.data.log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import ru.nsu.group20211.airport_system.data.Repository
import ru.nsu.group20211.airport_system.di.app_module.DatabaseModule
import ru.nsu.group20211.airport_system.domain.employee.models.Brigade
import ru.nsu.group20211.airport_system.domain.employee.models.Brigade.Companion.getInstance
import ru.nsu.group20211.airport_system.domain.employee.models.Department
import ru.nsu.group20211.airport_system.domain.employee.models.Department.Companion.getInstance
import ru.nsu.group20211.airport_system.domain.employee.models.Employee
import javax.inject.Inject

class BrigadeRepository @Inject constructor(
    override val dbContainer: DatabaseModule.DriverContainer
) : Repository<Brigade> {
    override suspend fun getAll(
        listCond: List<String>,
        listOrder: List<String>
    ): List<Brigade> {
        val listResult = mutableListOf<Brigade>()
        val joinList = listOf(
            """LEFT JOIN ${Department.getTableName()} ON (${Brigade.getTableName()}."idDepartment" = ${Department.getTableName()}."id") """,
        )
        dbContainer.connect().use {
            val result =
                it.executeQuery(
                    (Brigade.getAll() + addJoins(joinList) + addWhere(listCond) + addOrderBy(
                        listOrder
                    )).log()
                )
            while (result.next()) {
                val (brigade, index) = result.getInstance(clazz = Brigade::class)
                val department = result.getInstance(index, clazz = Department::class).first
                brigade.department = department
                listResult.add(brigade)
            }
        }
        coroutineScope {
            launch {
                listResult.forEach { brigade ->
                    launch(Dispatchers.IO) {
                        val sum = async { getSumSalary(brigade) }
                        val avg = async { getAvgSalary(brigade) }
                        val countPeople = async { getCountPeople(brigade) }
                        brigade.sumSalary = sum.await()
                        brigade.avgSalary = avg.await()
                        brigade.countPeople = countPeople.await()
                    }
                }
            }.join()
        }
        return listResult
    }

    suspend fun getCountPeople(brigade: Brigade): Int {
        return dbContainer.connect().use {
            val result =
                it.executeQuery(
                    """ SELECT COUNT(*) FROM ${Employee.getTableName()} 
                    |WHERE "idBrigade" = ${brigade.id}""".trimMargin().log()
                )
            if (result.next()) {
                result.getInt(1)
            } else {
                0
            }
        }
    }

    suspend fun getSumSalary(brigade: Brigade): Int {
        return dbContainer.connect().use {
            val result = it.executeQuery(
                """ SELECT SUM("salary") FROM ${Employee.getTableName()}  
                | LEFT JOIN ${Brigade.getTableName()} ON (${Employee.getTableName()}."idBrigade" = ${Brigade.getTableName()}."id") 
                | WHERE ${Employee.getTableName()}."idBrigade" = ${brigade.id}""".trimMargin().log()
            )
            if (result.next()) {
                result.getInt(1)
            } else {
                0
            }
        }
    }

    suspend fun getAvgSalary(brigade: Brigade): Int {
        return dbContainer.connect().use {
            val result = it.executeQuery(
                """ SELECT AVG("salary") FROM ${Employee.getTableName()}  
                |LEFT JOIN ${Brigade.getTableName()} ON (${Employee.getTableName()}."idBrigade" = ${Brigade.getTableName()}."id") 
                |WHERE ${Employee.getTableName()}."idBrigade" = ${brigade.id}""".trimMargin().log()
            )
            if (result.next()) {
                result.getInt(1)
            } else {
                0
            }
        }
    }

    fun getDepartment(): List<Department> {
        return dbContainer.connect().use {
            val listResult = mutableListOf<Department>()
            val result = it.executeQuery(Department.getAll().log())
            while (result.next()) {
                listResult.add(result.getInstance(clazz = Department::class).first)
            }
            listResult
        }
    }

    fun getCountBrigades(): Int {
        return dbContainer.connect().use {
            val result =
                it.executeQuery(""" SELECT COUNT(*) FROM ${Brigade.getTableName()} """.log())
            if (result.next()) {
                result.getInt(1)
            } else {
                0
            }
        }
    }
}
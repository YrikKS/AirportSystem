package entity

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column

object OthersEmployees : IdTable<Int>(name = "othersEmployees".addQuo()) {
    override val id: Column<EntityID<Int>> = reference("employee".addQuo(), Employees.id)
    override val primaryKey = PrimaryKey(id)
    val typeWorker = varchar("typeWorker".addQuo(), 255)
}

class OthersEmployeeEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<OthersEmployeeEntity>(OthersEmployees)

    var employee by EmployeeEntity referencedOn OthersEmployees.id
    var typeWorker by OthersEmployees.typeWorker
}
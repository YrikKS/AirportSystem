package entity

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column

object Dispatchers : IdTable<Int>(name = "dispatchers".addQuo()) {
    override val id: Column<EntityID<Int>> = reference("employee".addQuo(), Employees.id)
    override val primaryKey = PrimaryKey(id)
    val numberLanguages = integer("numberLanguages".addQuo())
}

class DispatcherEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<DispatcherEntity>(Dispatchers)

    var employee by EmployeeEntity referencedOn Dispatchers.id
    var numberLanguages by Dispatchers.numberLanguages
}
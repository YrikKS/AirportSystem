package entity

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column

object Administrators : IdTable<Int>(name = "administrators".addQuo()) {
    override val id: Column<EntityID<Int>> = reference("employee".addQuo(), Employees.id)
    override val primaryKey = PrimaryKey(id)
}

class AdministratorEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<AdministratorEntity>(Administrators)

    var employee by EmployeeEntity referencedOn Administrators.id
}
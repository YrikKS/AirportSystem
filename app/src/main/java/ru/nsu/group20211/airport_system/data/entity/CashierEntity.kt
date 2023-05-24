package entity

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column

object Cashiers : IdTable<Int>(name = "cashiers".addQuo()) {
    override val id: Column<EntityID<Int>> = reference("employee".addQuo(), Employees.id)
    override val primaryKey = PrimaryKey(id)
    val numberLanguages = integer("numberLanguages".addQuo()).nullable()
}

class CashierEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<CashierEntity>(Cashiers)

    var employee by EmployeeEntity referencedOn Cashiers.id
    var numberLanguages by Cashiers.numberLanguages
}
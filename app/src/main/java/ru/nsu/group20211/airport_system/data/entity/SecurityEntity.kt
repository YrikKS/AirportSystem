package entity

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.or

object SecurityServiceEmployees : IdTable<Int>(name = "securityServiceEmployees".addQuo()) {
    override val id: Column<EntityID<Int>> = reference("employee".addQuo(), Employees.id)
    override val primaryKey = PrimaryKey(id)
    val weaponsPermit = char("weaponsPermit".addQuo()).check { (it eq 'N') or (it eq 'Y') }
    val militaryService = char("militaryService".addQuo()).check { (it eq 'N') or (it eq 'Y') }
}

class SecurityEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<SecurityEntity>(SecurityServiceEmployees)

    var employee by EmployeeEntity referencedOn SecurityServiceEmployees.id
    var weaponsPermit by SecurityServiceEmployees.weaponsPermit
    var militaryService by SecurityServiceEmployees.militaryService
}
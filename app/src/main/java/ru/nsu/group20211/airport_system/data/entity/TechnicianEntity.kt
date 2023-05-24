package entity

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column

object Techniques : IdTable<Int>(name = "techniques".addQuo()) {
    override val id: Column<EntityID<Int>> = reference("employee".addQuo(), Employees.id)
    override val primaryKey = PrimaryKey(id)
    val qualification = varchar("qualification".addQuo(), 255)
}

class TechnicianEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<TechnicianEntity>(Techniques)

    var employee by EmployeeEntity referencedOn Techniques.id
    var qualification by Techniques.qualification
}
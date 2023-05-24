package entity

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column

object Pilots : IdTable<Int>(name = "pilots".addQuo()) {
    override val id: Column<EntityID<Int>> = reference("employee".addQuo(), Employees.id)
    override val primaryKey = PrimaryKey(id)
    val licenseCategory = varchar("licenseСategory".addQuo(), 255).nullable()
    val rating = varchar("rating".addQuo(), 255).nullable()
}

class PilotEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<PilotEntity>(Pilots)

    var employee by EmployeeEntity referencedOn Pilots.id
    var licenseCategory by Pilots.licenseCategory
    var rating by Pilots.rating
}
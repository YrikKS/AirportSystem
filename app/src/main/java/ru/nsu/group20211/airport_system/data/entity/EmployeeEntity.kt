package entity

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.date

object Employees : IntIdTable(name = "employees".addQuo(), columnName = "id".addQuo()) {
    val idHuman = reference("idHuman".addQuo(), Humans.id).uniqueIndex()
    val idBrigade = reference("idBrigade".addQuo(), Brigades.id)
    val dateOfEmployment = date("dateOfEmployment".addQuo()).nullable()
    val salary = float("salary".addQuo()).default(0F)
}

class EmployeeEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<EmployeeEntity>(Employees)

    var idHuman by HumanEntity referencedOn Employees.idHuman
    var idBrigade by BrigadeEntity referencedOn Employees.idBrigade
    var dateOfEmployment by Employees.dateOfEmployment
    var salary by Employees.salary
}

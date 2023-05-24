package entity

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object Brigades : IntIdTable(name = "brigades".addQuo(), columnName = "id".addQuo()) {
    val idDepartment = reference("idDepartment".addQuo(), Departments.id)
    val nameBrigade = varchar("nameBrigade".addQuo(), 255)
}

class BrigadeEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<BrigadeEntity>(Brigades)

    var idDepartment by DepartmentEntity referencedOn Brigades.idDepartment
    var nameBrigade by Brigades.nameBrigade
}
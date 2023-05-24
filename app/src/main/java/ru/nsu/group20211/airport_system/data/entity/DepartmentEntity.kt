package entity

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable


object Departments : IntIdTable(name = "departments".addQuo(), columnName = "id".addQuo()) {
    val idBoss = reference("idBoss".addQuo(), Administrators.id)
    val nameDepartment = varchar("nameDepartment".addQuo(), 255)
}

class DepartmentEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<DepartmentEntity>(Departments)

    var idBoss by AdministratorEntity referencedOn Departments.idBoss
    var nameDepartment by Departments.nameDepartment
}
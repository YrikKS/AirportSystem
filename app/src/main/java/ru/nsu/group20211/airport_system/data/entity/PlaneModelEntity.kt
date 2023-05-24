package entity

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object PlaneModels : IntIdTable(name = "modelPlane".addQuo(), columnName = "id".addQuo()) {
    val nameModel = varchar("nameModel".addQuo(), 255).index()
}

class PlaneModelEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<PlaneModelEntity>(PlaneModels)

    var nameModel by PlaneModels.nameModel
}
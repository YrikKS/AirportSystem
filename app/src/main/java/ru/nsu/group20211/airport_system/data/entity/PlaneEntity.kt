package entity

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.date

object Planes : IntIdTable(name = "planes".addQuo(), columnName = "id".addQuo()) {
    val model = reference("model".addQuo(), PlaneModels.id)
    val numberPassengerSeats = integer("numberPassengerSeats".addQuo()).default(0)
    val dateCreation = date("dateCreation".addQuo()).nullable()
}

class PlaneEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<PlaneEntity>(Planes)

    var model by PlaneModelEntity referencedOn Planes.model
    var numberPassengerSeats by Planes.numberPassengerSeats
    var dateCreation by Planes.dateCreation
}
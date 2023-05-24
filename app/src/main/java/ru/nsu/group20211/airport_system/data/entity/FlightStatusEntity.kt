package entity

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object FlightStatus : IntIdTable(name = "flightStatus".addQuo(), columnName = "id".addQuo()) {
    val flightStatus = varchar("flightStatus".addQuo(), 255)
}

class FlightStatusEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<FlightStatusEntity>(FlightStatus)

    var flightStatus by FlightStatus.flightStatus
}
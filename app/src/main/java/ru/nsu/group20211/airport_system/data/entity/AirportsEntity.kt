package entity

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object Airports : IntIdTable("airports".addQuo(), "id".addQuo()) {
    val city = varchar("city".addQuo(), 50)
    val name = varchar("airportName".addQuo(), 50)
}

class AirportsEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<AirportsEntity>(Airports)

    var name by Airports.name
    var city by Airports.city
}
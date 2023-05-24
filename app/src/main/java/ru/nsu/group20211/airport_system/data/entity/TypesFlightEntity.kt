package entity

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object TypesFlights : IntIdTable(name = "typesFlights".addQuo(), columnName = "id".addQuo()) {
    val title = varchar("title".addQuo(), 255).nullable()
}

class TypesFlightEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<TypesFlightEntity>(TypesFlights)

    var title by TypesFlights.title
}
package entity

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

object TypeFuel : IntIdTable("typeFuel".addQuo(), columnName = "id".addQuo()) {
    val name: Column<String> = varchar("name".addQuo(), 255)
}

class TypeFuelEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<TypeFuelEntity>(TypeFuel)

    var name by TypeFuel.name
}
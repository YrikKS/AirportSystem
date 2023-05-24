package entity

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object RefuelingTable : IntIdTable("refueling".addQuo(), "id".addQuo()) {
    val idFlight = integer("idFlight".addQuo())
    val typeFule = integer("typeFule".addQuo())
    val idRefuelingTeam = integer("idRefuelingTeam".addQuo())
    val refilledLiters = float("refilledLiters".addQuo()).default(0.0f)
    val date = datetime("date".addQuo()).nullable()
}

class RefuelingEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<RefuelingEntity>(RefuelingTable)

    var idFlight by RefuelingTable.idFlight
    var tyoeFule by RefuelingTable.typeFule
    var idRefuelingTeam by RefuelingTable.idRefuelingTeam
    var refilledLiters by RefuelingTable.refilledLiters
    var date by RefuelingTable.date
}
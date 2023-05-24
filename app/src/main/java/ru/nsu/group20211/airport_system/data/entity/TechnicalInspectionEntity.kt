package entity

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import org.jetbrains.exposed.sql.or


object TechnicalInspections : IntIdTable("technicalInspection".addQuo(), "id".addQuo()) {
    val idFlight = integer("idFlight".addQuo())
    val idInspectionTeam = reference("idInspectionTeam".addQuo(), Brigades)
    val date = timestamp("date".addQuo())
    val scheduledRepairs = char("scheduledRepairs".addQuo()).default('N').check { (it eq 'N') or (it eq 'Y') }
    val resultInspection = varchar("resultInspection".addQuo(), length = 4000).nullable()
}

class TechnicalInspectionEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<TechnicalInspectionEntity>(TechnicalInspections)

    var idFlight by TechnicalInspections.idFlight
    var idInspectionTeam by BrigadeEntity referencedOn TechnicalInspections.idInspectionTeam
    var date by TechnicalInspections.date
    var scheduledRepairs by TechnicalInspections.scheduledRepairs
    var resultInspection by TechnicalInspections.resultInspection
}
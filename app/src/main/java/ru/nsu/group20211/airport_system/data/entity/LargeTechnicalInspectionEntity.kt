package entity

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentTimestamp
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import org.jetbrains.exposed.sql.or

object LargeTechnicalInspections : IntIdTable("largeTechnicalInspection".addQuo(), columnName = "id".addQuo()) {
    val idPlane = reference("idPlane".addQuo(), Planes.id)
    val idInspectionTeam = reference("idInspectionTeam".addQuo(), Brigades)
    val date = timestamp("date".addQuo()).defaultExpression(CurrentTimestamp())
    val scheduledRepairs = char("scheduledRepairs".addQuo()).check { (it eq 'N') or (it eq 'Y') }.default('N')
    val resultInspection = varchar("resultInspection".addQuo(), 2000)
}

class LargeTechnicalInspectionEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<LargeTechnicalInspectionEntity>(LargeTechnicalInspections)

    var idPlane by PlaneEntity referencedOn LargeTechnicalInspections.idPlane
    var idInspectionTeam by BrigadeEntity referencedOn LargeTechnicalInspections.idInspectionTeam
    var date by LargeTechnicalInspections.date
    var scheduledRepairs by LargeTechnicalInspections.scheduledRepairs
    var resultInspection by LargeTechnicalInspections.resultInspection
}
package entity

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentTimestamp
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import org.jetbrains.exposed.sql.or

object MedicalExaminations : IntIdTable("medicalExamination".addQuo(), "id".addQuo()) {
    val idPilot = reference("idPilot".addQuo(), Pilots.id)
    val healthy = char("healthy".addQuo()).check { (it eq 'N') or (it eq 'Y') }.default('Y')
    val conclusion = varchar("conclusion".addQuo(), 4000).nullable()
    val date = timestamp("date".addQuo()).defaultExpression(CurrentTimestamp())
}

class MedicalExaminationEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<MedicalExaminationEntity>(MedicalExaminations)

    var idPilot by PilotEntity referencedOn MedicalExaminations.idPilot
    var healthy by MedicalExaminations.healthy
    var conclusion by MedicalExaminations.conclusion
    var date by MedicalExaminations.date
}
package entity

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object AircraftRepairReports : IntIdTable("aircraftRepairReports".addQuo(), "id".addQuo()) {
    val plane = reference("plane".addQuo(), Planes)
    val repairTeam = reference("repairTeam".addQuo(), Brigades)
    val dateRepair = timestamp("dateRepair".addQuo()).nullable()
    val report = varchar("report".addQuo(), length = 4000)
}

class AircraftRepairReportEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<AircraftRepairReportEntity>(AircraftRepairReports)

    var plane by PlaneEntity referencedOn AircraftRepairReports.plane
    var repairTeam by BrigadeEntity referencedOn AircraftRepairReports.repairTeam
    var dateRepair by AircraftRepairReports.dateRepair
    var report by AircraftRepairReports.report
}
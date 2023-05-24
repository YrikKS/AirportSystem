package entity

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object ApproximateFlights : IntIdTable("approximateFlights".addQuo(), "id".addQuo()) {
    val idDepartureAirport = reference("idDepartureAirport".addQuo(), Airports)
    val idArrivalAirport = reference("idArrivalAirport".addQuo(), Airports)
    val frequencyInDays = integer("frequencyInDays".addQuo()).default(1)
    val approximateTakeoffTime = datetime("approximateTakeoffTime".addQuo())
    val approximatePrice = float("approximatePrice".addQuo())
}

class ApproximateFlightsEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ApproximateFlightsEntity>(ApproximateFlights)

    var idDepartureAirport by AirportsEntity referencedOn ApproximateFlights.idDepartureAirport
    var idArrivalAirport by AirportsEntity referencedOn ApproximateFlights.idArrivalAirport
    var frequencyInDays by ApproximateFlights.frequencyInDays
    var approximateTakeoffTime by ApproximateFlights.approximateTakeoffTime
    var approximatePrice by ApproximateFlights.approximatePrice
}